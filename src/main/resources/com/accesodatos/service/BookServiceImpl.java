package com.accesodatos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accesodatos.dtos.bookdto.BookRequestDto;
import com.accesodatos.dtos.bookdto.BookResponseDto;
import com.accesodatos.entity.Book;
import com.accesodatos.entity.Writer;
import com.accesodatos.exception.ResourceNotFoundException;
import com.accesodatos.mappers.book.BookMapper;
import com.accesodatos.repository.BookRepository;
import com.accesodatos.repository.WriterRepository;

@Service
public class BookServiceImpl implements BookService {

	private static final String BOOK_NOT_FOUND = "Book with id %d not found";
	private static final String WRITER_NOT_FOUND = "writer with id %d not found";

	@Autowired
	BookRepository bookRepository;
	@Autowired
	WriterRepository writerRepository;
	@Autowired
	BookMapper bookMapper;

	@Override
	public BookResponseDto createBook(BookRequestDto bookRequestDto) {
		Book book = bookMapper.toBook(bookRequestDto);
		Book saveBook = bookRepository.save(book);

		return bookMapper.toBookResponse(saveBook);
	}

	@Override
	public List<BookResponseDto> getAllBooks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookResponseDto getBookById(Long bookId) {
//		Book book = bookRepository.findById(bookId)
//				.orElseThrow(() -> new ResourceNotFoundException(String.format(BOOK_NOT_FOUND, bookId)));
		
		Book book = validateAndGetBook(bookId);

		return bookMapper.toBookResponse(book);
	}

	@Override
	public BookResponseDto updateBook(Long bookId, BookRequestDto bookRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBook(Long bookId) {
		// TODO Auto-generated method stub

	}
	

	@Override
	public BookResponseDto addWriterToBook(Long bookId, Long writerId) {
		Book book = validateAndGetBook(bookId);
		Writer writer = writerRepository.findById(writerId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(WRITER_NOT_FOUND, writerId)));
		book.addWriter(writer);
		
		Book updatedBook = bookRepository.save(book);
		
		return bookMapper.toBookResponse(updatedBook);
	}

	private Book validateAndGetBook(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(BOOK_NOT_FOUND, id)));
	}
}