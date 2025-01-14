-- init.sql

-- 외래키 활성화
PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS deck_card;
DROP TABLE IF EXISTS deck;
--DROP TABLE IF EXISTS card;

-- 덱 테이블 생성
CREATE TABLE IF NOT EXISTS deck (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    win_rate REAL,
    game_count INTEGER,
    last_play TEXT
);

-- 카드 테이블 생성
CREATE TABLE IF NOT EXISTS card (
    name TEXT PRIMARY KEY,
    cost INTEGER,
    power INTEGER
);

-- 덱에 있는 카드 리스트 테이블 생성
CREATE TABLE IF NOT EXISTS deck_card (
    id INTEGER PRIMARY KEY,
    owner TEXT,
    name TEXT,
    FOREIGN KEY (owner) REFERENCES deck(id) ON UPDATE CASCADE,
    FOREIGN KEY (name) REFERENCES card(name) ON UPDATE CASCADE
);

INSERT INTO deck VALUES ('test', 'TEST DECk', 0.0, 0, '2025-01-10 10:41');
--INSERT INTO card VALUES ('Abomination', 5, 9);
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');
--INSERT INTO deck_card(owner, name) VALUES ('test', 'Abomination');