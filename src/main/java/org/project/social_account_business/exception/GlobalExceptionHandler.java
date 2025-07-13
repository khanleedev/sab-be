package org.project.social_account_business.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ApiMessageDto;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.form.ErrorForm;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String BAD_REQUEST = "BAD REQUEST";
    final ObjectMapper mapper = new ObjectMapper();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiMessageDto<String>> globalExceptionHandler(NotFoundException ex) {
        log.error("" + ex.getMessage(), ex);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setCode(ex.getCode());
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnregisteredCurrencyException.class})
    public ResponseEntity<ApiMessageDto<String>> unregisteredCurrencyException(UnregisteredCurrencyException ex) {
        log.error("" + ex.getMessage(), ex);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setCode(ex.getCode());
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.NOT_FOUND);
    }
//    @Override
//    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
//        apiMessageDto.setCode("ERROR handleNoHandlerFoundException");
//        apiMessageDto.setResult(false);
//        apiMessageDto.setMessage("[Ex3]: 404");
//        return new ResponseEntity<>(apiMessageDto, HttpStatus.NOT_FOUND);
//    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiMessageDto<String>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyBindingException.class)
    public ResponseEntity<ApiMessageDto> handleBindingException(MyBindingException ex) {
        log.error("Binding exception: " + ex.getMessage(), ex);
        ApiMessageDto response = new ApiMessageDto("error", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiMessageDto> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("Json processing exception: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiMessageDto("error", ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizationException.class})
    public ResponseEntity<ApiMessageDto<String>> notAllow(UnauthorizationException ex) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiMessageDto<String>> badRequest(BadRequestException ex) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setCode(ex.getCode());
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        ex.printStackTrace();
        Map<String, Object> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getPropertyPath() + " | " + violation.getMessage());
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<ApiMessageDto<String>> databaseError(DataAccessException ex) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage("Database Error");
        apiMessageDto.setCode(ErrorCode.DB_QUERY_ERROR);
        return new ResponseEntity<>(apiMessageDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({TokenExceptionHandler.class})
    public ResponseEntity<ApiMessageDto<String>> invalidTokenHandler(TokenExceptionHandler ex, WebRequest request) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setCode("Invalid token or token had expired!");
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomizeOverallException.class)
    public ResponseEntity<ApiMessageDto<String>> handleInternalExceptionAll(CustomizeOverallException ex, WebRequest request) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setCode("internalServerError");
        apiMessageDto.setResult(false);
        apiMessageDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}