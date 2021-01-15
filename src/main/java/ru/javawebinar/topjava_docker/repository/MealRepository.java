package ru.javawebinar.topjava_docker.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava_docker.model.Meal;

import java.time.LocalDate;

@Repository
public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {

    @Query("SELECT m from Meal m WHERE " +
            "(coalesce(:startDate, null) IS NULL OR m.date >= :startDate) AND " +
            "(coalesce(:endDate, null) IS NULL OR m.date <= :endDate)")
    Iterable<Meal> getBetweenHalfOpen(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Sort sort);

}
