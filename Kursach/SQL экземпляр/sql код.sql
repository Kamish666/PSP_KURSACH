CREATE TABLE country (
    country_id INT AUTO_INCREMENT PRIMARY KEY,
    country_name VARCHAR(40) UNIQUE
);

CREATE TABLE manufacturer (
    manufacturer_id INT AUTO_INCREMENT PRIMARY KEY,
    manufacturer_name VARCHAR(255) UNIQUE,
    country_id INT,
    FOREIGN KEY (country_id) REFERENCES country(country_id)
);

CREATE TABLE product_category (
    product_category_id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(100) UNIQUE,
    definition VARCHAR(255)
);

CREATE TABLE provider (
    provider_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    email VARCHAR(70)
);

CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(11, 2),
    category_id INT,
    manufacturer_id INT,
    provider_id INT,
    FOREIGN KEY (category_id) REFERENCES product_category(product_category_id),
    FOREIGN KEY (manufacturer_id) REFERENCES manufacturer(manufacturer_id),
    FOREIGN KEY (provider_id) REFERENCES provider(provider_id)
);

CREATE TABLE client (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    email VARCHAR(70)
);

CREATE TABLE orders (
    orders_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    client_id INT,
    date DATETIME,
    amount INT,
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (client_id) REFERENCES client(client_id)
);


CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    login VARCHAR(45),
    password VARCHAR(45),
    role INT
);

