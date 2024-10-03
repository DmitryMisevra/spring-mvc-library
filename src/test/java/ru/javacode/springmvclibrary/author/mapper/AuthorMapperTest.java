package ru.javacode.springmvclibrary.author.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.javacode.springmvclibrary.author.dto.AuthorDto;
import ru.javacode.springmvclibrary.author.dto.CreatedAuthorDto;
import ru.javacode.springmvclibrary.author.dto.UpdatedAuthorDto;
import ru.javacode.springmvclibrary.author.model.Author;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестовый класс для проверки корректности маппинга в AuthorMapper.
 */
class AuthorMapperTest {

    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapper();
    }

    /**
     * Тестирует корректный маппинг CreatedAuthorDto в Author.
     */
    @Test
    @DisplayName("Должен корректно маппить CreatedAuthorDto в Author")
    void testCreatedAuthorDtoToAuthor_Success() {
        // Создаем тестовый CreatedAuthorDto
        CreatedAuthorDto createdAuthorDto = CreatedAuthorDto.builder()
                .authorName("Ivan")
                .authorSurname("Ivanov")
                .build();

        // Маппим DTO в модельный объект Author
        Author author = authorMapper.CreatedAuthorDtoToAuthor(createdAuthorDto);

        // Проверяем результат
        assertThat(author).isNotNull();
        assertThat(author.getAuthorName()).isEqualTo("Ivan");
        assertThat(author.getAuthorSurname()).isEqualTo("Ivanov");
        // Предполагается, что маппер не устанавливает authorId при создании
        assertThat(author.getAuthorId()).isNull();
    }

    /**
     * Тестирует поведение маппера при передаче null в CreatedAuthorDtoToAuthor.
     */
    @Test
    @DisplayName("Должен выбросить NullPointerException при маппинге null CreatedAuthorDto")
    void testCreatedAuthorDtoToAuthor_NullInput() {
        // Проверяем, что при передаче null выбрасывается NullPointerException
        assertThrows(NullPointerException.class, () -> {
            authorMapper.CreatedAuthorDtoToAuthor(null);
        }, "Маппинг null CreatedAuthorDto должен выбросить NullPointerException");
    }

    /**
     * Тестирует корректный маппинг UpdatedAuthorDto в Author.
     */
    @Test
    @DisplayName("Должен корректно маппить UpdatedAuthorDto в Author")
    void testUpdatedAuthorDtoToAuthor_Success() {
        // Создаем тестовый UpdatedAuthorDto
        UpdatedAuthorDto updatedAuthorDto = UpdatedAuthorDto.builder()
                .authorName("Sergey")
                .authorSurname("Sergeev")
                .build();

        // Маппим DTO в модельный объект Author
        Author author = authorMapper.UpdatedAuthorDtoToAuthor(updatedAuthorDto);

        // Проверяем результат
        assertThat(author).isNotNull();
        assertThat(author.getAuthorName()).isEqualTo("Sergey");
        assertThat(author.getAuthorSurname()).isEqualTo("Sergeev");
        // Предполагается, что маппер не устанавливает authorId при обновлении
        assertThat(author.getAuthorId()).isNull();
    }

    /**
     * Тестирует поведение маппера при передаче null в UpdatedAuthorDtoToAuthor.
     */
    @Test
    @DisplayName("Должен выбросить NullPointerException при маппинге null UpdatedAuthorDto")
    void testUpdatedAuthorDtoToAuthor_NullInput() {
        // Проверяем, что при передаче null выбрасывается NullPointerException
        assertThrows(NullPointerException.class, () -> {
            authorMapper.UpdatedAuthorDtoToAuthor(null);
        }, "Маппинг null UpdatedAuthorDto должен выбросить NullPointerException");
    }

    /**
     * Тестирует корректный маппинг Author в AuthorDto.
     */
    @Test
    @DisplayName("Должен корректно маппить Author в AuthorDto")
    void testAuthorToAuthorDto_Success() {
        // Создаем тестовый Author
        Author author = Author.builder()
                .authorId(1L)
                .authorName("Ivan")
                .authorSurname("Ivanov")
                .build();

        // Маппим Author в AuthorDto
        AuthorDto authorDto = authorMapper.AuthorToAuthorDto(author);

        // Проверяем результат
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getAuthorId()).isEqualTo(1L);
        assertThat(authorDto.getAuthorName()).isEqualTo("Ivan");
        assertThat(authorDto.getAuthorSurname()).isEqualTo("Ivanov");
    }

    /**
     * Тестирует поведение маппера при передаче null в AuthorToAuthorDto.
     */
    @Test
    @DisplayName("Должен выбросить NullPointerException при маппинге null Author")
    void testAuthorToAuthorDto_NullInput() {
        // Проверяем, что при передаче null выбрасывается NullPointerException
        assertThrows(NullPointerException.class, () -> {
            authorMapper.AuthorToAuthorDto(null);
        }, "Маппинг null Author должен выбросить NullPointerException");
    }

    /**
     * Тестирует маппинг Author с null полями в AuthorDto.
     */
    @Test
    @DisplayName("Должен корректно маппить Author с null полями в AuthorDto")
    void testAuthorToAuthorDto_NullFields() {
        // Создаем тестовый Author с null полями
        Author author = Author.builder()
                .authorId(null)
                .authorName(null)
                .authorSurname(null)
                .build();

        // Маппим Author в AuthorDto
        AuthorDto authorDto = authorMapper.AuthorToAuthorDto(author);

        // Проверяем результат
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getAuthorId()).isNull();
        assertThat(authorDto.getAuthorName()).isNull();
        assertThat(authorDto.getAuthorSurname()).isNull();
    }
}