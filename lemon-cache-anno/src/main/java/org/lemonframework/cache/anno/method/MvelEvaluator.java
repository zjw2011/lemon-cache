package org.lemonframework.cache.anno.method;

import java.util.function.Function;

import org.mvel2.MVEL;

class MvelEvaluator implements Function<Object, Object> {
    private String script;

    public MvelEvaluator(String script) {
        this.script = script;
    }

    @Override
    public Object apply(Object context) {
        return MVEL.eval(script, context);
    }
}

