package com.accesodatos.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = "writers")
@EqualsAndHashCode(exclude = "writers")
@Entity
@Table(name = "books")
public class Book {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToMany(fetch = FetchType.LAZY,
			cascade = { CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "books_writers", joinColumns  = @JoinColumn(name = "book_id"),
	inverseJoinColumns =@JoinColumn(name = "writer_id"))
	@JsonBackReference
    private Set<Writer> writers = new LinkedHashSet<>();

    @Column(nullable = false)
    private String name;

    public void addWriter(Writer writer) {
        this.writers.add(writer);
        writer.getBooks().add(this);
    }

    public void removeWriter(Writer writer) {
        this.writers.remove(writer);
        writer.getBooks().remove(this);
    }
}
