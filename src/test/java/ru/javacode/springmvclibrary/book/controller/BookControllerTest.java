package ru.javacode.springmvclibrary.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.javacode.springmvclibrary.book.dto.BookDto;
import ru.javacode.springmvclibrary.book.dto.CreateBookDto;
import ru.javacode.springmvclibrary.book.dto.UpdateBookDto;
import ru.javacode.springmvclibrary.book.service.BookService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестовый класс для BookController.
 */
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc для выполнения HTTP-запросов

    @MockBean
    private BookService bookService; // Мокируем сервисный слой

    @Autowired
    private ObjectMapper objectMapper; // Для сериализации объектов в JSON

    private BookDto sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = BookDto.builder()
                .bookId(1L)
                .bookName("Spring in Action")
                .description("Comprehensive guide to Spring framework.")
                .authorId(1L)
                .build();
    }

    /**
     * Тестирует успешное получение книги по ID.
     */
    @Test
    @DisplayName("Должен успешно получить книгу по ID")
    void testGetBookById_Success() throws Exception {
        // Настраиваем поведение мокированного сервиса
        Mockito.when(bookService.getBookById(1L)).thenReturn(sampleBook);

        // Выполняем GET-запрос к /api/v1/books/1
        mockMvc.perform(get("/api/v1/books/{bookId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId", is(1)))
                .andExpect(jsonPath("$.bookName", is("Spring in Action")))
                .andExpect(jsonPath("$.description", is("Comprehensive guide to Spring framework.")))
                .andExpect(jsonPath("$.authorId", is(1)));
    }

    /**
     * Тестирует получение книги по недопустимому ID (меньше 1).
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при получении книги с недопустимым ID")
    void testGetBookById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/books/{bookId}", 0L) // Неверный ID
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType("application/problem+json"));
    }

    /**
     * Тестирует успешное создание новой книги.
     */
    @Test
    @DisplayName("Должен успешно создать новую книгу")
    void testCreateBook_Success() throws Exception {
        CreateBookDto createBookDto = CreateBookDto.builder()
                .bookName("Java Concurrency in Practice")
                .description("A guide to concurrent programming in Java.")
                .authorId(2L)
                .build();

        BookDto createdBook = BookDto.builder()
                .bookId(2L)
                .bookName("Java Concurrency in Practice")
                .description("A guide to concurrent programming in Java.")
                .authorId(2L)
                .build();

        Mockito.when(bookService.createBook(any(CreateBookDto.class))).thenReturn(createdBook);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookDto)))
                .andExpect(status().isCreated()) // Ожидаем статус 201 Created
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId", is(2)))
                .andExpect(jsonPath("$.bookName", is("Java Concurrency in Practice")))
                .andExpect(jsonPath("$.description", is("A guide to concurrent programming in Java.")))
                .andExpect(jsonPath("$.authorId", is(2)));
    }

    /**
     * Тестирует создание книги с некорректными входными данными.
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при создании книги с некорректными данными")
    void testCreateBook_InvalidInput() throws Exception {
        CreateBookDto invalidCreateBookDto = CreateBookDto.builder()
                .bookName("") // Пустое имя
                .description("A".repeat(201)) // Описание превышает 200 символов
                .authorId(-1L) // Отрицательный ID автора
                .build();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateBookDto)))
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType("application/json"));
    }

    /**
     * Тестирует успешное обновление существующей книги.
     */
    @Test
    @DisplayName("Должен успешно обновить существующую книгу")
    void testUpdateBook_Success() throws Exception {
        UpdateBookDto updateBookDto = UpdateBookDto.builder()
                .bookName("Spring Boot in Action")
                .description("Updated description for Spring Boot.")
                .authorId(3L)
                .build();

        BookDto updatedBook = BookDto.builder()
                .bookId(1L)
                .bookName("Spring Boot in Action")
                .description("Updated description for Spring Boot.")
                .authorId(3L)
                .build();

        Mockito.when(bookService.updateBook(anyLong(), any(UpdateBookDto.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/v1/books/{bookId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDto)))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId", is(1)))
                .andExpect(jsonPath("$.bookName", is("Spring Boot in Action")))
                .andExpect(jsonPath("$.description", is("Updated description for Spring Boot.")))
                .andExpect(jsonPath("$.authorId", is(3)));
    }

    /**
     * Тестирует обновление книги с недопустимым ID.
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при обновлении книги с недопустимым ID")
    void testUpdateBook_InvalidId() throws Exception {
        UpdateBookDto updateBookDto = UpdateBookDto.builder()
                .bookName("Spring Boot in Action")
                .description("Updated description for Spring Boot.")
                .authorId(3L)
                .build();

        mockMvc.perform(put("/api/v1/books/{bookId}", 0L) // Неверный ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDto)))
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType("application/problem+json"));
    }

    /**
     * Тестирует обновление книги с некорректными входными данными.
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при обновлении книги с некорректными данными")
    void testUpdateBook_InvalidInput() throws Exception {
        UpdateBookDto invalidUpdateBookDto = UpdateBookDto.builder()
                .bookName("") // Пустое имя (если применимо)
                .description("A".repeat(201)) // Описание превышает 200 символов
                .authorId(-2L) // Отрицательный ID автора
                .build();

        mockMvc.perform(put("/api/v1/books/{bookId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateBookDto)))
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType("application/problem+json"));
    }

    /**
     * Тестирует успешное удаление существующей книги.
     */
    @Test
    @DisplayName("Должен успешно удалить существующую книгу")
    void testDeleteBook_Success() throws Exception {
        // Нет необходимости настраивать поведение сервиса, так как метод возвращает void

        mockMvc.perform(delete("/api/v1/books/{bookId}", 1L))
                .andExpect(status().isNoContent()); // Ожидаем статус 204 No Content
    }

    /**
     * Тестирует удаление книги с недопустимым ID.
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при удалении книги с недопустимым ID")
    void testDeleteBook_InvalidId() throws Exception {
        mockMvc.perform(delete("/api/v1/books/{bookId}", 0L)) // Неверный ID
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType("application/problem+json"));
    }

    /**
     * Тестирует успешное получение списка всех книг с параметрами.
     */
    @Test
    @DisplayName("Должен успешно получить список всех книг с параметрами")
    void testGetAllBooks_Success() throws Exception {
        BookDto secondBook = BookDto.builder()
                .bookId(2L)
                .bookName("Effective Java")
                .description("Best practices for Java programming.")
                .authorId(2L)
                .build();

        List<BookDto> books = Arrays.asList(sampleBook, secondBook);

        Mockito.when(bookService.getAllBooks(0, 20, new String[]{"bookName", "asc"})).thenReturn(books);

        mockMvc.perform(get("/api/v1/books")
                        .param("offset", "0")
                        .param("limit", "20")
                        .param("sort", "bookName,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].bookId", is(1)))
                .andExpect(jsonPath("$[0].bookName", is("Spring in Action")))
                .andExpect(jsonPath("$[0].description", is("Comprehensive guide to Spring framework.")))
                .andExpect(jsonPath("$[0].authorId", is(1)))
                .andExpect(jsonPath("$[1].bookId", is(2)))
                .andExpect(jsonPath("$[1].bookName", is("Effective Java")))
                .andExpect(jsonPath("$[1].description", is("Best practices for Java programming.")))
                .andExpect(jsonPath("$[1].authorId", is(2)));
    }

    /**
     * Тестирует получение списка всех книг с использованием значений по умолчанию.
     */
    @Test
    @DisplayName("Должен успешно получить список всех книг с использованием значений по умолчанию")
    void testGetAllBooks_DefaultParams_Success() throws Exception {
        BookDto secondBook = BookDto.builder()
                .bookId(2L)
                .bookName("Effective Java")
                .description("Best practices for Java programming.")
                .authorId(2L)
                .build();

        List<BookDto> books = Arrays.asList(sampleBook, secondBook);

        Mockito.when(bookService.getAllBooks(0, 20, new String[]{"bookName", "asc"})).thenReturn(books);

        mockMvc.perform(get("/api/v1/books") // Без указания параметров
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].bookId", is(1)))
                .andExpect(jsonPath("$[0].bookName", is("Spring in Action")))
                .andExpect(jsonPath("$[0].description", is("Comprehensive guide to Spring framework.")))
                .andExpect(jsonPath("$[0].authorId", is(1)))
                .andExpect(jsonPath("$[1].bookId", is(2)))
                .andExpect(jsonPath("$[1].bookName", is("Effective Java")))
                .andExpect(jsonPath("$[1].description", is("Best practices for Java programming.")))
                .andExpect(jsonPath("$[1].authorId", is(2)));
    }

    /**
     * Тестирует получение всех книг, когда список пуст.
     */
    @Test
    @DisplayName("Должен успешно вернуть пустой список книг")
    void testGetAllBooks_EmptyList() throws Exception {
        Mockito.when(bookService.getAllBooks(0, 20, new String[]{"bookName", "asc"})).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Тестирует получение всех книг с недопустимыми параметрами.
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при получении книг с недопустимыми параметрами")
    void testGetAllBooks_InvalidParams() throws Exception {
        mockMvc.perform(get("/api/v1/books")
                        .param("offset", "-1") // Недопустимый offset
                        .param("limit", "-10") // Недопустимый limit
                        .param("sort", "invalidSort")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType("application/problem+json"));
    }
}