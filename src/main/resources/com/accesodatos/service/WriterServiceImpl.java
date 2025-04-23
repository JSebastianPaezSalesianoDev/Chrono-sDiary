package com.accesodatos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accesodatos.dtos.bookdto.BookRequestDto;
import com.accesodatos.dtos.bookdto.BookResponseDto;
import com.accesodatos.dtos.writerdto.WriterRequestDto;
import com.accesodatos.dtos.writerdto.WriterResponseDto;
import com.accesodatos.entity.Writer;
import com.accesodatos.entity.Book;
import com.accesodatos.exception.ResourceNotFoundException;
import com.accesodatos.mappers.book.BookMapper;
import com.accesodatos.mappers.writter.WriterMapper;

import com.accesodatos.repository.WriterRepository;

import lombok.extern.slf4j.Slf4j;

import com.accesodatos.repository.BookRepository;

@Service
@Slf4j
public class WriterServiceImpl implements WriterService {
	
	private final static String WRITER_NOT_FOUND = "Writer with id %id not found";
	private final static String BOOK_NOT_FOUND = "Book with id %id not found";
	
	@Autowired private WriterRepository writerRepository;
	@Autowired private BookRepository bookRepository;
	@Autowired private BookMapper bookMapper;
	
	@Autowired private WriterMapper writerMapper;
	
	
	private Writer validateAndGetWriter(Long id) {
		return writerRepository.findById(id)
										.orElseThrow(() -> new ResourceNotFoundException(
												String.format(WRITER_NOT_FOUND, id)));
	}
	@Override
	public WriterResponseDto createWriter(WriterRequestDto writerRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WriterResponseDto> getAllWriters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WriterResponseDto getWriterById(Long writerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WriterResponseDto updateWriter(Long writerId, WriterRequestDto writerRequestDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteWriter(Long writerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WriterResponseDto addBookToWriter(Long writerId, Long bookId) {
		Writer writer = validateAndGetWriter(writerId);
		
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException(
						String.format(BOOK_NOT_FOUND, bookId)));
		
		writer.addBook(book);
		Writer updatedWriter = writerRepository.save(writer);
		
		return writerMapper.toWriteResponse(updatedWriter);
		

	}
	
	@Override
	public List<BookResponseDto> getBookByWriterId(Long writerId) {
		Writer writer = validateAndGetWriter(writerId);
		
		return writer.getBooks().stream()
				.map(bookMapper::toBookResponse).toList();
		
	}
	@Override
	public void removeBookFromWriter(Long writerId, Long bookId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public WriterResponseDto createAndAddBookToWriter(Long writerId, BookRequestDto bookRequestDto) {
		Writer writer = validateAndGetWriter(writerId);
		
		Book book = bookRepository.findByName(bookRequestDto.getName())
										.orElseGet(() ->{
											Book newBook = bookMapper.toBook(bookRequestDto);
											return bookRepository.save(newBook);
										});
		writer.addBook(book);
		writerRepository.save(writer);
		return writerMapper.toWriteResponse(writer);
	}
	


}
