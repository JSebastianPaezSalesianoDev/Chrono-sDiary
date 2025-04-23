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

import com.accesodatos.dtos.api.ApiError;
import com.accesodatos.dtos.api.ApiResponseDto;
import com.accesodatos.dtos.bookdto.BookRequestDto;
import com.accesodatos.dtos.bookdto.BookResponseDto;	
import com.accesodatos.dtos.writerdto.WriterRequestDto;
import com.accesodatos.dtos.writerdto.WriterResponseDto;
import com.accesodatos.service.WriterServiceImpl;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Writer", description = "Controller for writer")
public class WriterController {

	private static final String WRITER_RESOURCE = "/writers";
	private static final String WRITER_ID_PATH = WRITER_RESOURCE + "/{writerId}";

	@Autowired
	WriterServiceImpl writerService;

	@Hidden
	@GetMapping(WRITER_RESOURCE + "/ping")
	public ResponseEntity<String> pong() {
		return ResponseEntity.ok("pong Book...");
	}

	@Operation(summary = "Add a ner writer to the database",
			description = "Add a new writer to the database",
			tags = {"Writer"})
	@ApiResponses(
			value = {
					@ApiResponse(
							responseCode = "201",
							description = "Writer created successfully",
							content = {
									@Content(
											mediaType = MediaType.APPLICATION_JSON_VALUE,
											schema = @Schema(
													implementation = ApiResponseDto.class,
													subTypes = {
															WriterResponseDto.class
													}
													)
											)
							}
							),
					@ApiResponse(
							responseCode = "405",
							description = "Invalid Input",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(
											implementation = ApiError.class
											)
									)
							)
			}
			)
	@PostMapping(value = WRITER_RESOURCE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<WriterResponseDto>> createWriter(
			@Parameter(description = "Create a new writer in the database", required = true)
			@Valid @RequestBody WriterRequestDto writerRequestDto) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Operation(summary = "Get all writer from database",
			description = "Get all writers",
			tags = {"Writer"})
	@ApiResponses(
			value = {
					@ApiResponse(
							responseCode = "200",
							description = "Writer getted successfully",
							content = {
									@Content(
											mediaType = MediaType.APPLICATION_JSON_VALUE,
											schema = @Schema(
													implementation = ApiResponseDto.class,
													subTypes = {
															WriterResponseDto.class
													}
													)
											)
							}
			)
			}
			)
	@GetMapping(value = WRITER_RESOURCE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<List<WriterResponseDto>>> getAllWriters() {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@Operation(summary = "Get writer by id from database",
			description = "Get writer by id",
			tags = {"Writer"})
	@ApiResponses(
			value = {
					@ApiResponse(
							responseCode = "200",
							description = "Writer getted successfully",
							content = {
									@Content(
											mediaType = MediaType.APPLICATION_JSON_VALUE,
											schema = @Schema(
													implementation = ApiResponseDto.class,
													subTypes = {
															WriterResponseDto.class
													},
													example = """
															{
																"Status": "OK",
																"message": "Writer fetched successfully",
																"data": {
																	"id": 1,
																	"name": "John Doe",
																	"books": [
																		{
																			"id": 1,
																			"name": "Sample book"
																		}
																	]
																},
																"timestamp": "04/01/2025 20:34:00"
															}
															"""
													)
											)
							}
			),
					@ApiResponse(
							responseCode = "400",
							description = "invalid id supplied",
							content = @Content
							),
					@ApiResponse(
							responseCode = "404",
							description = "writer not found",
							content = @Content(
									schema = @Schema(
											implementation = ApiError.class
											)
									)
							),
					@ApiResponse(
							responseCode = "500",
							description = "Internal error server",
							content = {@Content(schema = @Schema)}
							)
			}
			)
	@GetMapping(value = WRITER_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<WriterResponseDto>> getWriterById(
			@Parameter(description = "Getted a new writer by id in the database", required = true)
			@PathVariable Long writerId) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@PutMapping(value = WRITER_ID_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<WriterResponseDto>> updateWriter(@Valid @PathVariable Long writerId,
			@RequestBody WriterRequestDto writerRequestDto) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	@DeleteMapping(value = WRITER_ID_PATH)
	public ResponseEntity<Void> deleteWriter(@PathVariable Long writerId) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	@PostMapping(value = WRITER_ID_PATH
			+ "/books/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<WriterResponseDto>> addExistinBookToWriter(@PathVariable Long writerId,
			@PathVariable Long bookId) {

		WriterResponseDto updateWriter = writerService.addBookToWriter(writerId, bookId);
		ApiResponseDto<WriterResponseDto> response = new ApiResponseDto<>("Book created successfully",
				HttpStatus.OK.value(), updateWriter);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = WRITER_ID_PATH + "/books", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<List<BookResponseDto>>> getBooksByWriterId(@PathVariable Long writerId) {

		List<BookResponseDto> books = writerService.getBookByWriterId(writerId);
		ApiResponseDto<List<BookResponseDto>> response = new ApiResponseDto<>("Books fetched successfully",
				HttpStatus.OK.value(), books);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = WRITER_ID_PATH + "/books", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<WriterResponseDto>> createAndAddBookToWriter(
			@PathVariable Long writerId, @Valid @RequestBody BookRequestDto bookRequestDto ) {
		WriterResponseDto updatedWriter = writerService.createAndAddBookToWriter(writerId, bookRequestDto);
		ApiResponseDto<WriterResponseDto> response = new ApiResponseDto<>("Books created and add successfully",
				HttpStatus.CREATED.value(), updatedWriter);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

}