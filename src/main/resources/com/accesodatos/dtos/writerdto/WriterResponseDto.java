package com.accesodatos.dtos.writerdto;

import java.util.Set;

import com.accesodatos.entity.Book;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class WriterResponseDto {
    private Long id;
    private String name;
    private Set<Book> books; 
}
