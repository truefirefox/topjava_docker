DROP TABLE IF EXISTS meals;

CREATE TABLE meals
(
    id          serial PRIMARY KEY,
    date_time   TIMESTAMP NOT NULL,
    description TEXT      NOT NULL,
    calories    INT       NOT NULL
);