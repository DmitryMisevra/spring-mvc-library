package ru.javacode.springmvclibrary.book.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.springmvclibrary.author.model.Author;
import ru.javacode.springmvclibrary.author.repository.AuthorRepository;
import ru.javacode.springmvclibrary.book.dto.BookDto;
import ru.javacode.springmvclibrary.book.dto.CreateBookDto;
import ru.javacode.springmvclibrary.book.dto.UpdateBookDto;
import ru.javacode.springmvclibrary.book.mapper.BookMapper;
import ru.javacode.springmvclibrary.book.model.Book;
import ru.javacode.springmvclibrary.book.repository.BookRepository;
import ru.javacode.springmvclibrary.exception.ResourceNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test") // Убедитесь, что используется профиль для тестов
@Transactional // Автоматически откатывает транзакции после каждого теста
class BookServiceImplIntegrationTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private EntityManager entityManager;

    private Author sampleAuthor;
    private Book sampleBook;

    @BeforeEach
    void setUp() {
        // Инициализируем и сохраняем автора
        sampleAuthor = Author.builder()
                .authorName("John")
                .authorSurname("Doe")
                .build();
        authorRepository.save(sampleAuthor);

        // Инициализируем и сохраняем книгу
        sampleBook = Book.builder()
                .bookName("Spring in Action")
                .description("Comprehensive guide to Spring framework.")
                .author(sampleAuthor)
                .build();
        bookRepository.save(sampleBook);
    }

    @Nested
    @DisplayName("Тесты для метода createBook")
    class CreateBookTests {

        @Test
        @DisplayName("Должен успешно создать книгу")
        void createBook_Success() {
            // Arrange
            CreateBookDto createBookDto = CreateBookDto.builder()
                    .bookName("Java Concurrency in Practice")
                    .description("A guide to concurrent programming in Java.")
                    .authorId(sampleAuthor.getAuthorId())
                    .build();

            // Act
            BookDto createdBookDto = bookService.createBook(createBookDto);

            // Flush and clear the persistence context to ensure данные читаются из базы данных
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(createdBookDto).isNotNull();
            assertThat(createdBookDto.getBookId()).isNotNull();
            assertThat(createdBookDto.getBookName()).isEqualTo("Java Concurrency in Practice");
            assertThat(createdBookDto.getDescription()).isEqualTo("A guide to concurrent programming in Java.");
            assertThat(createdBookDto.getAuthorId()).isEqualTo(sampleAuthor.getAuthorId());

            // Дополнительно, проверяем, что книга действительно сохранена в репозитории
            Book savedBook = bookRepository.findById(createdBookDto.getBookId()).orElse(null);
            assertThat(savedBook).isNotNull();
            assertThat(savedBook.getBookName()).isEqualTo("Java Concurrency in Practice");
            assertThat(savedBook.getDescription()).isEqualTo("A guide to concurrent programming in Java.");
            assertThat(savedBook.getAuthor().getAuthorId()).isEqualTo(sampleAuthor.getAuthorId());
        }

        @Test
        @DisplayName("Должен выбросить ResourceNotFoundException при отсутствии автора")
        void createBook_AuthorNotFound() {
            // Arrange
            CreateBookDto createBookDto = CreateBookDto.builder()
                    .bookName("Java Concurrency in Practice")
                    .description("A guide to concurrent programming in Java.")
                    .authorId(999L) // Не существующий ID автора
                    .build();

            // Act & Assert
            assertThatThrownBy(() -> bookService.createBook(createBookDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Автор с id 999 не найден");
        }
    }

    @Nested
    @DisplayName("Тесты для метода updateBook")
    class UpdateBookTests {

        @Test
        @DisplayName("Должен успешно обновить книгу полностью")
        void updateBook_FullUpdate_Success() {
            // Arrange
            Long bookId = sampleBook.getBookId();
            UpdateBookDto updateBookDto = UpdateBookDto.builder()
                    .bookName("Spring Boot in Action")
                    .description("Updated description for Spring Boot.")
                    .authorId(sampleAuthor.getAuthorId())
                    .build();

            // Act
            BookDto updatedBookDto = bookService.updateBook(bookId, updateBookDto);

            // Flush and clear the persistence context
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(updatedBookDto).isNotNull();
            assertThat(updatedBookDto.getBookId()).isEqualTo(bookId);
            assertThat(updatedBookDto.getBookName()).isEqualTo("Spring Boot in Action");
            assertThat(updatedBookDto.getDescription()).isEqualTo("Updated description for Spring Boot.");
            assertThat(updatedBookDto.getAuthorId()).isEqualTo(sampleAuthor.getAuthorId());

            // Дополнительно, проверяем, что изменения сохранены в репозитории
            Book savedBook = bookRepository.findById(bookId).orElse(null);
            assertThat(savedBook).isNotNull();
            assertThat(savedBook.getBookName()).isEqualTo("Spring Boot in Action");
            assertThat(savedBook.getDescription()).isEqualTo("Updated description for Spring Boot.");
            assertThat(savedBook.getAuthor().getAuthorId()).isEqualTo(sampleAuthor.getAuthorId());
        }

        @Test
        @DisplayName("Должен успешно обновить только название книги")
        void updateBook_PartialUpdate_NameOnly() {
            // Arrange
            Long bookId = sampleBook.getBookId();
            UpdateBookDto updateBookDto = UpdateBookDto.builder()
                    .bookName("Spring Boot Essentials")
                    .build();

            // Act
            BookDto updatedBookDto = bookService.updateBook(bookId, updateBookDto);

            // Flush and clear the persistence context
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(updatedBookDto).isNotNull();
            assertThat(updatedBookDto.getBookId()).isEqualTo(bookId);
            assertThat(updatedBookDto.getBookName()).isEqualTo("Spring Boot Essentials");
            assertThat(updatedBookDto.getDescription()).isEqualTo(sampleBook.getDescription());
            assertThat(updatedBookDto.getAuthorId()).isEqualTo(sampleAuthor.getAuthorId());

            // Дополнительно, проверяем, что изменения сохранены в репозитории
            Book savedBook = bookRepository.findById(bookId).orElse(null);
            assertThat(savedBook).isNotNull();
            assertThat(savedBook.getBookName()).isEqualTo("Spring Boot Essentials");
            assertThat(savedBook.getDescription()).isEqualTo(sampleBook.getDescription());
            assertThat(savedBook.getAuthor().getAuthorId()).isEqualTo(sampleAuthor.getAuthorId());
        }

        @Test
        @DisplayName("Должен выбросить ResourceNotFoundException при отсутствии книги")
        void updateBook_BookNotFound() {
            // Arrange
            Long nonExistentBookId = 999L;
            UpdateBookDto updateBookDto = UpdateBookDto.builder()
                    .bookName("Non-Existent Book")
                    .build();

            // Act & Assert
            assertThatThrownBy(() -> bookService.updateBook(nonExistentBookId, updateBookDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Книга с id " + nonExistentBookId + " не найдена");
        }

        @Test
        @DisplayName("Должен выбросить ResourceNotFoundException при отсутствии автора при обновлении")
        void updateBook_AuthorNotFound() {
            // Arrange
            Long bookId = sampleBook.getBookId();
            Long nonExistentAuthorId = 999L;
            UpdateBookDto updateBookDto = UpdateBookDto.builder()
                    .authorId(nonExistentAuthorId)
                    .build();

            // Act & Assert
            assertThatThrownBy(() -> bookService.updateBook(bookId, updateBookDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Автор с id " + nonExistentAuthorId + " не найден");
        }
    }

    @Nested
    @DisplayName("Тесты для метода getAllBooks")
    class GetAllBooksTests {

        @Test
        @DisplayName("Должен успешно получить список всех книг с заданными параметрами")
        void getAllBooks_Success() {
            // Arrange
            // Добавим еще одну книгу
            Author author2 = Author.builder()
                    .authorName("Jane")
                    .authorSurname("Smith")
                    .build();
            authorRepository.save(author2);

            Book book2 = Book.builder()
                    .bookName("Effective Java")
                    .description("Best practices for Java programming.")
                    .author(author2)
                    .build();
            bookRepository.save(book2);

            Integer offset = 0;
            Integer limit = 10;
            String[] sort = {"bookName", "asc"};

            // Act
            List<BookDto> bookDtos = bookService.getAllBooks(offset, limit, sort);

            // Assert
            assertThat(bookDtos).isNotNull();
            assertThat(bookDtos).hasSize(2);

            // Проверяем содержимое списка
            BookDto firstBookDto = bookDtos.get(0);
            BookDto secondBookDto = bookDtos.get(1);

            assertThat(firstBookDto.getBookName()).isEqualTo("Effective Java");
            assertThat(firstBookDto.getDescription()).isEqualTo("Best practices for Java programming.");
            assertThat(firstBookDto.getAuthorId()).isEqualTo(author2.getAuthorId());

            assertThat(secondBookDto.getBookName()).isEqualTo("Spring in Action");
            assertThat(secondBookDto.getDescription()).isEqualTo("Comprehensive guide to Spring framework.");
            assertThat(secondBookDto.getAuthorId()).isEqualTo(sampleAuthor.getAuthorId());
        }

        @Test
        @DisplayName("Должен вернуть пустой список, если книг нет")
        void getAllBooks_EmptyList() {
            // Arrange
            // Удаляем все книги
            bookRepository.deleteAll();

            // Act
            List<BookDto> bookDtos = bookService.getAllBooks(0, 10, new String[]{"bookName", "asc"});

            // Assert
            assertThat(bookDtos).isNotNull();
            assertThat(bookDtos).isEmpty();
        }

        @Test
        @DisplayName("Должен корректно обрабатывать параметры сортировки")
        void getAllBooks_SortDescending() {
            // Arrange
            // Добавим еще две книги
            Author author2 = Author.builder()
                    .authorName("Jane")
                    .authorSurname("Smith")
                    .build();
            authorRepository.save(author2);

            Book book2 = Book.builder()
                    .bookName("Effective Java")
                    .description("Best practices for Java programming.")
                    .author(author2)
                    .build();
            bookRepository.save(book2);

            Book book3 = Book.builder()
                    .bookName("Clean Code")
                    .description("A Handbook of Agile Software Craftsmanship.")
                    .author(sampleAuthor)
                    .build();
            bookRepository.save(book3);

            Integer offset = 0;
            Integer limit = 10;
            String[] sort = {"bookName", "desc"};

            // Act
            List<BookDto> bookDtos = bookService.getAllBooks(offset, limit, sort);

            // Assert
            assertThat(bookDtos).isNotNull();
            assertThat(bookDtos).hasSize(3);

            // Проверяем порядок сортировки по убыванию
            assertThat(bookDtos.get(0).getBookName()).isEqualTo("Spring in Action");
            assertThat(bookDtos.get(1).getBookName()).isEqualTo("Effective Java");
            assertThat(bookDtos.get(2).getBookName()).isEqualTo("Clean Code");
        }
    }

    @Nested
    @DisplayName("Тесты для метода deleteBook")
    class DeleteBookTests {

        @Test
        @DisplayName("Должен успешно удалить книгу по ID")
        void deleteBook_Success() {
            // Arrange
            Long bookId = sampleBook.getBookId();

            // Act
            bookService.deleteBook(bookId);

            // Flush and clear the persistence context
            entityManager.flush();
            entityManager.clear();

            // Assert
            assertThat(bookRepository.findById(bookId)).isEmpty();
        }
    }
}