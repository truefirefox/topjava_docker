package ru.javawebinar.topjava_docker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@EqualsAndHashCode(of = {"id"})
@Table(name = "meals")
public class Meal{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "calories", nullable = false)
    @Max(5000)
    @Min(2)
    int calories;

    @Column(name = "created_date", nullable = false)
    @NotNull
    LocalDate date;

    @Column(name = "created_time", nullable = false)
    @NotNull
    LocalTime time;


    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    String description;
}
