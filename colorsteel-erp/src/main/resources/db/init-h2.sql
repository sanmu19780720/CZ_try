-- H2（MODE=MySQL）开发库初始化：步骤 3 涉及的 7 张表（无 ENGINE/COLLATE，便于嵌入式启动）

DROP TABLE IF EXISTS profit_stat_daily;
DROP TABLE IF EXISTS payroll;
DROP TABLE IF EXISTS worker_attendance;
DROP TABLE IF EXISTS worker;
DROP TABLE IF EXISTS project_expense;
DROP TABLE IF EXISTS project_receipt;
DROP TABLE IF EXISTS project_material_issue_item;
DROP TABLE IF EXISTS project_material_issue;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS sales_receipt;
DROP TABLE IF EXISTS sales_order_item;
DROP TABLE IF EXISTS sales_order;
DROP TABLE IF EXISTS purchase_order_item;
DROP TABLE IF EXISTS purchase_payment;
DROP TABLE IF EXISTS purchase_order;
DROP TABLE IF EXISTS inventory_txn;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS supplier;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS warehouse;

CREATE TABLE sys_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL,
  password_hash VARCHAR(128) NOT NULL,
  real_name VARCHAR(64),
  phone VARCHAR(32),
  status TINYINT NOT NULL DEFAULT 1,
  role_id BIGINT,
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_sys_user_username ON sys_user (username);
CREATE INDEX idx_sys_user_role_id ON sys_user (role_id);

CREATE TABLE sys_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_code VARCHAR(64) NOT NULL,
  role_name VARCHAR(128) NOT NULL,
  remark VARCHAR(255),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_sys_role_code ON sys_role (role_code);

