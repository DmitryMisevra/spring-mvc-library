package ru.javacode.springmvclibrary.book.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateBookDtoTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Сериализация UpdateBookDto в JSON")
    void serializeUpdateBookDto() throws JsonProcessingException {
        UpdateBookDto updateBookDto = UpdateBookDto.builder()
                .bookName("Spring Boot in Action")
                .description("Updated description for Spring Boot.")
                .authorId(3L)
                .build();

        String json = objectMapper.writeValueAsString(updateBookDto);
        String expectedJson = "{\"bookName\":\"Spring Boot in Action\",\"description\":\"Updated description for Spring Boot.\",\"authorId\":3}";

        assertEquals(expectedJson, json, "Сериализованный JSON должен соответствовать ожидаемому");
    }

    @Test
    @DisplayName("Десериализация JSON в UpdateBookDto")
    void deserializeUpdateBookDto() throws JsonProcessingException {
        String json = "{\"bookName\":\"Spring Boot in Action\",\"description\":\"Updated description for Spring Boot.\",\"authorId\":3}";
        UpdateBookDto updateBookDto = objectMapper.readValue(json, UpdateBookDto.class);

        assertNotNull(updateBookDto, "Десериализованный объект не должен быть null");
        assertEquals("Spring Boot in Action", updateBookDto.getBookName(), "bookName должен быть 'Spring Boot in Action'");
        assertEquals("Updated description for Spring Boot.", updateBookDto.getDescription(), "description должен быть 'Updated description for Spring Boot.'");
        assertEquals(3L, updateBookDto.getAuthorId(), "authorId должен быть 3");
    }

    @Test
    @DisplayName("Десериализация JSON с отсутствующим description в UpdateBookDto")
    void deserializeUpdateBookDto_MissingDescription() throws JsonProcessingException {
        String json = "{\"bookName\":\"Spring Boot in Action\",\"authorId\":3}";
        UpdateBookDto updateBookDto = objectMapper.readValue(json, UpdateBookDto.class);

        assertNotNull(updateBookDto, "Десериализованный объект не должен быть null");
        assertEquals("Spring Boot in Action", updateBookDto.getBookName(), "bookName должен быть 'Spring Boot in Action'");
        assertNull(updateBookDto.getDescription(), "description должен быть null");
        assertEquals(3L, updateBookDto.getAuthorId(), "authorId должен быть 3");
    }

    @Test
    @DisplayName("Десериализация JSON с отсутствующим authorId в UpdateBookDto")
    void deserializeUpdateBookDto_MissingAuthorId() throws JsonProcessingException {
        String json = "{\"bookName\":\"Spring Boot in Action\",\"description\":\"Updated description for Spring Boot.\"}";
        UpdateBookDto updateBookDto = objectMapper.readValue(json, UpdateBookDto.class);

        assertNotNull(updateBookDto, "Десериализованный объект не должен быть null");
        assertEquals("Spring Boot in Action", updateBookDto.getBookName(), "bookName должен быть 'Spring Boot in Action'");
        assertEquals("Updated description for Spring Boot.", updateBookDto.getDescription(), "description должен быть 'Updated description for Spring Boot.'");
        assertNull(updateBookDto.getAuthorId(), "authorId должен быть null");
    }
}