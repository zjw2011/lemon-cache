package org.lemonframework.cache.anno.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

/**
 * config selector.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class ConfigSelector extends AdviceModeImportSelector<EnableMethodCache> {

    @Override
    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return getProxyImports();
            case ASPECTJ:
//                return getAspectJImports();
            default:
                return null;
        }
    }

    /**
     * Return the imports to use if the {@link AdviceMode} is set to {@link AdviceMode#PROXY}.
     * <p>Take care of adding the necessary JSR-107 import if it is available.
     */
    private String[] getProxyImports() {
        List<String> result = new ArrayList<String>();
        result.add(AutoProxyRegistrar.class.getName());
        result.add(LemonCacheProxyConfiguration.class.getName());
        return result.toArray(new String[result.size()]);
    }

    /**
     * Return the imports to use if the {@link AdviceMode} is set to {@link AdviceMode#ASPECTJ}.
     * <p>Take care of adding the necessary JSR-107 import if it is available.
     */
//    private String[] getAspectJImports() {
//        List<String> result = new ArrayList<String>();
//        result.add(CACHE_ASPECT_CONFIGURATION_CLASS_NAME);
//        return result.toArray(new String[result.size()]);
//    }
}
