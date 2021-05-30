package com.zaid.spring.webflux.loggingdemo.util;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.ContextView;

import java.util.Map;
import java.util.function.Consumer;

public class LogHelper {

    public static final String MDC_CONTEXT = "mdc-context";

    public static void log(ContextView context, Runnable log) {
        Map<String, String> mdcContext = context.get(MDC_CONTEXT);
        try {
            MDC.setContextMap(mdcContext);
            log.run();
        } finally {
            MDC.clear();
        }
    }

    public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> log) {
        return signal -> {
            if (!signal.isOnNext())
                return;
            Map<String, String> mdcContext = signal.getContextView().get(MDC_CONTEXT);
            try {
                MDC.setContextMap(mdcContext);
                log.accept(signal.get());
            } finally {
                MDC.clear();
            }
        };
    }

    public static <T> Consumer<Signal<T>> logOnError(Consumer<Throwable> log) {
        return signal -> {
            if (!signal.isOnError())
                return;
            Map<String, String> mdcContext = signal.getContextView().get(MDC_CONTEXT);
            try {
                MDC.setContextMap(mdcContext);
                log.accept(signal.getThrowable());
            } finally {
                MDC.clear();
            }
        };
    }

}
