package ru.javawebinar.topjava_docker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava_docker.model.Meal;
import ru.javawebinar.topjava_docker.repository.MealRepository;
import ru.javawebinar.topjava_docker.to.MealTo;

import java.time.LocalDate;
import java.util.*;

@Service
public class MealService {
    private static final Sort SORT_BY_DATE_TIME = Sort.by("dateTime").descending();

    private MealRepository mealRepository;

    @Value("${caloriesPerDay}")
    private int caloriesPerDay;

    @Autowired
    public void setMealRepository(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal update(Meal meal) {
        Meal updatedMeal = create(meal);
        checkNotFound(updatedMeal != null, meal.getId());
        return updatedMeal;
    }

    @CacheEvict(value = "meals", allEntries = true)
    public Meal create(Meal meal) {
        return mealRepository.save(meal);
    }

    public Meal get(long id) {
        Optional<Meal> meal = mealRepository.findById(id);
        checkNotFound(meal.isPresent(), id);
        return meal.get();
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void delete(long id) {
        mealRepository.deleteById(id);
    }

    @Cacheable("meals")
    public List<MealTo> getAll() {
        return convert(mealRepository.findAll(SORT_BY_DATE_TIME));
    }

    private void checkNotFound(boolean isExist, long id) {
        if (!isExist) {
            throw new RuntimeException("Not found entity with " + id);
        }
    }

    private List<MealTo> convert(Iterable<Meal> meals) {
        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));

        final List<MealTo> mealsTo = new ArrayList<>();
        meals.forEach(meal -> mealsTo.add(new MealTo(
                meal.getId(),
                meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
                caloriesSumByDate.get(meal.getDate()) > caloriesPerDay)));
        return mealsTo;
    }
}