package ru.javawebinar.topjava_docker.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava_docker.model.Meal;

import java.time.LocalDate;

@Repository
public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT m from Meal m WHERE " +
            "(coalesce(:startDate, null) IS NULL OR m.date >= :startDate) AND " +
            "(coalesce(:endDate, null) IS NULL OR m.date <= :endDate) AND m.email = :email")
    Iterable<Meal> getBetweenHalfOpen(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, String email, Sort sort);

    @Transactional(readOnly = true)
    Iterable<Meal> findAllByEmail(String email, Sort sort);

    @Transactional(readOnly = true)
    Meal findByIdAndEmail(long id, String email);

    @Transactional
    int deleteByIdAndEmail(long id, String email);
}
