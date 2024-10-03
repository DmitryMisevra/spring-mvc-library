package ru.javacode.springmvclibrary.book.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.javacode.springmvclibrary.author.model.Author;
import ru.javacode.springmvclibrary.book.dto.BookDto;
import ru.javacode.springmvclibrary.book.dto.CreateBookDto;
import ru.javacode.springmvclibrary.book.dto.UpdateBookDto;
import ru.javacode.springmvclibrary.book.model.Book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестовый класс для проверки корректности маппинга в BookMapper.
 */
class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = new BookMapper();
    }

    /**
     * Тестирует корректный маппинг CreateBookDto в Book.
     */
    @Test
    @DisplayName("Должен корректно маппить CreateBookDto в Book")
    void testCreateBookDtoToBook_Success() {
        // Создаем тестовый CreateBookDto
        CreateBookDto createBookDto = CreateBookDto.builder()
                .bookName("Effective Java")
                .description("A programming book about best practices in Java.")
                .authorId(1L)
                .build();

        // Маппим DTO в модельный объект Book
        Book book = bookMapper.CreateBookDtoToBook(createBookDto);

        // Проверяем результат
        assertThat(book).isNotNull();
        assertThat(book.getBookName()).isEqualTo("Effective Java");
        assertThat(book.getDescription()).isEqualTo("A programming book about best practices in Java.");
        // Предполагается, что маппер не устанавливает author в Book
        assertThat(book.getAuthor()).isNull();
    }

    /**
     * Тестирует поведение маппера при передаче null в CreateBookDtoToBook.
     */
    @Test
    @DisplayName("Должен выбросить NullPointerException при маппинге null CreateBookDto")
    void testCreateBookDtoToBook_NullInput() {
        // Проверяем, что при передаче null выбрасывается NullPointerException
        assertThrows(NullPointerException.class, () -> {
            bookMapper.CreateBookDtoToBook(null);
        }, "Маппинг null CreateBookDto должен выбросить NullPointerException");
    }

    /**
     * Тестирует корректный маппинг Book в BookDto.
     */
    @Test
    @DisplayName("Должен корректно маппить Book в BookDto")
    void testBookToBookDto_Success() {
        // Создаем тестовый Author
        Author author = Author.builder()
                .authorId(1L)
                .authorName("John")
                .authorSurname("Doe")
                .build();

        // Создаем тестовый Book
        Book book = Book.builder()
                .bookId(1L)
                .bookName("Effective Java")
                .description("A programming book about best practices in Java.")
                .author(author)
                .build();

        // Маппим Book в BookDto
        BookDto bookDto = bookMapper.BookToBookDto(book);

        // Проверяем результат
        assertThat(bookDto).isNotNull();
        assertThat(bookDto.getBookId()).isEqualTo(1L);
        assertThat(bookDto.getBookName()).isEqualTo("Effective Java");
        assertThat(bookDto.getDescription()).isEqualTo("A programming book about best practices in Java.");
        assertThat(bookDto.getAuthorId()).isEqualTo(1L);
    }

    /**
     * Тестирует поведение маппера при передаче null в BookToBookDto.
     */
    @Test
    @DisplayName("Должен выбросить NullPointerException при маппинге null Book")
    void testBookToBookDto_NullInput() {
        // Проверяем, что при передаче null выбрасывается NullPointerException
        assertThrows(NullPointerException.class, () -> {
            bookMapper.BookToBookDto(null);
        }, "Маппинг null Book должен выбросить NullPointerException");
    }
}