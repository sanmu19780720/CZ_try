package com.colorsteel.erp.inventory.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成全局唯一流水单号（进程内唯一性足够支撑单机；分布式可换雪花/号段）。
 */
public final class InventoryTxnNoGenerator {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private InventoryTxnNoGenerator() {
    }

    public static String next() {
        return "TXN" + TS.format(LocalDateTime.now())
                + String.format("%05d", ThreadLocalRandom.current().nextInt(100_000));
    }
}
