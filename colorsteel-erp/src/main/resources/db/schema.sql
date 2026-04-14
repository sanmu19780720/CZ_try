-- =============================================================================
-- 彩钢商家 ERP / MIS — MySQL 8 建表脚本（步骤2）
-- 字符集：utf8mb4；引擎：InnoDB
-- 逻辑删除：deleted（0 未删 / 1 已删），与 MyBatis-Plus 约定一致
-- =============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------------------------
-- 用户与角色
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username`      VARCHAR(64)  NOT NULL COMMENT '登录名',
  `password_hash` VARCHAR(128) NOT NULL COMMENT '密码哈希',
  `real_name`     VARCHAR(64)           DEFAULT NULL COMMENT '姓名',
  `phone`         VARCHAR(32)           DEFAULT NULL COMMENT '手机',
  `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
  `role_id`       BIGINT                DEFAULT NULL COMMENT '角色ID',
  `created_by`    BIGINT                DEFAULT NULL,
  `created_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`    BIGINT                DEFAULT NULL,
  `updated_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`       TINYINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_username` (`username`),
  KEY `idx_sys_user_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `role_code`     VARCHAR(64)  NOT NULL COMMENT '角色编码',
  `role_name`     VARCHAR(128) NOT NULL COMMENT '角色名称',
  `remark`        VARCHAR(255)          DEFAULT NULL,
  `created_by`    BIGINT                DEFAULT NULL,
  `created_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`    BIGINT                DEFAULT NULL,
  `updated_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`       TINYINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色';

-- -----------------------------------------------------------------------------
-- 基础资料：客户、供应商
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `customer_no`   VARCHAR(64)  NOT NULL COMMENT '客户编号',
  `name`          VARCHAR(256) NOT NULL COMMENT '客户名称',
  `contact_name`  VARCHAR(64)           DEFAULT NULL COMMENT '联系人',
  `phone`         VARCHAR(32)           DEFAULT NULL,
  `address`       VARCHAR(512)          DEFAULT NULL,
  `remark`        VARCHAR(512)          DEFAULT NULL,
  `created_by`    BIGINT                DEFAULT NULL,
  `created_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`    BIGINT                DEFAULT NULL,
  `updated_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`       TINYINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_no` (`customer_no`),
  KEY `idx_customer_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户';

DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `supplier_no`   VARCHAR(64)  NOT NULL COMMENT '供应商编号',
  `name`          VARCHAR(256) NOT NULL COMMENT '供应商名称',
  `contact_name`  VARCHAR(64)           DEFAULT NULL,
  `phone`         VARCHAR(32)           DEFAULT NULL,
  `address`       VARCHAR(512)          DEFAULT NULL,
  `remark`        VARCHAR(512)          DEFAULT NULL,
  `created_by`    BIGINT                DEFAULT NULL,
  `created_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`    BIGINT                DEFAULT NULL,
  `updated_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`       TINYINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_no` (`supplier_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商';

-- -----------------------------------------------------------------------------
-- 商品：分类、商品、仓库
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `parent_id`     BIGINT                DEFAULT NULL COMMENT '上级分类ID',
  `name`          VARCHAR(128) NOT NULL COMMENT '分类名称',
  `sort_order`    INT          NOT NULL DEFAULT 0,
  `created_by`    BIGINT                DEFAULT NULL,
  `created_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`    BIGINT                DEFAULT NULL,
  `updated_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`       TINYINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_product_category_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类';

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id`              BIGINT         NOT NULL AUTO_INCREMENT,
  `category_id`     BIGINT                  DEFAULT NULL,
  `sku`             VARCHAR(64)    NOT NULL COMMENT 'SKU/物料编码',
  `name`            VARCHAR(256)   NOT NULL COMMENT '品名',
  `unit`            VARCHAR(32)    NOT NULL DEFAULT '件' COMMENT '计量单位',
  `spec`            VARCHAR(256)            DEFAULT NULL COMMENT '规格',
  `standard_price`  DECIMAL(18,2)           DEFAULT NULL COMMENT '参考售价',
  `last_purchase_price` DECIMAL(18,2)       DEFAULT NULL COMMENT '最近采购单价',
  `avg_cost_price`    DECIMAL(18,2)         DEFAULT NULL COMMENT '全仓加权平均成本（由库存汇总）',
  `remark`          VARCHAR(512)            DEFAULT NULL,
  `created_by`      BIGINT                  DEFAULT NULL,
  `created_at`      DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                  DEFAULT NULL,
  `updated_at`      DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT        NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_sku` (`sku`),
  KEY `idx_product_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品';

DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `warehouse_code` VARCHAR(64) NOT NULL COMMENT '仓库编码',
  `name`          VARCHAR(128) NOT NULL COMMENT '仓库名称',
  `address`       VARCHAR(512)          DEFAULT NULL,
  `created_by`    BIGINT                DEFAULT NULL,
  `created_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`    BIGINT                DEFAULT NULL,
  `updated_at`    DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`       TINYINT      NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库';

