package org.lemonframework.cache.autoconfigure;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.masterslave.MasterSlave;
import io.lettuce.core.masterslave.StatefulRedisMasterSlaveConnection;
import org.lemonframework.cache.CacheBuilder;
import org.lemonframework.cache.CacheConfigException;
import org.lemonframework.cache.anno.CacheConsts;
import org.lemonframework.cache.external.ExternalCacheBuilder;
import org.lemonframework.cache.redis.lettuce.LemonCacheCodec;
import org.lemonframework.cache.redis.lettuce.LettuceConnectionManager;
import org.lemonframework.cache.redis.lettuce.RedisLettuceCacheBuilder;

/**
 * lettuce redis auto configuration.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
@Configuration
@Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
public class RedisLettuceAutoConfiguration {
    public static final String AUTO_INIT_BEAN_NAME = "redisLettuceAutoInit";

    public static class RedisLettuceCondition extends LemonCacheCondition {
        public RedisLettuceCondition() {
            super("redis.lettuce");
        }
    }

    @Bean(name = {AUTO_INIT_BEAN_NAME})
    public RedisLettuceAutoInit redisLettuceAutoInit() {
        return new RedisLettuceAutoInit();
    }

    public static class RedisLettuceAutoInit extends ExternalCacheAutoInit {

        public RedisLettuceAutoInit() {
            super("redis.lettuce");
        }

        @Override
        protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
            Map<String, Object> map = ct.subTree("uri"/*there is no dot*/).getProperties();
            String readFromStr = ct.getProperty("readFrom");
            long asyncResultTimeoutInMillis = Long.parseLong(
                    ct.getProperty("asyncResultTimeoutInMillis", Long.toString(CacheConsts.ASYNC_RESULT_TIMEOUT.toMillis())));
            ReadFrom readFrom = null;
            if (readFromStr != null) {
                readFrom = ReadFrom.valueOf(readFromStr.trim());
            }

            AbstractRedisClient client;
            StatefulConnection connection = null;
            if (map == null || map.size() == 0) {
                throw new CacheConfigException("uri is required");
            } else if (map.size() == 1) {
                String uri = (String) map.values().iterator().next();
                if (readFrom == null) {
                    client = RedisClient.create(uri);
                    ((RedisClient)client).setOptions(ClientOptions.builder().
                            disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS).build());
                } else {
                    client = RedisClient.create();
                    ((RedisClient)client).setOptions(ClientOptions.builder().
                            disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS).build());
                    StatefulRedisMasterSlaveConnection c = MasterSlave.connect(
                            (RedisClient) client,
                            new LemonCacheCodec(),
                            RedisURI.create(uri));
                    c.setReadFrom(readFrom);
                    connection = c;
                }
            } else {
                List<RedisURI> list = map.values().stream().map((k) -> RedisURI.create(URI.create(k.toString())))
                        .collect(Collectors.toList());
                client = RedisClusterClient.create(list);
                ((RedisClusterClient)client).setOptions(ClusterClientOptions.builder().
                        disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS).build());
                if (readFrom != null) {
                    StatefulRedisClusterConnection c = ((RedisClusterClient) client).connect(new LemonCacheCodec());
                    c.setReadFrom(readFrom);
                    connection = c;
                }
            }

            ExternalCacheBuilder externalCacheBuilder = RedisLettuceCacheBuilder.createRedisLettuceCacheBuilder()
                    .connection(connection)
                    .redisClient(client)
                    .asyncResultTimeoutInMillis(asyncResultTimeoutInMillis);
            parseGeneralConfig(externalCacheBuilder, ct);

            // eg: "remote.default.client"
            autoConfigureBeans.getCustomContainer().put(cacheAreaWithPrefix + ".client", client);
            LettuceConnectionManager m = LettuceConnectionManager.defaultManager();
            m.init(client, connection);
            autoConfigureBeans.getCustomContainer().put(cacheAreaWithPrefix + ".connection", m.connection(client));
            autoConfigureBeans.getCustomContainer().put(cacheAreaWithPrefix + ".commands", m.commands(client));
            autoConfigureBeans.getCustomContainer().put(cacheAreaWithPrefix + ".asyncCommands", m.asyncCommands(client));
            autoConfigureBeans.getCustomContainer().put(cacheAreaWithPrefix + ".reactiveCommands", m.reactiveCommands(client));
            return externalCacheBuilder;
        }
    }
}
