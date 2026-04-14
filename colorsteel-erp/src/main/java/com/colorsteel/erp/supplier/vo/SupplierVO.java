package com.colorsteel.erp.supplier.vo;

import com.colorsteel.erp.supplier.entity.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "供应商展示")
public class SupplierVO {

    private Long id;
    private String supplierNo;
    private String name;
    private String contactName;
    private String phone;
    private String address;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SupplierVO from(Supplier e) {
        if (e == null) {
            return null;
        }
        SupplierVO vo = new SupplierVO();
        vo.setId(e.getId());
        vo.setSupplierNo(e.getSupplierNo());
        vo.setName(e.getName());
        vo.setContactName(e.getContactName());
        vo.setPhone(e.getPhone());
        vo.setAddress(e.getAddress());
        vo.setRemark(e.getRemark());
        vo.setCreatedAt(e.getCreatedAt());
        vo.setUpdatedAt(e.getUpdatedAt());
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
