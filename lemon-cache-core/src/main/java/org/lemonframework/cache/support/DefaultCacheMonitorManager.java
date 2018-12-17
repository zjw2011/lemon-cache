package org.lemonframework.cache.support;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * cache monitor manager.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class DefaultCacheMonitorManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultCacheMonitorManager.class);

    protected CopyOnWriteArrayList<DefaultCacheMonitor> monitorList = new CopyOnWriteArrayList();

    private ScheduledFuture<?> future;

    private int resetTime;
    private TimeUnit resetTimeUnit;
    private Consumer<StatInfo> statCallback;

    public DefaultCacheMonitorManager(int resetTime, TimeUnit resetTimeUnit, Consumer<StatInfo> statCallback) {
        this.resetTime = resetTime;
        this.resetTimeUnit = resetTimeUnit;
        this.statCallback = statCallback;
    }

    public DefaultCacheMonitorManager(int resetTime, TimeUnit resetTimeUnit) {
        this(resetTime, resetTimeUnit, false);
    }

    public DefaultCacheMonitorManager(int resetTime, TimeUnit resetTimeUnit, boolean verboseLog) {
        this.resetTime = resetTime;
        this.resetTimeUnit = resetTimeUnit;
        this.statCallback = new StatInfoLogger(verboseLog);
    }

    Runnable cmd = new Runnable() {
        private long time = System.currentTimeMillis();

        @Override
        public void run() {
            try {
                List<CacheStat> stats = monitorList.stream().map((m) -> {
                    CacheStat stat = m.getCacheStat();
                    m.resetStat();
                    return stat;
                }).collect(Collectors.toList());

                long endTime = System.currentTimeMillis();
                StatInfo statInfo = new StatInfo();
                statInfo.setStartTime(time);
                statInfo.setEndTime(endTime);
                statInfo.setStats(stats);
                time = endTime;

                statCallback.accept(statInfo);
            } catch (Exception e) {
                logger.error("jetcache DefaultCacheMonitorManager error", e);
            }
        }
    };

    @PostConstruct
    public synchronized void start() {
        if (future != null) {
            return;
        }
        long delay = firstDelay(resetTime, resetTimeUnit);
        future = LemonCacheExecutor.defaultExecutor().scheduleAtFixedRate(
                cmd, delay, resetTimeUnit.toMillis(resetTime), TimeUnit.MILLISECONDS);
        logger.info("cache stat period at " + resetTime + " " + resetTimeUnit);
    }

    @PreDestroy
    public synchronized void stop() {
        future.cancel(false);
        logger.info("cache stat canceled");
        future = null;
    }

    public DefaultCacheMonitorManager add(DefaultCacheMonitor... monitors) {
        monitorList.addAll(Arrays.asList(monitors));
        return this;
    }

    public DefaultCacheMonitorManager remove(DefaultCacheMonitor... monitor) {
        monitorList.remove(monitor);
        return this;
    }

    protected static long firstDelay(int resetTime, TimeUnit resetTimeUnit) {
        LocalDateTime firstResetTime = computeFirstResetTime(LocalDateTime.now(), resetTime, resetTimeUnit);
        return firstResetTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();
    }

    protected static LocalDateTime computeFirstResetTime(LocalDateTime baseTime, int time, TimeUnit unit) {
        if (unit != TimeUnit.SECONDS && unit != TimeUnit.MINUTES && unit != TimeUnit.HOURS && unit != TimeUnit.DAYS) {
            throw new IllegalArgumentException();
        }
        LocalDateTime t = baseTime;
        switch (unit) {
            case DAYS:
                t = t.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case HOURS:
                if (24 % time == 0) {
                    t = t.plusHours(time - t.getHour() % time);
                } else {
                    t = t.plusHours(1);
                }
                t = t.withMinute(0).withSecond(0).withNano(0);
                break;
            case MINUTES:
                if (60 % time == 0) {
                    t = t.plusMinutes(time - t.getMinute() % time);
                } else {
                    t = t.plusMinutes(1);
                }
                t = t.withSecond(0).withNano(0);
                break;
            case SECONDS:
                if (60 % time == 0) {
                    t = t.plusSeconds(time - t.getSecond() % time);
                } else {
                    t = t.plusSeconds(1);
                }
                t = t.withNano(0);
                break;
        }
        return t;
    }


}
