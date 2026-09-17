-- Drop existing tables (clean recreate)
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS inventory;

-- Inventory table
CREATE TABLE inventory (
    product_id  VARCHAR(20) PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    stock       INTEGER NOT NULL DEFAULT 0
);

-- Orders table (one order can have many items now)
CREATE TABLE orders (
    order_id    SERIAL PRIMARY KEY,
    status      VARCHAR(20) NOT NULL CHECK (status IN ('CONFIRMED', 'REJECTED', 'CANCELLED')),
    reason      VARCHAR(255),
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Order items (line items per order)
CREATE TABLE order_items (
    item_id     SERIAL PRIMARY KEY,
    order_id    INTEGER NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    product_id  VARCHAR(20) NOT NULL REFERENCES inventory(product_id),
    quantity    INTEGER NOT NULL
);

-- Notifications (activity feed)
CREATE TABLE notifications (
    notification_id SERIAL PRIMARY KEY,
    message          VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Seed data
INSERT INTO inventory (product_id, name, stock) VALUES
('P100', 'Wireless Mouse', 25),
('P200', 'Mechanical Keyboard', 10),
('P300', 'USB-C Hub', 0);