-- Drop existing tables if they exist
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS inventory CASCADE;

-- Create inventory table
CREATE TABLE inventory (
    product_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL
);

-- Create orders table
CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed inventory data
INSERT INTO inventory (product_id, name, stock) VALUES
    ('P100', 'Wireless Mouse', 25),
    ('P200', 'Mechanical Keyboard', 10),
    ('P300', 'USB-C Hub', 0);
