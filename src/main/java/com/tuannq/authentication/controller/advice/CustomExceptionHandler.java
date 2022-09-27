package com.tuannq.authentication.controller.advice;

import com.tuannq.authentication.exception.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.tuannq.authentication.model.response.ErrorsResponse;
import com.tuannq.authentication.model.response.data.MethodArgumentNotValidResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {
    private final MessageSource messageSource;

    @Autowired
    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorsResponse<String>> handlerNotFoundException(NotFoundException ex, WebRequest req) {
        // Log err
        ex.printStackTrace();

        try {
            var errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
            var response =new ErrorsResponse<>(HttpStatus.NOT_FOUND.name(), errorMessage);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.NOT_FOUND.name(), ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handlerBadRequestException(BadRequestException ex, WebRequest req) {
        // Log err
        ex.printStackTrace();

        try {
            var errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
            var response =new ErrorsResponse<>(HttpStatus.BAD_REQUEST.name(), errorMessage);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<?> handlerDuplicateRecordException(DuplicateRecordException ex, WebRequest req) {
        // Log err

        ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    // Xử lý tất cả các exception chưa được khai báo
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception ex, WebRequest req) {
        // Log err
        ex.printStackTrace();

        ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<?> handlerUnsupportedOperationException(
            UnsupportedOperationException ex,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            WebRequest req
    ) throws IOException {
        if (httpServletRequest.getQueryString().contains("=") && !httpServletRequest.getRequestURI().contains("api")) {
            httpServletResponse.sendRedirect(httpServletRequest.getRequestURI());
            return null;
        }

        ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handlerBadCredentialsException(BadCredentialsException ex) {
        ErrorsResponse<String> err = new ErrorsResponse<>(ex.getMessage(), HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorsResponse<List<MethodArgumentNotValidResponse>>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<MethodArgumentNotValidResponse> response = new ArrayList<>();
        for (var e : ex.getFieldErrors()) {
            try {
                var errorMessage = messageSource.getMessage(Objects.requireNonNull(e.getDefaultMessage()), e.getArguments(), LocaleContextHolder.getLocale());
                if (response.stream().anyMatch(l -> l.getField().equals(e.getField()))) {
                    response = response.stream().peek(l -> {
                        if (l.getField().equals(e.getField()) && !l.getMessage().contains(errorMessage))
                            l.setMessage(l.getMessage().concat("\n").concat(errorMessage));
                    }).collect(Collectors.toList());
                } else response.add(new MethodArgumentNotValidResponse(e.getField(), errorMessage));
            } catch (Exception exception) {
                if (response.stream().anyMatch(l -> l.getField().equals(e.getField()))) {
                    response = response.stream().peek(l -> {
                        if (l.getField().equals(e.getField()) && !l.getMessage().contains(Objects.requireNonNull(e.getDefaultMessage())))
                            l.setMessage(l.getMessage().concat("\n").concat(e.getDefaultMessage()));
                    }).collect(Collectors.toList());
                } else response.add(new MethodArgumentNotValidResponse(e.getField(), e.getDefaultMessage()));
            }
        }

        return new ResponseEntity<>(
                new ErrorsResponse<>(HttpStatus.BAD_REQUEST.name(), response),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {ArgumentException.class})
    protected ResponseEntity<ErrorsResponse<List<MethodArgumentNotValidResponse>>> handleMethodArgumentNotValid(ArgumentException ex) {
        String message = messageSource.getMessage("bad-request", null, LocaleContextHolder.getLocale());
        try {
            var errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
            var response = new ErrorsResponse<>(message, List.of(new MethodArgumentNotValidResponse(ex.getField(), errorMessage)));
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            var response = new ErrorsResponse<>(message, List.of(new MethodArgumentNotValidResponse(ex.getField(), ex.getMessage())));
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }


    @ExceptionHandler(value = {UsernameNotFoundException.class})
    protected ResponseEntity<ErrorsResponse<List<MethodArgumentNotValidResponse>>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String message = messageSource.getMessage("bad-request", null, LocaleContextHolder.getLocale());

        ErrorsResponse<List<MethodArgumentNotValidResponse>> response = new ErrorsResponse<>(message, List.of(new MethodArgumentNotValidResponse("email", ex.getMessage())));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
