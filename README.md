# Finance Tracker API

Spring Boot приложение для учета доходов и расходов.

## Запуск локально

```bash
mvn spring-boot:run
```

## Регистрация и доступ

1. Зарегистрируйте пользователя через `POST /api/users/register`.
2. Используйте созданные `username/password` для Basic Auth на остальных эндпоинтах.
3. Swagger: `http://localhost:8080/swagger-ui.html`.

> Предзагруженный пользователь:
>
> - username: `demo`
> - password: `password`

## Запуск в Docker

```bash
docker compose up --build
```

## Основные эндпоинты

### Users
- `POST /api/users/register`

### Categories (CRUD)
- `POST /api/categories`
- `GET /api/categories`
- `GET /api/categories/{id}`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

### Transactions (CRUD + stats)
- `POST /api/transactions`
- `GET /api/transactions`
- `GET /api/transactions/{id}`
- `PUT /api/transactions/{id}`
- `DELETE /api/transactions/{id}`
- `GET /api/transactions/stats/monthly`
