package ru.javawebinar.topjava_docker.to;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MealTo {
    private final long id;
    private final LocalDate date;
    private final LocalTime time;
    private final String description;
    private final int calories;
    private final boolean excess;
}