-- -----------------------------------------------------------------------------
-- 库存：现存量、流水
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id`             BIGINT          NOT NULL AUTO_INCREMENT,
  `warehouse_id`   BIGINT          NOT NULL,
  `product_id`     BIGINT          NOT NULL,
  `quantity`       DECIMAL(18,3)   NOT NULL DEFAULT 0.000 COMMENT '当前数量',
  `avg_unit_cost`  DECIMAL(18,2)            DEFAULT NULL COMMENT '加权平均单位成本',
  `created_by`     BIGINT                   DEFAULT NULL,
  `created_at`     DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`     BIGINT                   DEFAULT NULL,
  `updated_at`     DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`        TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_inv_wh_product` (`warehouse_id`, `product_id`),
  KEY `idx_inventory_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存（按仓+商品）';

DROP TABLE IF EXISTS `inventory_txn`;
CREATE TABLE `inventory_txn` (
  `id`               BIGINT          NOT NULL AUTO_INCREMENT,
  `txn_no`           VARCHAR(64)     NOT NULL COMMENT '流水单号',
  `warehouse_id`     BIGINT          NOT NULL,
  `product_id`       BIGINT          NOT NULL,
  `biz_type`         VARCHAR(32)     NOT NULL COMMENT 'PURCHASE_IN / SALES_OUT / PROJECT_ISSUE',
  `direction`        VARCHAR(8)      NOT NULL COMMENT 'IN / OUT',
  `quantity`         DECIMAL(18,3)   NOT NULL COMMENT '本笔数量（正数）',
  `unit_cost`        DECIMAL(18,2)            DEFAULT NULL COMMENT '本笔单位成本（出库时取结存成本）',
  `amount`           DECIMAL(18,2)            DEFAULT NULL COMMENT '数量*单位成本',
  `before_quantity`  DECIMAL(18,3)   NOT NULL COMMENT '变动前结存数量',
  `after_quantity`   DECIMAL(18,3)   NOT NULL COMMENT '变动后结存数量',
  `ref_type`         VARCHAR(32)              DEFAULT NULL COMMENT '来源单据类型',
  `ref_id`           BIGINT                   DEFAULT NULL COMMENT '来源单据ID',
  `ref_biz_no`       VARCHAR(64)              DEFAULT NULL COMMENT '来源业务单号',
  `remark`           VARCHAR(512)             DEFAULT NULL,
  `created_by`       BIGINT                   DEFAULT NULL,
  `created_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`       BIGINT                   DEFAULT NULL,
  `updated_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`          TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_inventory_txn_no` (`txn_no`),
  KEY `idx_inv_txn_wh_product` (`warehouse_id`, `product_id`),
  KEY `idx_inv_txn_biz` (`biz_type`, `ref_type`, `ref_id`),
  KEY `idx_inv_txn_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存流水';

-- -----------------------------------------------------------------------------
-- 采购
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE `purchase_order` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `purchase_no`     VARCHAR(64)     NOT NULL COMMENT '采购单号',
  `supplier_id`     BIGINT          NOT NULL,
  `warehouse_id`    BIGINT          NOT NULL COMMENT '入库仓库',
  `order_date`      DATE            NOT NULL,
  `status`          VARCHAR(32)     NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT/APPROVED/CANCELLED',
  `total_amount`    DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `paid_amount`     DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_purchase_no` (`purchase_no`),
  KEY `idx_po_supplier` (`supplier_id`),
  KEY `idx_po_warehouse` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购单';

