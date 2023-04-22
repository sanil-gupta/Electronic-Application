package com.sanil.electronic.store.exception;

import com.sanil.electronic.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //handle Resource not found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex)
    {
        logger.info("Exception Handler Invoked !!");
        ApiResponseMessage response = ApiResponseMessage
                                        .builder()
                                        .message(ex.getMessage())
                                        .httpStatus(HttpStatus.NOT_FOUND)
                                        .success(true)
                                        .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    //MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

            Map<String,Object> response = new HashMap<>();

            allErrors.stream().forEach(ObjectError -> {
                String message = ObjectError.getDefaultMessage();
                String field = ((FieldError)ObjectError).getField();
                response.put(field,message);
            });
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    //Handle bad api exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequestException ex)
    {
        logger.info("Bad Api Request !!");
        ApiResponseMessage response = ApiResponseMessage
                .builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
