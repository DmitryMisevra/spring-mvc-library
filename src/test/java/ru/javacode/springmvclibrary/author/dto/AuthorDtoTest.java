package ru.javacode.springmvclibrary.author.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthorDtoTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("Тесты для AuthorDto")
    class AuthorDtoTests {

        @Test
        @DisplayName("Сериализация AuthorDto в JSON")
        void serializeAuthorDto() throws JsonProcessingException {
            AuthorDto author = AuthorDto.builder()
                    .authorId(1L)
                    .authorName("Ivan")
                    .authorSurname("Ivanov")
                    .build();

            String json = objectMapper.writeValueAsString(author);
            String expectedJson = "{\"authorId\":1,\"authorName\":\"Ivan\",\"authorSurname\":\"Ivanov\"}";

            assertEquals(expectedJson, json, "Сериализованный JSON должен соответствовать ожидаемому");
        }

        @Test
        @DisplayName("Десериализация JSON в AuthorDto")
        void deserializeAuthorDto() throws JsonProcessingException {
            String json = "{\"authorId\":1,\"authorName\":\"Ivan\",\"authorSurname\":\"Ivanov\"}";
            AuthorDto author = objectMapper.readValue(json, AuthorDto.class);

            assertNotNull(author, "Десериализованный объект не должен быть null");
            assertEquals(1L, author.getAuthorId(), "authorId должен быть 1");
            assertEquals("Ivan", author.getAuthorName(), "authorName должен быть 'Ivan'");
            assertEquals("Ivanov", author.getAuthorSurname(), "authorSurname должен быть 'Ivanov'");
        }

        @Test
        @DisplayName("Десериализация JSON без authorId в AuthorDto")
        void deserializeAuthorDto_NoId() throws JsonProcessingException {
            String json = "{\"authorName\":\"Ivan\",\"authorSurname\":\"Ivanov\"}";
            AuthorDto author = objectMapper.readValue(json, AuthorDto.class);

            assertNotNull(author, "Десериализованный объект не должен быть null");
            assertNull(author.getAuthorId(), "authorId должен быть null");
            assertEquals("Ivan", author.getAuthorName(), "authorName должен быть 'Ivan'");
            assertEquals("Ivanov", author.getAuthorSurname(), "authorSurname должен быть 'Ivanov'");
        }

    }
}