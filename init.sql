-- init.sql

-- 외래키 활성화
PRAGMA foreign_keys = ON;

-- 첫 번째 테이블 생성 (users 테이블)
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER
);

-- 두 번째 테이블 생성 (orders 테이블)
CREATE TABLE IF NOT EXISTS orders (
    order_id INTEGER PRIMARY KEY,
    user_id INTEGER,
    order_date TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

