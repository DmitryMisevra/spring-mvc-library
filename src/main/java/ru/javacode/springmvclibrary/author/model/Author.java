package ru.javacode.springmvclibrary.author.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.javacode.springmvclibrary.book.model.Book;

import java.util.List;

@Entity
@Table(name = "authors")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false)
    private Long authorId;
    @Column(name = "author_name", nullable = false)
    private String authorName;
    @Column(name = "author_surname", nullable = false)
    private String authorSurname;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    List<Book> books;
}
