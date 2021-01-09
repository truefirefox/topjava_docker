package ru.javawebinar.topjava_docker.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava_docker.model.Meal;

@Repository
public interface MealRepository extends PagingAndSortingRepository<Meal, Long> {
}
