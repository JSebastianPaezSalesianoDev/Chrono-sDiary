package com.accesodatos.mappers.book;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.accesodatos.dtos.bookdto.BookRequestDto;
import com.accesodatos.dtos.bookdto.BookResponseDto;
import com.accesodatos.entity.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
	
	//Dto to entity
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "writers", ignore = true)
	 Book toBook(BookRequestDto bookRequestDto);
	 
	 // entity to Dto
	 BookResponseDto toBookResponse(Book book);
	
}
