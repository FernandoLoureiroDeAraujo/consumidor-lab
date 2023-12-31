package br.com.flallaca.processor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TryCatchFunction {

    private static final Logger logger = LoggerFactory.getLogger(TryCatchFunction.class);

    public static void execute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }
}
