# 🍺 Pub Quiz

A full-stack pub quiz application built with Java and Spring Boot.

**[Play it here → pubquiz-production.up.railway.app](https://pubquiz-production.up.railway.app/)**

---

## About

Pub Quiz fetches trivia questions from the [Open Trivia DB API](https://opentdb.com/), caches them locally in a SQLite database, and serves them through a custom REST API. The question bank grows passively in the background after each quiz is completed.

Users can also submit their own questions, which are fact-checked in real time by Claude (Anthropic) before being saved to the database.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3, Spring JDBC
- **Database:** SQLite
- **External APIs:** Open Trivia DB, Anthropic Claude API
- **Frontend:** HTML, Tailwind CSS, Vanilla JavaScript
- **Deployment:** Railway

## Features

- Filter by one or multiple categories
- Filter by difficulty (Easy, Medium, Hard)
- Choose any number of questions based on what's available
- Live score tracking with progress bar
- Persistent leaderboard
- Question bank grows silently in the background after each quiz
- Admin panel with full CRUD — add, edit, and delete questions
- AI validation via Claude API — user-submitted questions are fact-checked before being saved

## Admin Panel

Available at [/admin.html](https://pubquiz-production.up.railway.app/admin.html)

- View all questions with category, difficulty, and source filters
- Search questions by text
- Add new questions (validated by Claude before saving)
- Edit and delete existing questions
- Stats showing total questions, API-sourced vs user-added

## REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/quiz/start` | Start a quiz (`?category=&difficulty=&amount=`) |
| GET | `/api/quiz/count` | Get available question count for current filters |
| GET | `/api/categories` | Get categories with 5+ questions available |
| GET | `/api/leaderboard` | Get top 20 scores |
| POST | `/api/quiz/score` | Submit a score |
| GET | `/api/admin/questions` | Get all questions |
| POST | `/api/admin/questions` | Add a new question (AI validated) |
| PUT | `/api/admin/questions/{id}` | Update a question |
| DELETE | `/api/admin/questions/{id}` | Delete a question |

## Running Locally

Requires Java 17 and Maven.

```bash
git clone https://github.com/stephenmcglynn/pubquiz
cd pubquiz
mvn spring-boot:run
```

Create a `secrets.properties` file in the project root:

```properties
anthropic.api.key=your_key_here
```

Open [http://localhost:8080](http://localhost:8080)

On first startup the app fetches an initial batch of questions from the Open Trivia DB API — this takes about 20 seconds and only happens once.