package ru.javawebinar.topjava_docker.to;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MealTo {
    private final long id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean excess;
}

