CREATE TABLE IF NOT EXISTS article (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(1000),
    description VARCHAR(5000),
    content TEXT,
    url VARCHAR(1000),
    image VARCHAR(1000),
    published_at TIMESTAMP,
    lang VARCHAR(10),
    category VARCHAR(50),
    source_id VARCHAR(255),
    source_name VARCHAR(255),
    source_url VARCHAR(1000),
    source_country VARCHAR(10)
);
