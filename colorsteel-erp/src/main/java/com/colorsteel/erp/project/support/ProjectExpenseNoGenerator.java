package com.colorsteel.erp.project.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class ProjectExpenseNoGenerator {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private ProjectExpenseNoGenerator() {
    }

    public static String next() {
        return "PEX" + TS.format(LocalDateTime.now())
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10_000));
    }
}
