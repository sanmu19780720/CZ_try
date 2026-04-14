package com.colorsteel.erp.purchase.support;

/**
 * 采购单状态：草稿未入库；审核后入库并锁定为已审核。
 */
public final class PurchaseOrderStatus {

    public static final String DRAFT = "DRAFT";
    public static final String APPROVED = "APPROVED";
    public static final String CANCELLED = "CANCELLED";

    private PurchaseOrderStatus() {
    }
}
