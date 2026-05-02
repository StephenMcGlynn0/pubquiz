CREATE TABLE IF NOT EXISTS questions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    external_id TEXT UNIQUE,
    category TEXT,
    difficulty TEXT,
    question TEXT,
    correct_answer TEXT,
    incorrect_answers TEXT,
    fetched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_name TEXT NOT NULL,
    score INTEGER NOT NULL,
    total INTEGER NOT NULL,
    category TEXT,
    difficulty TEXT,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);