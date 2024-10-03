package ru.javacode.springmvclibrary.author.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UpdatedAuthorDtoTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Сериализация UpdatedAuthorDto в JSON")
    void serializeUpdatedAuthorDto() throws JsonProcessingException {
        UpdatedAuthorDto updatedAuthor = UpdatedAuthorDto.builder()
                .authorName("Sergey")
                .authorSurname("Sergeev")
                .build();

        String json = objectMapper.writeValueAsString(updatedAuthor);
        String expectedJson = "{\"authorName\":\"Sergey\",\"authorSurname\":\"Sergeev\"}";

        assertEquals(expectedJson, json, "Сериализованный JSON должен соответствовать ожидаемому");
    }

    @Test
    @DisplayName("Десериализация JSON в UpdatedAuthorDto")
    void deserializeUpdatedAuthorDto() throws JsonProcessingException {
        String json = "{\"authorName\":\"Sergey\",\"authorSurname\":\"Sergeev\"}";
        UpdatedAuthorDto updatedAuthor = objectMapper.readValue(json, UpdatedAuthorDto.class);

        assertNotNull(updatedAuthor, "Десериализованный объект не должен быть null");
        assertEquals("Sergey", updatedAuthor.getAuthorName(), "authorName должен быть 'Sergey'");
        assertEquals("Sergeev", updatedAuthor.getAuthorSurname(), "authorSurname должен быть 'Sergeev'");
    }

    @Test
    @DisplayName("Десериализация JSON с отсутствующим authorSurname в UpdatedAuthorDto")
    void deserializeUpdatedAuthorDto_MissingSurname() throws JsonProcessingException {
        String json = "{\"authorName\":\"Sergey\"}";
        UpdatedAuthorDto updatedAuthor = objectMapper.readValue(json, UpdatedAuthorDto.class);

        assertNotNull(updatedAuthor, "Десериализованный объект не должен быть null");
        assertEquals("Sergey", updatedAuthor.getAuthorName(), "authorName должен быть 'Sergey'");
        assertNull(updatedAuthor.getAuthorSurname(), "authorSurname должен быть null");
    }
}
