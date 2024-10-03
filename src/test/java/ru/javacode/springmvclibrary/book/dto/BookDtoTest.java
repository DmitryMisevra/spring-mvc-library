package ru.javacode.springmvclibrary.book.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookDtoTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Сериализация BookDto в JSON")
    void serializeBookDto() throws JsonProcessingException {
        BookDto book = BookDto.builder()
                .bookId(1L)
                .bookName("Spring in Action")
                .description("Comprehensive guide to Spring framework.")
                .authorId(1L)
                .build();

        String json = objectMapper.writeValueAsString(book);
        String expectedJson = "{\"bookId\":1,\"bookName\":\"Spring in Action\",\"description\":\"Comprehensive guide to Spring framework.\",\"authorId\":1}";

        assertEquals(expectedJson, json, "Сериализованный JSON должен соответствовать ожидаемому");
    }

    @Test
    @DisplayName("Десериализация JSON в BookDto")
    void deserializeBookDto() throws JsonProcessingException {
        String json = "{\"bookId\":1,\"bookName\":\"Spring in Action\",\"description\":\"Comprehensive guide to Spring framework.\",\"authorId\":1}";
        BookDto book = objectMapper.readValue(json, BookDto.class);

        assertNotNull(book, "Десериализованный объект не должен быть null");
        assertEquals(1L, book.getBookId(), "bookId должен быть 1");
        assertEquals("Spring in Action", book.getBookName(), "bookName должен быть 'Spring in Action'");
        assertEquals("Comprehensive guide to Spring framework.", book.getDescription(), "description должен быть 'Comprehensive guide to Spring framework.'");
        assertEquals(1L, book.getAuthorId(), "authorId должен быть 1");
    }

    @Test
    @DisplayName("Десериализация JSON без bookId в BookDto")
    void deserializeBookDto_NoId() throws JsonProcessingException {
        String json = "{\"bookName\":\"Spring in Action\",\"description\":\"Comprehensive guide to Spring framework.\",\"authorId\":1}";
        BookDto book = objectMapper.readValue(json, BookDto.class);

        assertNotNull(book, "Десериализованный объект не должен быть null");
        assertNull(book.getBookId(), "bookId должен быть null");
        assertEquals("Spring in Action", book.getBookName(), "bookName должен быть 'Spring in Action'");
        assertEquals("Comprehensive guide to Spring framework.", book.getDescription(), "description должен быть 'Comprehensive guide to Spring framework.'");
        assertEquals(1L, book.getAuthorId(), "authorId должен быть 1");
    }

}