package com.servicio.poliza.exception;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
@RestController
public class ValidationHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationHandler.class);

	private static final String TIPO_VALIDAR_PETICIONES = "/validaciones/peticiones";
	private static final String TIPO_VALIDAR_RECURSOS = "/validaciones/recursos";
	private static final String TIPO_VALIDAR_METODOS = "/validaciones/metodo";
	private static final String TIPO_VALIDAR_INTERNAL = "/error/interno";

	@ExceptionHandler(ArgumentRequiredException.class)
	public final ResponseEntity<ApiRespuestaExcepcionDTO> argumentRequiredException(ArgumentRequiredException ex,
			WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = ApiRespuestaExcepcionDTO.builder().tipoError(TIPO_VALIDAR_PETICIONES)
				.tituloError("Error Validación de parámetro").codigoError("E-010")
				.instanciaError(request.getDescription(false)).detalleError(ex.getMessage()).build();
		return new ResponseEntity<>(errorPeticion, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ApiRespuestaExcepcionDTO> notFoundException(NotFoundException ex, WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = ApiRespuestaExcepcionDTO.builder().tipoError(TIPO_VALIDAR_RECURSOS) 
				.tituloError("Recurso no encontrado").codigoError("E-404").instanciaError(request.getDescription(false))
				.detalleError(ex.getMessage()).build();
		return new ResponseEntity<>(errorPeticion, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> constraintViolationException(HttpServletResponse response, Exception ex,
			WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_PETICIONES,
				"Error Validación de parámetro", "E-010", request.getDescription(false));
		errorPeticion.setDetalleError(ex.getMessage());
		return new ResponseEntity<>(errorPeticion, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResponseStatusException.class)
	protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_RECURSOS,
				"Error al solicitar la petición", "E-010", request.getDescription(false));
		errorPeticion.setDetalleError(ex.getMessage());
		return new ResponseEntity<>(errorPeticion, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NullPointerException.class)
	public final ResponseEntity<Object> nullPointerException(NullPointerException ex, WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_INTERNAL,
				"Error Internal Server", "E-010", request.getDescription(false));
		errorPeticion.setDetalleError(ex.getMessage());
		LOGGER.error("Error generado por un nullPointerException: {}", ex.fillInStackTrace().toString());
		return new ResponseEntity<>(errorPeticion, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> manejadorException(Exception ex, WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_INTERNAL,
				"Error Internal Server", "E-010", request.getDescription(false));
		errorPeticion.setDetalleError(ex.getLocalizedMessage());
		LOGGER.error("Error generado por un Exception: {}", ex.fillInStackTrace().toString());
		return new ResponseEntity<>(errorPeticion, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_METODOS,
				"Metodo no admitido", "E-010", request.getDescription(false));
		errorPeticion.setDetalleError("El método de solicitud " + ex.getMethod() + " no es compatible.");
		return new ResponseEntity<>(errorPeticion, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<Object> listaErrores = new LinkedList<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_PETICIONES,
					"Error Validacion de parámetro", "E-010", request.getDescription(false));
			errorPeticion.setDetalleError(
					error.getDefaultMessage() + ", campo del error: " + ((FieldError) error).getField());
			listaErrores.add(errorPeticion);
		});
		return new ResponseEntity<>(listaErrores, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_PETICIONES,
				"Error Validacion de parámetro", "E-010", request.getDescription(false));
		errorPeticion.setDetalleError(ex.getMessage());
		return new ResponseEntity<>(errorPeticion, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_METODOS,
				"Contenido de un tipo no admitido.", "E-010", request.getDescription(false));
		errorPeticion.setDetalleError("El tipo de contenido " + ex.getContentType() + " no es compatible");
		return new ResponseEntity<>(errorPeticion, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		ApiRespuestaExcepcionDTO errorPeticion = new ApiRespuestaExcepcionDTO(TIPO_VALIDAR_RECURSOS,
				"Error al solicitar la petición", "E-010", request.getDescription(false));
		errorPeticion.setDetalleError(
				"No se encontró ningún recurso solicitado para " + ex.getHttpMethod() + " " + ex.getRequestURL());
		return new ResponseEntity<>(errorPeticion, HttpStatus.NOT_FOUND);
	}

}
