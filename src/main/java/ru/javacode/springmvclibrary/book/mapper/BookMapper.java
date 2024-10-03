package ru.javacode.springmvclibrary.book.mapper;

import org.springframework.stereotype.Component;
import ru.javacode.springmvclibrary.book.dto.BookDto;
import ru.javacode.springmvclibrary.book.dto.CreateBookDto;
import ru.javacode.springmvclibrary.book.dto.UpdateBookDto;
import ru.javacode.springmvclibrary.book.model.Book;

@Component
public class BookMapper {

    public Book CreateBookDtoToBook(CreateBookDto createBookDto) {
        return Book.builder()
                .bookName(createBookDto.getBookName())
                .description(createBookDto.getDescription())
                .build();
    }

    public BookDto BookToBookDto(Book book) {
        return BookDto.builder()
                .bookId(book.getBookId())
                .bookName(book.getBookName())
                .description(book.getDescription())
                .authorId(book.getAuthor().getAuthorId())
                .build();
    }
}
