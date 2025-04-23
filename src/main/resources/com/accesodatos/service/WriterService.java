package com.accesodatos.service;

import java.util.List;

import com.accesodatos.dtos.bookdto.BookResponseDto;
import com.accesodatos.dtos.writerdto.WriterRequestDto;
import com.accesodatos.dtos.writerdto.WriterResponseDto;
import com.accesodatos.dtos.bookdto.BookRequestDto;

public interface WriterService {

	WriterResponseDto createWriter(WriterRequestDto writerRequestDto);	
	List<WriterResponseDto> getAllWriters();		
	WriterResponseDto getWriterById(Long writerId);	
	WriterResponseDto updateWriter(Long writerId, WriterRequestDto writerRequestDto);
	void deleteWriter(Long writerId);
	
	WriterResponseDto addBookToWriter(Long writerId, Long bookId);
	List<BookResponseDto> getBookByWriterId(Long id);
	
	void removeBookFromWriter(Long writerId, Long bookId);
	WriterResponseDto createAndAddBookToWriter(Long writerId, BookRequestDto bookRequestDto);

	
	
}
