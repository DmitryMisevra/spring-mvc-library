package ru.javacode.springmvclibrary.author.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.springmvclibrary.author.dto.AuthorDto;
import ru.javacode.springmvclibrary.author.dto.CreatedAuthorDto;
import ru.javacode.springmvclibrary.author.dto.UpdatedAuthorDto;
import ru.javacode.springmvclibrary.author.mapper.AuthorMapper;
import ru.javacode.springmvclibrary.author.model.Author;
import ru.javacode.springmvclibrary.author.repository.AuthorRepository;
import ru.javacode.springmvclibrary.exception.ResourceNotFoundException;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Указывает на использование тестового профиля
@Transactional // Автоматически откатывает транзакции после каждого теста
@DisplayName("AuthorServiceImpl Интеграционные Тесты")
class AuthorServiceImplIntegrationTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private EntityManager entityManager;

    private Author sampleAuthor;

    @BeforeEach
    void setUp() {
        // Инициализируем и сохраняем тестового автора
        sampleAuthor = Author.builder()
                .authorName("John")
                .authorSurname("Doe")
                .build();
        authorRepository.save(sampleAuthor);
    }

    @Nested
    @DisplayName("Тесты для метода getAuthorById")
    class GetAuthorByIdTests {

        @Test
        @DisplayName("Должен успешно получить автора по ID")
        void getAuthorById_Success() {
            // Arrange
            Long authorId = sampleAuthor.getAuthorId();

            // Act
            AuthorDto result = authorService.getAuthorById(authorId);

            // Flush и Clear контекста персистенции, чтобы убедиться, что данные читаются из базы данных
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getAuthorId()).isEqualTo(authorId);
            assertThat(result.getAuthorName()).isEqualTo("John");
            assertThat(result.getAuthorSurname()).isEqualTo("Doe");
        }

        @Test
        @DisplayName("Должен выбросить ResourceNotFoundException при отсутствии автора")
        void getAuthorById_NotFound() {
            // Arrange
            Long nonExistentAuthorId = 999L;

            // Act & Assert
            assertThatThrownBy(() -> authorService.getAuthorById(nonExistentAuthorId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Автор с id " + nonExistentAuthorId + " не найден");
        }
    }

    @Nested
    @DisplayName("Тесты для метода createAuthor")
    class CreateAuthorTests {

        @Test
        @DisplayName("Должен успешно создать автора")
        void createAuthor_Success() {
            // Arrange
            CreatedAuthorDto createdAuthorDto = CreatedAuthorDto.builder()
                    .authorName("Jane")
                    .authorSurname("Smith")
                    .build();

            // Act
            AuthorDto createdAuthor = authorService.createAuthor(createdAuthorDto);

            // Flush и Clear контекста персистенции
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(createdAuthor).isNotNull();
            assertThat(createdAuthor.getAuthorId()).isNotNull();
            assertThat(createdAuthor.getAuthorName()).isEqualTo("Jane");
            assertThat(createdAuthor.getAuthorSurname()).isEqualTo("Smith");

            // Дополнительно, проверяем, что автор действительно сохранен в репозитории
            Author savedAuthor = authorRepository.findById(createdAuthor.getAuthorId()).orElse(null);
            assertThat(savedAuthor).isNotNull();
            assertThat(savedAuthor.getAuthorName()).isEqualTo("Jane");
            assertThat(savedAuthor.getAuthorSurname()).isEqualTo("Smith");
        }
    }

    @Nested
    @DisplayName("Тесты для метода updateAuthor")
    class UpdateAuthorTests {

        @Test
        @DisplayName("Должен успешно обновить автора полностью")
        void updateAuthor_FullUpdate_Success() {
            // Arrange
            Long authorId = sampleAuthor.getAuthorId();
            UpdatedAuthorDto updatedAuthorDto = UpdatedAuthorDto.builder()
                    .authorName("Jonathan")
                    .authorSurname("Doe-Smith")
                    .build();

            // Act
            AuthorDto updatedAuthor = authorService.updateAuthor(authorId, updatedAuthorDto);

            // Flush и Clear контекста персистенции
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(updatedAuthor).isNotNull();
            assertThat(updatedAuthor.getAuthorId()).isEqualTo(authorId);
            assertThat(updatedAuthor.getAuthorName()).isEqualTo("Jonathan");
            assertThat(updatedAuthor.getAuthorSurname()).isEqualTo("Doe-Smith");

            // Дополнительно, проверяем, что изменения сохранены в репозитории
            Author savedAuthor = authorRepository.findById(authorId).orElse(null);
            assertThat(savedAuthor).isNotNull();
            assertThat(savedAuthor.getAuthorName()).isEqualTo("Jonathan");
            assertThat(savedAuthor.getAuthorSurname()).isEqualTo("Doe-Smith");
        }

        @Test
        @DisplayName("Должен успешно обновить только имя автора")
        void updateAuthor_PartialUpdate_NameOnly() {
            // Arrange
            Long authorId = sampleAuthor.getAuthorId();
            UpdatedAuthorDto updatedAuthorDto = UpdatedAuthorDto.builder()
                    .authorName("Johnny")
                    .build();

            // Act
            AuthorDto updatedAuthor = authorService.updateAuthor(authorId, updatedAuthorDto);

            // Flush и Clear контекста персистенции
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(updatedAuthor).isNotNull();
            assertThat(updatedAuthor.getAuthorId()).isEqualTo(authorId);
            assertThat(updatedAuthor.getAuthorName()).isEqualTo("Johnny");
            assertThat(updatedAuthor.getAuthorSurname()).isEqualTo("Doe"); // Осталось прежним

            // Дополнительно, проверяем, что изменения сохранены в репозитории
            Author savedAuthor = authorRepository.findById(authorId).orElse(null);
            assertThat(savedAuthor).isNotNull();
            assertThat(savedAuthor.getAuthorName()).isEqualTo("Johnny");
            assertThat(savedAuthor.getAuthorSurname()).isEqualTo("Doe");
        }

        @Test
        @DisplayName("Должен выбросить ResourceNotFoundException при отсутствии автора")
        void updateAuthor_AuthorNotFound() {
            // Arrange
            Long nonExistentAuthorId = 999L;
            UpdatedAuthorDto updatedAuthorDto = UpdatedAuthorDto.builder()
                    .authorName("NonExistent")
                    .authorSurname("Author")
                    .build();

            // Act & Assert
            assertThatThrownBy(() -> authorService.updateAuthor(nonExistentAuthorId, updatedAuthorDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Автор с id " + nonExistentAuthorId + " не найден");
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAllAuthors")
    class GetAllAuthorsTests {

        @Test
        @DisplayName("Должен успешно получить список всех авторов")
        void getAllAuthors_Success() {
            // Arrange
            // Добавим еще одного автора
            Author anotherAuthor = Author.builder()
                    .authorName("Jane")
                    .authorSurname("Smith")
                    .build();
            authorRepository.save(anotherAuthor);

            // Act
            List<AuthorDto> authors = authorService.getAllAuthors();

            // Flush и Clear контекста персистенции
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(authors).isNotNull();
            assertThat(authors).hasSize(2);

            // Проверяем содержимое списка
            AuthorDto firstAuthor = authors.get(0);
            AuthorDto secondAuthor = authors.get(1);

            // Порядок может быть не гарантирован, поэтому используем assertThat().extracting()
            assertThat(authors)
                    .extracting(AuthorDto::getAuthorName, AuthorDto::getAuthorSurname)
                    .containsExactlyInAnyOrder(
                            tuple("John", "Doe"),
                            tuple("Jane", "Smith")
                    );
        }

        @Test
        @DisplayName("Должен вернуть пустой список, если авторов нет")
        void getAllAuthors_EmptyList() {
            // Arrange
            // Удаляем всех авторов
            authorRepository.deleteAll();

            // Act
            List<AuthorDto> authors = authorService.getAllAuthors();

            // Assert
            assertThat(authors).isNotNull();
            assertThat(authors).isEmpty();
        }
    }
}