DROP TABLE IF EXISTS meals;

CREATE TABLE meals
(
    id           serial PRIMARY KEY,
    created_date DATE NOT NULL,
    created_time TIME NOT NULL,
    description  TEXT NOT NULL,
    calories     INT  NOT NULL,
    email   VARCHAR NOT NULL
);