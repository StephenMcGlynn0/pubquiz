# 🍺 Pub Quiz

A full-stack pub quiz application built with Java and Spring Boot.

**[Play it here → pubquiz-production.up.railway.app](https://pubquiz-production.up.railway.app/)**

---

## About

Pub Quiz fetches trivia questions from the [Open Trivia DB API](https://opentdb.com/), caches them locally in a SQLite database, and serves them through a custom REST API. The question bank grows passively in the background after each quiz is completed.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3, Spring JDBC
- **Database:** SQLite
- **External API:** Open Trivia DB
- **Frontend:** HTML, Tailwind CSS, Vanilla JavaScript
- **Deployment:** Railway

## Features

- Filter by one or multiple categories
- Filter by difficulty (Easy, Medium, Hard)
- Choose any number of questions based on what's available
- Live score tracking with progress bar
- Persistent leaderboard stored in SQLite
- Question bank grows silently in the background after each quiz

## REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/quiz/start` | Start a quiz (`?category=&difficulty=&amount=`) |
| GET | `/api/quiz/count` | Get available question count for filters |
| GET | `/api/categories` | Get categories with 5+ questions |
| GET | `/api/leaderboard` | Get top 20 scores |
| POST | `/api/quiz/score` | Submit a score |

## Running Locally

Requires Java 17 and Maven.

```bash
git clone https://github.com/stephenmcglynn/pubquiz
cd pubquiz
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080)

On first startup the app fetches an initial batch of questions from the Open Trivia DB API — this takes about 20 seconds and only happens once.