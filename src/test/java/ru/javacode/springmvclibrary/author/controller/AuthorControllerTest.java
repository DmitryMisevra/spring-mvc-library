package ru.javacode.springmvclibrary.author.controller;

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
import ru.javacode.springmvclibrary.author.dto.AuthorDto;
import ru.javacode.springmvclibrary.author.dto.CreatedAuthorDto;
import ru.javacode.springmvclibrary.author.dto.UpdatedAuthorDto;
import ru.javacode.springmvclibrary.author.service.AuthorService;

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
 * Тестовый класс для AuthorController.
 */
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc для выполнения HTTP-запросов

    @MockBean
    private AuthorService authorService; // Мокируем сервисный слой

    @Autowired
    private ObjectMapper objectMapper; // Для сериализации объектов в JSON

    private AuthorDto sampleAuthor;

    @BeforeEach
    void setUp() {
        sampleAuthor = AuthorDto.builder()
                .authorId(1L)
                .authorName("John")
                .authorSurname("Doe")
                .build();
    }

    /**
     * Тестирует успешное получение автора по ID.
     */
    @Test
    @DisplayName("Должен успешно получить автора по ID")
    void testGetAuthorById_Success() throws Exception {
        // Настраиваем поведение мокированного сервиса
        Mockito.when(authorService.getAuthorById(1L)).thenReturn(sampleAuthor);

        // Выполняем GET-запрос к /api/v1/authors/1
        mockMvc.perform(get("/api/v1/authors/{authorId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId", is(1)))
                .andExpect(jsonPath("$.authorName", is("John")))
                .andExpect(jsonPath("$.authorSurname", is("Doe")));
    }

    /**
     * Тестирует получение автора по недопустимому ID (меньше 1).
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при получении автора с недопустимым ID")
    void testGetAuthorById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/authors/{authorId}", 0L) // Неверный ID
                        .contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тестирует успешное создание нового автора.
     */
    @Test
    @DisplayName("Должен успешно создать нового автора")
    void testCreateAuthor_Success() throws Exception {
        CreatedAuthorDto createdAuthorDto = CreatedAuthorDto.builder()
                .authorName("Jane")
                .authorSurname("Smith")
                .build();

        AuthorDto createdAuthor = AuthorDto.builder()
                .authorId(2L)
                .authorName("Jane")
                .authorSurname("Smith")
                .build();

        Mockito.when(authorService.createAuthor(any(CreatedAuthorDto.class))).thenReturn(createdAuthor);

        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdAuthorDto)))
                .andExpect(status().isCreated()) // Ожидаем статус 201 Created
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId", is(2)))
                .andExpect(jsonPath("$.authorName", is("Jane")))
                .andExpect(jsonPath("$.authorSurname", is("Smith")));
    }

    /**
     * Тестирует создание автора с некорректными входными данными.
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при создании автора с некорректными данными")
    void testCreateAuthor_InvalidInput() throws Exception {
        CreatedAuthorDto invalidAuthorDto = CreatedAuthorDto.builder()
                .authorName("") // Пустое имя
                .authorSurname(null) // Отсутствует фамилия
                .build();

        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAuthorDto)))
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errorType", is("MethodArgumentNotValidException")))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                // Поскольку ErrorHandler берет только первое сообщение об ошибке, ожидаем одно из возможных сообщений
                .andExpect(jsonPath("$.message").value(
                        is("Не указано имя автора") // Предполагаем, что имя проверяется первым
                        // Можно использовать любой(), если порядок не гарантирован
                        // is("Не указано имя автора") или is("Не указано имя автора"))
                ))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    /**
     * Тестирует успешное обновление существующего автора.
     */
    @Test
    @DisplayName("Должен успешно обновить существующего автора")
    void testUpdateAuthor_Success() throws Exception {
        UpdatedAuthorDto updatedAuthorDto = UpdatedAuthorDto.builder()
                .authorName("Johnny")
                .authorSurname("Doe")
                .build();

        AuthorDto updatedAuthor = AuthorDto.builder()
                .authorId(1L)
                .authorName("Johnny")
                .authorSurname("Doe")
                .build();

        Mockito.when(authorService.updateAuthor(anyLong(), any(UpdatedAuthorDto.class))).thenReturn(updatedAuthor);

        mockMvc.perform(put("/api/v1/authors/{authorId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthorDto)))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authorId", is(1)))
                .andExpect(jsonPath("$.authorName", is("Johnny")))
                .andExpect(jsonPath("$.authorSurname", is("Doe")));
    }

    /**
     * Тестирует обновление автора с недопустимым ID.
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при обновлении автора с недопустимым ID")
    void testUpdateAuthor_InvalidId() throws Exception {
        UpdatedAuthorDto updatedAuthorDto = UpdatedAuthorDto.builder()
                .authorName("Johnny")
                .authorSurname("Doe")
                .build();

        mockMvc.perform(put("/api/v1/authors/{authorId}", 0L) // Неверный ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthorDto)))
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errorType", is("ConstraintViolationException")))
                .andExpect(jsonPath("$.reason", is("Incorrectly made request.")))
                .andExpect(jsonPath("$.message", is("must be greater than or equal to 1")));
    }

    /**
     * Тестирует успешное удаление существующего автора.
     */
    @Test
    @DisplayName("Должен успешно удалить существующего автора")
    void testDeleteAuthor_Success() throws Exception {
        // Нет необходимости настраивать поведение сервиса, так как метод возвращает void

        mockMvc.perform(delete("/api/v1/authors/{authorId}", 1L))
                .andExpect(status().isNoContent()); // Ожидаем статус 204 No Content
    }

    /**
     * Тестирует удаление автора с недопустимым ID.
     */
    @Test
    @DisplayName("Должен вернуть 400 Bad Request при удалении автора с недопустимым ID")
    void testDeleteAuthor_InvalidId() throws Exception {
        mockMvc.perform(delete("/api/v1/authors/{authorId}", 0L)) // Неверный ID
                .andExpect(status().isBadRequest()) // Ожидаем статус 400 Bad Request
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    /**
     * Тестирует успешное получение списка всех авторов.
     */
    @Test
    @DisplayName("Должен успешно получить список всех авторов")
    void testGetAllAuthors_Success() throws Exception {
        AuthorDto secondAuthor = AuthorDto.builder()
                .authorId(2L)
                .authorName("Jane")
                .authorSurname("Smith")
                .build();

        List<AuthorDto> authors = Arrays.asList(sampleAuthor, secondAuthor);

        Mockito.when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].authorId", is(1)))
                .andExpect(jsonPath("$[0].authorName", is("John")))
                .andExpect(jsonPath("$[0].authorSurname", is("Doe")))
                .andExpect(jsonPath("$[1].authorId", is(2)))
                .andExpect(jsonPath("$[1].authorName", is("Jane")))
                .andExpect(jsonPath("$[1].authorSurname", is("Smith")));
    }

    /**
     * Тестирует получение всех авторов, когда список пуст.
     */
    @Test
    @DisplayName("Должен успешно вернуть пустой список авторов")
    void testGetAllAuthors_EmptyList() throws Exception {
        Mockito.when(authorService.getAllAuthors()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}