DROP TABLE IF EXISTS `purchase_order_item`;
CREATE TABLE `purchase_order_item` (
  `id`               BIGINT          NOT NULL AUTO_INCREMENT,
  `purchase_order_id` BIGINT        NOT NULL,
  `line_no`          INT             NOT NULL DEFAULT 1,
  `product_id`       BIGINT          NOT NULL,
  `quantity`         DECIMAL(18,3)   NOT NULL,
  `unit_price`       DECIMAL(18,2)   NOT NULL,
  `amount`           DECIMAL(18,2)   NOT NULL,
  `remark`           VARCHAR(256)             DEFAULT NULL,
  `created_by`       BIGINT                   DEFAULT NULL,
  `created_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`       BIGINT                   DEFAULT NULL,
  `updated_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`          TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_poi_order` (`purchase_order_id`),
  KEY `idx_poi_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购明细';

DROP TABLE IF EXISTS `purchase_payment`;
CREATE TABLE `purchase_payment` (
  `id`               BIGINT          NOT NULL AUTO_INCREMENT,
  `payment_no`       VARCHAR(64)     NOT NULL COMMENT '付款单号',
  `purchase_order_id` BIGINT         NOT NULL,
  `pay_date`         DATE            NOT NULL,
  `amount`           DECIMAL(18,2)   NOT NULL,
  `pay_method`       VARCHAR(32)              DEFAULT NULL COMMENT '现金/转账等',
  `remark`           VARCHAR(512)             DEFAULT NULL,
  `created_by`       BIGINT                   DEFAULT NULL,
  `created_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`       BIGINT                   DEFAULT NULL,
  `updated_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`          TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_purchase_payment_no` (`payment_no`),
  KEY `idx_pp_order` (`purchase_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购付款';

-- -----------------------------------------------------------------------------
-- 销售
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `sales_order`;
CREATE TABLE `sales_order` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `sales_no`        VARCHAR(64)     NOT NULL COMMENT '销售单号',
  `customer_id`     BIGINT          NOT NULL,
  `warehouse_id`    BIGINT          NOT NULL COMMENT '出库仓库',
  `order_date`      DATE            NOT NULL,
  `status`          VARCHAR(32)     NOT NULL DEFAULT 'DRAFT' COMMENT 'DRAFT/APPROVED/CANCELLED',
  `total_amount`    DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `total_cost_amount` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '已固化成本合计',
  `gross_profit`    DECIMAL(18,2)            DEFAULT NULL COMMENT '毛利 total_amount - total_cost_amount',
  `received_amount` DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '已收金额',
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sales_no` (`sales_no`),
  KEY `idx_so_customer` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售单';

DROP TABLE IF EXISTS `sales_order_item`;
CREATE TABLE `sales_order_item` (
  `id`               BIGINT          NOT NULL AUTO_INCREMENT,
  `sales_order_id`   BIGINT          NOT NULL,
  `line_no`          INT             NOT NULL DEFAULT 1,
  `product_id`       BIGINT          NOT NULL,
  `quantity`         DECIMAL(18,3)   NOT NULL,
  `unit_price`       DECIMAL(18,2)   NOT NULL,
  `unit_cost`        DECIMAL(18,2)            DEFAULT NULL COMMENT '出库时固化单位成本',
  `amount`           DECIMAL(18,2)   NOT NULL,
  `cost_amount`      DECIMAL(18,2)            DEFAULT NULL COMMENT 'quantity * unit_cost',
  `gross_profit`     DECIMAL(18,2)            DEFAULT NULL COMMENT '行毛利 amount - cost_amount',
  `remark`           VARCHAR(256)             DEFAULT NULL,
  `created_by`       BIGINT                   DEFAULT NULL,
  `created_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`       BIGINT                   DEFAULT NULL,
  `updated_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`          TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_soi_order` (`sales_order_id`),
  KEY `idx_soi_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售明细';

DROP TABLE IF EXISTS `sales_receipt`;
CREATE TABLE `sales_receipt` (
  `id`               BIGINT          NOT NULL AUTO_INCREMENT,
  `receipt_no`       VARCHAR(64)     NOT NULL COMMENT '收款单号',
  `sales_order_id`   BIGINT          NOT NULL,
  `receipt_date`     DATE            NOT NULL,
  `amount`           DECIMAL(18,2)   NOT NULL,
  `pay_method`       VARCHAR(32)              DEFAULT NULL,
  `remark`           VARCHAR(512)             DEFAULT NULL,
  `created_by`       BIGINT                   DEFAULT NULL,
  `created_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`       BIGINT                   DEFAULT NULL,
  `updated_at`       DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`          TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sales_receipt_no` (`receipt_no`),
  KEY `idx_sr_order` (`sales_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售收款';

-- -----------------------------------------------------------------------------
-- 项目
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `project_no`      VARCHAR(64)     NOT NULL COMMENT '项目编号',
  `name`            VARCHAR(256)    NOT NULL COMMENT '项目名称',
  `customer_id`     BIGINT                   DEFAULT NULL,
  `status`          VARCHAR(32)     NOT NULL DEFAULT 'ONGOING',
  `start_date`      DATE                     DEFAULT NULL,
  `end_date`        DATE                     DEFAULT NULL,
  `contract_amount` DECIMAL(18,2)            DEFAULT NULL COMMENT '合同/预算收入',
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_no` (`project_no`),
  KEY `idx_project_customer` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工程项目';

DROP TABLE IF EXISTS `project_material_issue`;
CREATE TABLE `project_material_issue` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `issue_no`        VARCHAR(64)     NOT NULL COMMENT '领料单号',
  `project_id`      BIGINT          NOT NULL,
  `warehouse_id`    BIGINT          NOT NULL,
  `issue_date`      DATE            NOT NULL,
  `status`          VARCHAR(32)     NOT NULL DEFAULT 'DRAFT',
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_issue_no` (`issue_no`),
  KEY `idx_pmi_project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目领料单';

DROP TABLE IF EXISTS `project_material_issue_item`;
CREATE TABLE `project_material_issue_item` (
  `id`                   BIGINT          NOT NULL AUTO_INCREMENT,
  `material_issue_id`    BIGINT          NOT NULL,
  `line_no`              INT             NOT NULL DEFAULT 1,
  `product_id`           BIGINT          NOT NULL,
  `quantity`             DECIMAL(18,3)   NOT NULL,
  `unit_cost`            DECIMAL(18,2)            DEFAULT NULL COMMENT '领料时固化单位成本（成本价）',
  `cost_amount`          DECIMAL(18,2)            DEFAULT NULL COMMENT '行材料成本 quantity*unit_cost',
  `amount`               DECIMAL(18,2)            DEFAULT NULL COMMENT '同 cost_amount，兼容字段',
  `remark`               VARCHAR(256)             DEFAULT NULL,
  `created_by`           BIGINT                   DEFAULT NULL,
  `created_at`           DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`           BIGINT                   DEFAULT NULL,
  `updated_at`           DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`              TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_pmii_issue` (`material_issue_id`),
  KEY `idx_pmii_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目领料明细';

DROP TABLE IF EXISTS `project_receipt`;
CREATE TABLE `project_receipt` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `receipt_no`      VARCHAR(64)     NOT NULL COMMENT '项目收款单号',
  `project_id`      BIGINT          NOT NULL,
  `receipt_date`    DATE            NOT NULL,
  `amount`          DECIMAL(18,2)   NOT NULL,
  `pay_method`      VARCHAR(32)              DEFAULT NULL,
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_receipt_no` (`receipt_no`),
  KEY `idx_pr_project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目收款';

DROP TABLE IF EXISTS `project_expense`;
CREATE TABLE `project_expense` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `expense_no`      VARCHAR(64)     NOT NULL COMMENT '项目费用单号',
  `project_id`      BIGINT          NOT NULL,
  `expense_date`    DATE            NOT NULL,
  `category`        VARCHAR(64)              DEFAULT NULL COMMENT '费用类别',
  `amount`          DECIMAL(18,2)   NOT NULL,
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_expense_no` (`expense_no`),
  KEY `idx_pe_project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目费用';

-- -----------------------------------------------------------------------------
-- 工人：工人、考勤、工资
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `worker`;
CREATE TABLE `worker` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `worker_no`       VARCHAR(64)     NOT NULL COMMENT '工人编号',
  `name`            VARCHAR(64)     NOT NULL,
  `phone`           VARCHAR(32)              DEFAULT NULL,
  `id_card_no`      VARCHAR(32)              DEFAULT NULL,
  `daily_wage`      DECIMAL(18,2)            DEFAULT NULL COMMENT '日单价 unit_wage（元/工日）',
  `status`          TINYINT         NOT NULL DEFAULT 1,
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_worker_no` (`worker_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工人';

DROP TABLE IF EXISTS `worker_attendance`;
CREATE TABLE `worker_attendance` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `attendance_no`   VARCHAR(64)     NOT NULL COMMENT '考勤单号',
  `worker_id`       BIGINT          NOT NULL,
  `work_date`       DATE            NOT NULL,
  `work_days`       DECIMAL(18,3)   NOT NULL DEFAULT 1.000 COMMENT '出勤工日：1 全天 / 0.5 半天等',
  `work_hours`      DECIMAL(18,3)            DEFAULT NULL COMMENT '工时（可选）',
  `status`          VARCHAR(32)     NOT NULL DEFAULT 'NORMAL' COMMENT 'NORMAL/ABSENT 等',
  `remark`          VARCHAR(256)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_worker_attendance_no` (`attendance_no`),
  UNIQUE KEY `uk_worker_date` (`worker_id`, `work_date`),
  KEY `idx_wa_worker` (`worker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工人考勤';

DROP TABLE IF EXISTS `payroll`;
CREATE TABLE `payroll` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `payroll_no`      VARCHAR(64)     NOT NULL COMMENT '工资单号',
  `worker_id`       BIGINT          NOT NULL,
  `project_id`      BIGINT                   DEFAULT NULL COMMENT '归属项目（用于项目人工成本闭环）',
  `period_start`    DATE            NOT NULL,
  `period_end`      DATE            NOT NULL,
  `work_days`       DECIMAL(18,3)            DEFAULT NULL COMMENT '计薪工日',
  `unit_wage`       DECIMAL(18,2)            DEFAULT NULL COMMENT '计薪日单价快照',
  `amount`          DECIMAL(18,2)   NOT NULL COMMENT '实发金额 work_days×unit_wage+奖金-扣款',
  `deduction`       DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '扣款',
  `bonus`           DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payroll_no` (`payroll_no`),
  KEY `idx_payroll_worker` (`worker_id`),
  KEY `idx_payroll_project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工资发放';

-- -----------------------------------------------------------------------------
-- 财务
-- -----------------------------------------------------------------------------

DROP TABLE IF EXISTS `expense_record`;
CREATE TABLE `expense_record` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `expense_no`      VARCHAR(64)     NOT NULL COMMENT '费用单号',
  `expense_date`    DATE            NOT NULL,
  `category`        VARCHAR(64)              DEFAULT NULL,
  `amount`          DECIMAL(18,2)   NOT NULL,
  `project_id`      BIGINT                   DEFAULT NULL COMMENT '可关联项目',
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_expense_record_no` (`expense_no`),
  KEY `idx_er_project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='费用登记';

DROP TABLE IF EXISTS `profit_stat_daily`;
CREATE TABLE `profit_stat_daily` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `stat_date`       DATE            NOT NULL COMMENT '统计日',
  `sales_revenue`   DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '销售收入（已审核销售）',
  `project_revenue` DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '项目收款收入',
  `revenue`         DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '总营收 sales_revenue+project_revenue',
  `material_cost`   DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '材料成本（销售出库成本+项目领料）',
  `labor_cost`      DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '人工成本（工资按计薪周期均摊到天）',
  `expense_amount`  DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '项目费用',
  `gross_profit`    DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '毛利 营收-材料',
  `net_profit`      DECIMAL(18,2)   NOT NULL DEFAULT 0.00 COMMENT '净利 毛利-人工-费用',
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_profit_stat_daily_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='利润日统计';

DROP TABLE IF EXISTS `profit_stat_period`;
CREATE TABLE `profit_stat_period` (
  `id`              BIGINT          NOT NULL AUTO_INCREMENT,
  `period_type`     VARCHAR(16)     NOT NULL COMMENT 'MONTH / QUARTER / YEAR',
  `period_key`      VARCHAR(32)     NOT NULL COMMENT '如 2026-03、2026Q1',
  `period_start`    DATE            NOT NULL,
  `period_end`      DATE            NOT NULL,
  `revenue`         DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `material_cost`   DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `labor_cost`      DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `expense_amount`  DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `gross_profit`    DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `net_profit`      DECIMAL(18,2)   NOT NULL DEFAULT 0.00,
  `remark`          VARCHAR(512)             DEFAULT NULL,
  `created_by`      BIGINT                   DEFAULT NULL,
  `created_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_by`      BIGINT                   DEFAULT NULL,
  `updated_at`      DATETIME(3)     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `deleted`         TINYINT         NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_profit_stat_period` (`period_type`, `period_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='利润区间统计';

SET FOREIGN_KEY_CHECKS = 1;
