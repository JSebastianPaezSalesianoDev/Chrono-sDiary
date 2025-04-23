package com.accesodatos.mappers.writter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.accesodatos.dtos.writerdto.WriterRequestDto;
import com.accesodatos.dtos.writerdto.WriterResponseDto;
import com.accesodatos.entity.Writer;

@Mapper(componentModel = "spring")
public interface WriterMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "books", ignore = true)
	Writer toWrite(WriterRequestDto writerRequestDto);
	
	WriterResponseDto toWriteResponse(Writer writer);

}
