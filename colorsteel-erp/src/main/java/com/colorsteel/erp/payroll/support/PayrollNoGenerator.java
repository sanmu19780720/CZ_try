package com.colorsteel.erp.payroll.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class PayrollNoGenerator {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private PayrollNoGenerator() {
    }

    public static String next() {
        return "PAY" + TS.format(LocalDateTime.now())
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10_000));
    }
}
