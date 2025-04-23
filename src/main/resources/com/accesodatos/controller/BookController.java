package com.accesodatos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accesodatos.dtos.api.ApiResponseDto;
import com.accesodatos.dtos.bookdto.BookRequestDto;
import com.accesodatos.dtos.bookdto.BookResponseDto;
import com.accesodatos.dtos.writerdto.WriterResponseDto;
import com.accesodatos.entity.Book;
import com.accesodatos.service.BookServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BookController {

	private static final String BOOK_RESOURCE = "/books";
	private static final String BOOK_ID_PATH = BOOK_RESOURCE + "/{bookId}";

	@Autowired
	BookServiceImpl bookService;

	@GetMapping(BOOK_RESOURCE + "/ping")
	public ResponseEntity<String> pong() {
		return ResponseEntity.ok("pong Book...");
	}

	@PostMapping(value = BOOK_RESOURCE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<BookResponseDto>> createBook(
			@Valid @RequestBody BookRequestDto bookRequestDto) {
		BookResponseDto createBook = bookService.createBook(bookRequestDto);
		ApiResponseDto<BookResponseDto> response = new ApiResponseDto<>("Book created successfully",
				HttpStatus.CREATED.value(), createBook);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = BOOK_RESOURCE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<List<BookResponseDto>>> getAllBooks() {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@GetMapping(value = BOOK_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<BookResponseDto>> getBookById(@PathVariable Long bookId) {
		BookResponseDto book = bookService.getBookById(bookId);
		
		ApiResponseDto<BookResponseDto> response = new ApiResponseDto<>("Book getted successfully", HttpStatus.OK.value(), book);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping(value = BOOK_ID_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<BookResponseDto>> updateBook(@Valid @PathVariable Long bookId,
			@RequestBody BookRequestDto bookRequestDto) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@DeleteMapping(value = BOOK_ID_PATH)
	public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@PostMapping(value = BOOK_ID_PATH + "/writers/{writerId}",
		 	 consumes = MediaType.APPLICATION_JSON_VALUE, 
		 	 produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<BookResponseDto>> addExistinBookToWriter(@PathVariable Long bookId, @PathVariable Long writerId) {
		
		BookResponseDto updateBook = bookService.addWriterToBook(bookId, writerId);
		ApiResponseDto<BookResponseDto> response = new ApiResponseDto<>("Book created successfully",
				HttpStatus.OK.value(), updateBook);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}