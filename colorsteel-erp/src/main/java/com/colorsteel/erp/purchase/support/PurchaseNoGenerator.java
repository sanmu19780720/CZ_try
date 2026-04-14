package com.colorsteel.erp.purchase.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class PurchaseNoGenerator {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private PurchaseNoGenerator() {
    }

    public static String next() {
        return "PO" + TS.format(LocalDateTime.now())
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10_000));
    }
}
