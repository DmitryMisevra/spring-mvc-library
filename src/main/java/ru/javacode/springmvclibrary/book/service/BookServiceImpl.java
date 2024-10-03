package ru.javacode.springmvclibrary.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Автор с " +
                "id " + bookId + " не найден"));
        return bookMapper.BookToBookDto(book);
    }

    @Override
    public BookDto createBook(CreateBookDto createBookDto) {
        Long authorId = createBookDto.getAuthorId();
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException(
                "Автор с id " + authorId + " не найден"));
        Book book = bookMapper.CreateBookDtoToBook(createBookDto);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return bookMapper.BookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(Long bookId, UpdateBookDto updateBookDto) {
        Book bookToUpdate = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException(
                "Книга с id " + bookId + " не найдена"));
        if (updateBookDto.getBookName() != null) {
            bookToUpdate.setBookName(updateBookDto.getBookName());
        }
        if (updateBookDto.getDescription() != null) {
            bookToUpdate.setDescription(updateBookDto.getDescription());
        }
        Long authorId = updateBookDto.getAuthorId();
        if (authorId != null) {
            Author author = authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException(
                    "Автор с id " + authorId + " не найден"));
            bookToUpdate.setAuthor(author);
        }
        Book savedBook = bookRepository.save(bookToUpdate);
        return bookMapper.BookToBookDto(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks(Integer offset, Integer limit, String[] sort) {
        Sort.Direction direction = Sort.Direction.ASC;
        String sortBy = "id";

        if (sort.length == 2) {
            direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortBy = sort[0];
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(direction, sortBy));
        List<Book> books = bookRepository.findAll(pageable).getContent();
        return books.stream()
                .map(bookMapper::BookToBookDto)
                .toList();
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}
