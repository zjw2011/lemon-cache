/**
 * Created on 2018/1/19.
 */
package org.lemonframework.cache.anno.method;

import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemonframework.cache.CacheConfigException;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class ExpressionEvaluator implements Function<Object, Object> {
    private static final Pattern pattern = Pattern.compile("\\s*(\\w+)\\s*\\{(.+)\\}\\s*");
    private Function<Object, Object> target;

    public ExpressionEvaluator(String script, Method defineMethod) {
        Object rt[] = parseEL(script);
        EL el = (EL) rt[0];
        String realScript = (String) rt[1];
        if (el == EL.MVEL) {
            target = new MvelEvaluator(realScript);
        } else if (el == EL.SPRING_EL) {
            target = new SpelEvaluator(realScript, defineMethod);
        }
    }

    private Object[] parseEL(String script) {
        if (script == null || script.trim().equals("")) {
            return null;
        }
        Object[] rt = new Object[2];
        Matcher matcher = pattern.matcher(script);
        if (!matcher.matches()) {
            rt[0] = EL.SPRING_EL; // default spel since 2.4
            rt[1] = script;
            return rt;
        } else {
            String s = matcher.group(1);
            if ("spel".equals(s)) {
                rt[0] = EL.SPRING_EL;
            } else if ("mvel".equals(s)) {
                rt[0] = EL.MVEL;
            }/* else if ("buildin".equals(s)) {
                rt[0] = EL.BUILD_IN;
            } */ else {
                throw new CacheConfigException("Can't parse \"" + script + "\"");
            }
            rt[1] = matcher.group(2);
            return rt;
        }
    }

    @Override
    public Object apply(Object o) {
        return target.apply(o);
    }

    Function<Object, Object> getTarget() {
        return target;
    }
}

