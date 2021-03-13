package ru.javawebinar.topjava_docker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava_docker.TopjavaDockerApplication;
import ru.javawebinar.topjava_docker.exception.NotFoundException;
import ru.javawebinar.topjava_docker.model.Meal;
import ru.javawebinar.topjava_docker.service.MealService;
import ru.javawebinar.topjava_docker.to.MealTo;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/meals", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
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
            log.info("create {}", meal);
            return mealService.create(meal, getUserEmail());
        } else {
            log.info("update {}", meal);
            return mealService.update(meal, getUserEmail());
        }
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable long id) {
        log.info("get meal {}", id);
        return mealService.get(id, getUserEmail());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("delete meal {}", id);
        mealService.delete(id, getUserEmail());
    }

    @GetMapping
    public List<MealTo> getAll() {
        log.info("getAll");
        return mealService.getAll(getUserEmail());
    }

    @GetMapping("/between")
    public List<MealTo> getBetween(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   @RequestParam @Nullable @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                   @RequestParam @Nullable @DateTimeFormat(pattern = "HH:mm") LocalTime endTime) {
        log.info("getAllBetween");
        return mealService.getBetween(startDate, endDate, startTime, endTime, getUserEmail());
    }

    @ExceptionHandler({NotFoundException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        String result = ex.getBindingResult().getFieldErrors().stream()
                .map(msg -> msg.getField() + " " + msg.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
    }

    public String getUserEmail() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null)
            throw new AuthenticationCredentialsNotFoundException("user not found");
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User) {
            String emailBase64 = ((OAuth2User) principal).getAttribute("sub");
            return new String(Base64.decodeBase64(emailBase64));
        }
        throw new NotFoundException("user not found");
    }
}
