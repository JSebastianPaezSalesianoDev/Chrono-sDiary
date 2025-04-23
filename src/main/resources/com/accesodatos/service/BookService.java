package com.accesodatos.service;

import java.util.List;



import com.accesodatos.dtos.bookdto.BookRequestDto;
import com.accesodatos.dtos.bookdto.BookResponseDto;


public interface BookService {

	BookResponseDto createBook(BookRequestDto bookRequestDto);
	
	List<BookResponseDto> getAllBooks();
	BookResponseDto getBookById(Long bookId);
	BookResponseDto updateBook(Long bookId, BookRequestDto bookRequestDto);
	void deleteBook(Long bookId);
	
	BookResponseDto addWriterToBook(Long bookId, Long writerId);
	
	
}