CREATE TABLE customer (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_no VARCHAR(64) NOT NULL,
  name VARCHAR(256) NOT NULL,
  contact_name VARCHAR(64),
  phone VARCHAR(32),
  address VARCHAR(512),
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_customer_no ON customer (customer_no);
CREATE INDEX idx_customer_name ON customer (name);

CREATE TABLE supplier (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  supplier_no VARCHAR(64) NOT NULL,
  name VARCHAR(256) NOT NULL,
  contact_name VARCHAR(64),
  phone VARCHAR(32),
  address VARCHAR(512),
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_supplier_no ON supplier (supplier_no);

CREATE TABLE product_category (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  parent_id BIGINT,
  name VARCHAR(128) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_product_category_parent ON product_category (parent_id);

CREATE TABLE product (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  category_id BIGINT,
  sku VARCHAR(64) NOT NULL,
  name VARCHAR(256) NOT NULL,
  unit VARCHAR(32) NOT NULL DEFAULT '件',
  spec VARCHAR(256),
  standard_price DECIMAL(18,2),
  last_purchase_price DECIMAL(18,2),
  avg_cost_price DECIMAL(18,2),
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_product_sku ON product (sku);
CREATE INDEX idx_product_category ON product (category_id);

CREATE TABLE warehouse (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  warehouse_code VARCHAR(64) NOT NULL,
  name VARCHAR(128) NOT NULL,
  address VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_warehouse_code ON warehouse (warehouse_code);

CREATE TABLE inventory (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  warehouse_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity DECIMAL(18,3) NOT NULL DEFAULT 0.000,
  avg_unit_cost DECIMAL(18,2),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_inv_wh_product ON inventory (warehouse_id, product_id);
CREATE INDEX idx_inventory_product ON inventory (product_id);

CREATE TABLE inventory_txn (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  txn_no VARCHAR(64) NOT NULL,
  warehouse_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  biz_type VARCHAR(32) NOT NULL,
  direction VARCHAR(8) NOT NULL,
  quantity DECIMAL(18,3) NOT NULL,
  unit_cost DECIMAL(18,2),
  amount DECIMAL(18,2),
  before_quantity DECIMAL(18,3) NOT NULL,
  after_quantity DECIMAL(18,3) NOT NULL,
  ref_type VARCHAR(32),
  ref_id BIGINT,
  ref_biz_no VARCHAR(64),
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_inventory_txn_no ON inventory_txn (txn_no);
CREATE INDEX idx_inv_txn_wh_product ON inventory_txn (warehouse_id, product_id);
CREATE INDEX idx_inv_txn_biz ON inventory_txn (biz_type, ref_type, ref_id);

CREATE TABLE purchase_order (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  purchase_no VARCHAR(64) NOT NULL,
  supplier_id BIGINT NOT NULL,
  warehouse_id BIGINT NOT NULL,
  order_date DATE NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  total_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  paid_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_purchase_no ON purchase_order (purchase_no);
CREATE INDEX idx_po_supplier ON purchase_order (supplier_id);
CREATE INDEX idx_po_warehouse ON purchase_order (warehouse_id);

CREATE TABLE purchase_order_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  purchase_order_id BIGINT NOT NULL,
  line_no INT NOT NULL DEFAULT 1,
  product_id BIGINT NOT NULL,
  quantity DECIMAL(18,3) NOT NULL,
  unit_price DECIMAL(18,2) NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  remark VARCHAR(256),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_poi_order ON purchase_order_item (purchase_order_id);
CREATE INDEX idx_poi_product ON purchase_order_item (product_id);

CREATE TABLE purchase_payment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  payment_no VARCHAR(64) NOT NULL,
  purchase_order_id BIGINT NOT NULL,
  pay_date DATE NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  pay_method VARCHAR(32),
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_purchase_payment_no ON purchase_payment (payment_no);
CREATE INDEX idx_pp_order ON purchase_payment (purchase_order_id);

CREATE TABLE sales_order (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sales_no VARCHAR(64) NOT NULL,
  customer_id BIGINT NOT NULL,
  warehouse_id BIGINT NOT NULL,
  order_date DATE NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  total_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  total_cost_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  gross_profit DECIMAL(18,2),
  received_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_sales_no ON sales_order (sales_no);
CREATE INDEX idx_so_customer ON sales_order (customer_id);

CREATE TABLE sales_order_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sales_order_id BIGINT NOT NULL,
  line_no INT NOT NULL DEFAULT 1,
  product_id BIGINT NOT NULL,
  quantity DECIMAL(18,3) NOT NULL,
  unit_price DECIMAL(18,2) NOT NULL,
  unit_cost DECIMAL(18,2),
  amount DECIMAL(18,2) NOT NULL,
  cost_amount DECIMAL(18,2),
  gross_profit DECIMAL(18,2),
  remark VARCHAR(256),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_soi_order ON sales_order_item (sales_order_id);
CREATE INDEX idx_soi_product ON sales_order_item (product_id);

CREATE TABLE sales_receipt (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  receipt_no VARCHAR(64) NOT NULL,
  sales_order_id BIGINT NOT NULL,
  receipt_date DATE NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  pay_method VARCHAR(32),
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_sales_receipt_no ON sales_receipt (receipt_no);
CREATE INDEX idx_sr_order ON sales_receipt (sales_order_id);

CREATE TABLE project (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  project_no VARCHAR(64) NOT NULL,
  name VARCHAR(256) NOT NULL,
  customer_id BIGINT,
  status VARCHAR(32) NOT NULL DEFAULT 'ONGOING',
  start_date DATE,
  end_date DATE,
  contract_amount DECIMAL(18,2),
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_project_no ON project (project_no);
CREATE INDEX idx_project_customer ON project (customer_id);

CREATE TABLE project_material_issue (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  issue_no VARCHAR(64) NOT NULL,
  project_id BIGINT NOT NULL,
  warehouse_id BIGINT NOT NULL,
  issue_date DATE NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_project_issue_no ON project_material_issue (issue_no);
CREATE INDEX idx_pmi_project ON project_material_issue (project_id);

CREATE TABLE project_material_issue_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  material_issue_id BIGINT NOT NULL,
  line_no INT NOT NULL DEFAULT 1,
  product_id BIGINT NOT NULL,
  quantity DECIMAL(18,3) NOT NULL,
  unit_cost DECIMAL(18,2),
  cost_amount DECIMAL(18,2),
  amount DECIMAL(18,2),
  remark VARCHAR(256),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_pmii_issue ON project_material_issue_item (material_issue_id);
CREATE INDEX idx_pmii_product ON project_material_issue_item (product_id);

CREATE TABLE project_receipt (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  receipt_no VARCHAR(64) NOT NULL,
  project_id BIGINT NOT NULL,
  receipt_date DATE NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  pay_method VARCHAR(32),
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_project_receipt_no ON project_receipt (receipt_no);
CREATE INDEX idx_pr_project ON project_receipt (project_id);

CREATE TABLE project_expense (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  expense_no VARCHAR(64) NOT NULL,
  project_id BIGINT NOT NULL,
  expense_date DATE NOT NULL,
  category VARCHAR(64),
  amount DECIMAL(18,2) NOT NULL,
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_project_expense_no ON project_expense (expense_no);
CREATE INDEX idx_pe_project ON project_expense (project_id);

CREATE TABLE worker (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  worker_no VARCHAR(64) NOT NULL,
  name VARCHAR(64) NOT NULL,
  phone VARCHAR(32),
  id_card_no VARCHAR(32),
  daily_wage DECIMAL(18,2),
  status TINYINT NOT NULL DEFAULT 1,
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_worker_no ON worker (worker_no);

CREATE TABLE worker_attendance (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  attendance_no VARCHAR(64) NOT NULL,
  worker_id BIGINT NOT NULL,
  work_date DATE NOT NULL,
  work_days DECIMAL(18,3) NOT NULL DEFAULT 1.000,
  work_hours DECIMAL(18,3),
  status VARCHAR(32) NOT NULL DEFAULT 'NORMAL',
  remark VARCHAR(256),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_worker_attendance_no ON worker_attendance (attendance_no);
CREATE UNIQUE INDEX uk_worker_date ON worker_attendance (worker_id, work_date);
CREATE INDEX idx_wa_worker ON worker_attendance (worker_id);

CREATE TABLE payroll (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  payroll_no VARCHAR(64) NOT NULL,
  worker_id BIGINT NOT NULL,
  project_id BIGINT,
  period_start DATE NOT NULL,
  period_end DATE NOT NULL,
  work_days DECIMAL(18,3),
  unit_wage DECIMAL(18,2),
  amount DECIMAL(18,2) NOT NULL,
  deduction DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  bonus DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_payroll_no ON payroll (payroll_no);
CREATE INDEX idx_payroll_worker ON payroll (worker_id);
CREATE INDEX idx_payroll_project ON payroll (project_id);

CREATE TABLE profit_stat_daily (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  stat_date DATE NOT NULL,
  sales_revenue DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  project_revenue DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  revenue DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  material_cost DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  labor_cost DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  expense_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  gross_profit DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  net_profit DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  remark VARCHAR(512),
  created_by BIGINT,
  created_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by BIGINT,
  updated_at TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted TINYINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_profit_stat_daily_date ON profit_stat_daily (stat_date);
