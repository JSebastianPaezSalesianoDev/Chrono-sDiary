package com.accesodatos.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@ToString(exclude = "books")
@EqualsAndHashCode(exclude = "books")
@Entity
@Table(name = "writers")
public class Writer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE},
    		mappedBy = "writers")
    @JsonManagedReference
    private Set<Book> books = new LinkedHashSet<>();

    @Column(nullable = false)
    private String name;

    public void addBook(Book book) {
        this.books.add(book);
        book.getWriters().add(this);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
        book.getWriters().remove(this);
    }
}
