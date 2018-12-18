package org.lemonframework.cache.autoconfigure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;

/**
 * cache condition.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class LemonCacheCondition extends SpringBootCondition {

    private String[] cacheTypes;

    protected LemonCacheCondition(String... cacheTypes) {
        Objects.requireNonNull(cacheTypes, "cacheTypes can't be null");
        Assert.isTrue(cacheTypes.length > 0, "cacheTypes length is 0");
        this.cacheTypes = cacheTypes;
    }

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        ConfigTree ct = new ConfigTree((ConfigurableEnvironment) conditionContext.getEnvironment(), "lemon.cache.");
        if (match(ct, "local.") || match(ct, "remote.")) {
            return ConditionOutcome.match();
        } else {
            return ConditionOutcome.noMatch("no match for " + cacheTypes[0]);
        }
    }

    private boolean match(ConfigTree ct, String prefix) {
        Map<String, Object> m = ct.subTree(prefix).getProperties();
        Set<String> cacheAreaNames = m.keySet().stream().map((s) -> s.substring(0, s.indexOf('.'))).collect(Collectors.toSet());
        final List<String> cacheTypesList = Arrays.asList(cacheTypes);
        return cacheAreaNames.stream().anyMatch((s) -> cacheTypesList.contains(m.get(s + ".type")));
    }
}
