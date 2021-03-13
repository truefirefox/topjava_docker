package ru.javawebinar.topjava_docker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava_docker.exception.NotFoundException;
import ru.javawebinar.topjava_docker.model.Meal;
import ru.javawebinar.topjava_docker.repository.MealRepository;
import ru.javawebinar.topjava_docker.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;

@Service
public class MealService {
    private static final Sort SORT_BY_DATE_TIME = Sort.by("date").descending().and(Sort.by("time"));

    private MealRepository mealRepository;

    @Value("${caloriesPerDay}")
    private int caloriesPerDay;

    @Autowired
    public void setMealRepository(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Transactional
    public Meal update(Meal meal, String email) {
        checkNotFound(get(meal.getId(), email) != null, meal.getId());
        return create(meal, email);
    }

    @CacheEvict(value = "meals", allEntries = true)
    public Meal create(Meal meal, String email) {
        meal.setEmail(email);
        return mealRepository.save(meal);
    }

    public Meal get(long id, String email) {
        Meal meal = mealRepository.findByIdAndEmail(id, email);
        checkNotFound(meal != null, id);
        return meal;
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void delete(long id, String email) {
        checkNotFound(mealRepository.deleteByIdAndEmail(id, email) > 0, id);
    }

    @Cacheable("meals")
    public List<MealTo> getAll(String email) {
        Iterable<Meal> meals = mealRepository.findAllByEmail(email, SORT_BY_DATE_TIME);
        return convert(meals, meal -> true);
    }

    public List<MealTo> getBetween(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String email) {
        Iterable<Meal> meals = mealRepository.getBetweenHalfOpen(startDate, endDate, email, SORT_BY_DATE_TIME);
        Predicate<Meal> mealPredicate = meal ->
                (startTime == null || meal.getTime().compareTo(startTime) >= 0) && (endTime == null || meal.getTime().compareTo(endTime) < 0);
        return convert(meals, mealPredicate);
    }

    private void checkNotFound(boolean isExist, long id) {
        if (!isExist) {
            throw new NotFoundException("meal not found, id = " + id);
        }
    }

    private List<MealTo> convert(Iterable<Meal> meals, Predicate<Meal> predicate) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));

        final List<MealTo> mealsTo = new ArrayList<>();
        meals.forEach(meal -> {
            if (predicate.test(meal)) {
                mealsTo.add(new MealTo(
                        meal.getId(),
                        meal.getDate(),
                        meal.getTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
            }
        });
        return mealsTo;
    }
}
