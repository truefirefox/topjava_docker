package ru.javawebinar.topjava_docker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MealControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldUpdate() throws Exception {
        String mealToUpdate = Files.readString(Path.of("src/test/resources/update.json"));
        this.mockMvc.perform(
                post("/meals/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mealToUpdate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mealToUpdate));
    }

    @Test
    public void shouldSave() throws Exception {
        String mealToSave = Files.readString(Path.of("src/test/resources/save.json"));
        String result = Files.readString(Path.of("src/test/resources/saveResult.json"));
        this.mockMvc.perform(
                post("/meals/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mealToSave))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }


    @Test
    public void shouldGet() throws Exception {
        String result = Files.readString(Path.of("src/test/resources/get.json"));
        this.mockMvc.perform(get("/meals/7"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    public void shouldGetAll() throws Exception {
        String result = Files.readString(Path.of("src/test/resources/getAll.json"));
        this.mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    public void shouldGetAllFiltered() throws Exception {
        String result = Files.readString(Path.of("src/test/resources/getAllBetween.json"));
        this.mockMvc.perform(get("/meals/between")
                .param("startDate", "2020-01-30")
                .param("startTime", "19:00")
                .param("endDate", "2020-01-31")
                .param("endTime", "22:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    public void shouldGetAllFilteredWithNullTime() throws Exception {
        String result = Files.readString(Path.of("src/test/resources/getAll.json"));
        this.mockMvc.perform(get("/meals/between")
                .param("startDate", "2020-01-30")
                .param("endTime", "22:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    public void shouldDelete() throws Exception {
        this.mockMvc.perform(delete("/meals/7"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn404() throws Exception {
        this.mockMvc.perform(delete("/meals/77"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("meal not found"));
    }

    @Test
    public void shouldReturnInvalidMsg() throws Exception {
        String mealToUpdate = Files.readString(Path.of("src/test/resources/invalid.json"));
        String result = "calories must be greater than or equal to 2\n" +
                "description must not be blank\n" +
                "description size must be between 2 and 120";
        this.mockMvc.perform(
                post("/meals/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mealToUpdate))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string(result));
    }
}
