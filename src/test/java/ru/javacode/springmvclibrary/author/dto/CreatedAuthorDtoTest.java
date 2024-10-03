package ru.javacode.springmvclibrary.author.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatedAuthorDtoTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Сериализация CreatedAuthorDto в JSON")
    void serializeCreatedAuthorDto() throws JsonProcessingException {
        CreatedAuthorDto createdAuthor = CreatedAuthorDto.builder()
                .authorName("Petr")
                .authorSurname("Petrov")
                .build();

        String json = objectMapper.writeValueAsString(createdAuthor);
        String expectedJson = "{\"authorName\":\"Petr\",\"authorSurname\":\"Petrov\"}";

        assertEquals(expectedJson, json, "Сериализованный JSON должен соответствовать ожидаемому");
    }

    @Test
    @DisplayName("Десериализация JSON в CreatedAuthorDto")
    void deserializeCreatedAuthorDto() throws JsonProcessingException {
        String json = "{\"authorName\":\"Petr\",\"authorSurname\":\"Petrov\"}";
        CreatedAuthorDto createdAuthor = objectMapper.readValue(json, CreatedAuthorDto.class);

        assertNotNull(createdAuthor, "Десериализованный объект не должен быть null");
        assertEquals("Petr", createdAuthor.getAuthorName(), "authorName должен быть 'Petr'");
        assertEquals("Petrov", createdAuthor.getAuthorSurname(), "authorSurname должен быть 'Petrov'");
    }

    @Test
    @DisplayName("Десериализация JSON с отсутствующим authorName в CreatedAuthorDto")
    void deserializeCreatedAuthorDto_MissingName() throws JsonProcessingException {
        String json = "{\"authorSurname\":\"Petrov\"}";
        CreatedAuthorDto createdAuthor = objectMapper.readValue(json, CreatedAuthorDto.class);

        assertNotNull(createdAuthor, "Десериализованный объект не должен быть null");
        assertNull(createdAuthor.getAuthorName(), "authorName должен быть null");
        assertEquals("Petrov", createdAuthor.getAuthorSurname(), "authorSurname должен быть 'Petrov'");
    }
}