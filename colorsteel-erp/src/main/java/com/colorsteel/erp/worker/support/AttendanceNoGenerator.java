package com.colorsteel.erp.worker.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class AttendanceNoGenerator {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private AttendanceNoGenerator() {
    }

    public static String next() {
        return "WA" + TS.format(LocalDateTime.now())
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10_000));
    }
}
