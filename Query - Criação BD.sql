DROP TABLE dbo.history_invoice
DROP TABLE dbo.tax_invoice_item
DROP TABLE dbo.tax_invoice
DROP TABLE dbo.credit_card
DROP TABLE dbo.invoice_address
DROP TABLE dbo.customer_address
DROP TABLE dbo.customers


CREATE TABLE dbo.customers(
	id INT IDENTITY(1,1) PRIMARY KEY ,
	user_id INT,
	document VARCHAR(50),
	customer_type CHAR(1),
	fullname VARCHAR(300),
	birth_date DATE,
	phone_1 VARCHAR(20),
	phone_2 VARCHAR(20),
	FOREIGN KEY (user_id) REFERENCES usr.users(id)
);

CREATE TABLE dbo.customer_address(
	id INT IDENTITY(1,1) PRIMARY KEY ,
	customer_id INT,
	customer_address_type CHAR(1),
	nickname VARCHAR(50),
	zip_code VARCHAR(11), 
	address_type VARCHAR(20), 
	address VARCHAR(250), 
	number VARCHAR(20), 
	complement VARCHAR(100), 
	neighborhood VARCHAR(100), 
	city VARCHAR(100), 
	state CHAR(2),
	status CHAR(1),
	FOREIGN KEY (customer_id) REFERENCES dbo.customers(id)
);

CREATE TABLE dbo.invoice_address(
	id INT IDENTITY(1,1) PRIMARY KEY ,
	customer_address_type CHAR(1),
	nickname VARCHAR(50),
	zip_code VARCHAR(11), 
	address_type VARCHAR(20), 
	address VARCHAR(250), 
	number VARCHAR(20), 
	complement VARCHAR(100), 
	neighborhood VARCHAR(100), 
	city VARCHAR(100), 
	state CHAR(2)
);

CREATE TABLE dbo.credit_card(
	token VARCHAR(200) PRIMARY KEY,
	card_number VARCHAR(16),
	cvv VARCHAR(5),
	fullname VARCHAR(300),
	expire_date VARCHAR(7)
)

CREATE TABLE dbo.tax_invoice (
    id INT IDENTITY(1,1) PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL,
	document VARCHAR(50),
	customer_type CHAR(1),
	fullname VARCHAR(300),
    total_cost DECIMAL(10, 2) NOT NULL,
	shipping_cost DECIMAL(10, 2),
    customer_id INT,
	invoice_status VARCHAR(2),
    created_at DATETIME DEFAULT GETDATE(),
	issue_date DATE NOT NULL,
	billing_address_id INT,
	delivery_address_id INT,
	payment_type VARCHAR(2),
	token_credit_card VARCHAR(200),
	installments_number INT,
	FOREIGN KEY (delivery_address_id) REFERENCES dbo.invoice_address(id),
	FOREIGN KEY (billing_address_id) REFERENCES dbo.invoice_address(id),
	FOREIGN KEY (customer_id) REFERENCES dbo.customers(id),
	FOREIGN KEY (token_credit_c"ard) REFERENCES dbo.credit_card(token)
);

CREATE TABLE dbo.history_invoice(
	id INT IDENTITY(1,1) PRIMARY KEY,
	tax_invoice_id INT NOT NULL,
	created_at DATETIME DEFAULT GETDATE(),
	user_id INT NOT NULL,
	before_invoice_status VARCHAR(2),
	actual_invoice_status VARCHAR(2),
	description VARCHAR(200),
	FOREIGN KEY (tax_invoice_id) REFERENCES dbo.tax_invoice(id),
	FOREIGN KEY (user_id) REFERENCES usr.users(id)
)

CREATE TABLE dbo.tax_invoice_item (
	id INT IDENTITY(1,1) PRIMARY KEY,
	tax_invoice_id INT NOT NULL,
	product_id INT NOT NULL,
	product_name VARCHAR(100) NOT NULL,
	product_price DECIMAL(10, 2) NOT NULL,
	quantity INT NOT NULL,
	FOREIGN KEY (tax_invoice_id) REFERENCES dbo.tax_invoice(id),
	FOREIGN KEY (product_id) REFERENCES prd.products(id)
);

CREATE TABLE prd.product_image(
	id INT IDENTITY(1,1) PRIMARY KEY,
	url_image VARCHAR(255),
	product_id INT,
	FOREIGN KEY (product_id) REFERENCES prd.products(id)
)


