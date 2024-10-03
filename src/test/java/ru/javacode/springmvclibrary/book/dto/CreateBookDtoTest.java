package ru.javacode.springmvclibrary.book.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateBookDtoTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Сериализация CreateBookDto в JSON")
    void serializeCreateBookDto() throws JsonProcessingException {
        CreateBookDto createBookDto = CreateBookDto.builder()
                .bookName("Java Concurrency in Practice")
                .description("A guide to concurrent programming in Java.")
                .authorId(2L)
                .build();

        String json = objectMapper.writeValueAsString(createBookDto);
        String expectedJson = "{\"bookName\":\"Java Concurrency in Practice\",\"description\":\"A guide to concurrent programming in Java.\",\"authorId\":2}";

        assertEquals(expectedJson, json, "Сериализованный JSON должен соответствовать ожидаемому");
    }

    @Test
    @DisplayName("Десериализация JSON в CreateBookDto")
    void deserializeCreateBookDto() throws JsonProcessingException {
        String json = "{\"bookName\":\"Java Concurrency in Practice\",\"description\":\"A guide to concurrent programming in Java.\",\"authorId\":2}";
        CreateBookDto createBookDto = objectMapper.readValue(json, CreateBookDto.class);

        assertNotNull(createBookDto, "Десериализованный объект не должен быть null");
        assertEquals("Java Concurrency in Practice", createBookDto.getBookName(), "bookName должен быть 'Java Concurrency in Practice'");
        assertEquals("A guide to concurrent programming in Java.", createBookDto.getDescription(), "description должен быть 'A guide to concurrent programming in Java.'");
        assertEquals(2L, createBookDto.getAuthorId(), "authorId должен быть 2");
    }

    @Test
    @DisplayName("Десериализация JSON с отсутствующим bookName в CreateBookDto")
    void deserializeCreateBookDto_MissingName() throws JsonProcessingException {
        String json = "{\"description\":\"A guide to concurrent programming in Java.\",\"authorId\":2}";
        CreateBookDto createBookDto = objectMapper.readValue(json, CreateBookDto.class);

        assertNotNull(createBookDto, "Десериализованный объект не должен быть null");
        assertNull(createBookDto.getBookName(), "bookName должен быть null");
        assertEquals("A guide to concurrent programming in Java.", createBookDto.getDescription(), "description должен быть 'A guide to concurrent programming in Java.'");
        assertEquals(2L, createBookDto.getAuthorId(), "authorId должен быть 2");
    }

    @Test
    @DisplayName("Десериализация JSON с отсутствующим authorId в CreateBookDto")
    void deserializeCreateBookDto_MissingAuthorId() throws JsonProcessingException {
        String json = "{\"bookName\":\"Java Concurrency in Practice\",\"description\":\"A guide to concurrent programming in Java.\"}";
        CreateBookDto createBookDto = objectMapper.readValue(json, CreateBookDto.class);

        assertNotNull(createBookDto, "Десериализованный объект не должен быть null");
        assertEquals("Java Concurrency in Practice", createBookDto.getBookName(), "bookName должен быть 'Java Concurrency in Practice'");
        assertEquals("A guide to concurrent programming in Java.", createBookDto.getDescription(), "description должен быть 'A guide to concurrent programming in Java.'");
        assertNull(createBookDto.getAuthorId(), "authorId должен быть null");
    }
}