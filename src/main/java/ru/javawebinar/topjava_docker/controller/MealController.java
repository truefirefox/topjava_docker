package ru.javawebinar.topjava_docker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava_docker.TopjavaDockerApplication;
import ru.javawebinar.topjava_docker.exception.NotFoundException;
import ru.javawebinar.topjava_docker.model.Meal;
import ru.javawebinar.topjava_docker.service.MealService;
import ru.javawebinar.topjava_docker.to.MealTo;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealController {
    private static final Logger log = LoggerFactory.getLogger(TopjavaDockerApplication.class);
    private MealService mealService;

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("/add")
    public Meal create(@Valid @RequestBody Meal meal) {
        if (meal.getId() == 0) {
            log.info("create", meal);
            return mealService.create(meal);
        } else {
            log.info("update {}", meal);
            return mealService.update(meal);
        }
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable long id) {
        log.info("get meal {}", id);
        return mealService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("delete meal {}", id);
        mealService.delete(id);
    }

    @GetMapping
    public List<MealTo> getAll() {
        log.info("getAll");
        return mealService.getAll();
    }

    @ExceptionHandler({NotFoundException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleException() {
        return new ResponseEntity("meal not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        String result = ex.getBindingResult().getFieldErrors().stream()
                .map(msg -> msg.getField() + " " + msg.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("\n"));
        return new ResponseEntity(result, HttpStatus.NOT_ACCEPTABLE);
    }
}
