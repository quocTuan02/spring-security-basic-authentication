package com.tuannq.authentication.controller.advice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.tuannq.authentication.exception.ArgumentException;
import com.tuannq.authentication.exception.BadRequestException;
import com.tuannq.authentication.exception.DuplicateRecordException;
import com.tuannq.authentication.exception.NotFoundException;
import com.tuannq.authentication.model.response.ErrorsResponse;
import com.tuannq.authentication.model.response.data.MethodArgumentNotValidResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    private final MessageSource messageSource;

    @Autowired
    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorsResponse<String>> handlerNotFoundException(NotFoundException ex) {
        // Log err
        ex.printStackTrace();

        try {
            var errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
            var response = new ErrorsResponse<>(HttpStatus.NOT_FOUND.name(), errorMessage);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.NOT_FOUND.name(), ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handlerBadRequestException(BadRequestException ex) {
        // Log err
        ex.printStackTrace();

        try {
            var errorMessage = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
            var response = new ErrorsResponse<>(HttpStatus.BAD_REQUEST.name(), errorMessage);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<?> handlerDuplicateRecordException(DuplicateRecordException ex) {
        // Log err

        ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    // Xử lý tất cả các exception chưa được khai báo
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception ex) {
        // Log err
        ex.printStackTrace();

        ErrorsResponse<String> err = new ErrorsResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<?> handlerUnsupportedOperationException(
            UnsupportedOperationException ex,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
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

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        var message = messageSource.getMessage("bad-request", null, LocaleContextHolder.getLocale());
        String errorCodeMessage;

        var requiredType = exception.getRequiredType();
        if (requiredType != null) {
            switch (requiredType.getName()) {
                case "java.time.Instant":
                    errorCodeMessage = "time.invalid";
                    break;
                case "java.lang.Long":
                case "java.lang.Integer":
                    errorCodeMessage = "number.invalid";
                    break;
                default:
                    errorCodeMessage = exception.getLocalizedMessage();
            }
        } else {
            errorCodeMessage = exception.getLocalizedMessage();
        }

        ErrorsResponse<List<MethodArgumentNotValidResponse>> response;
        try {
            var errorMessage = messageSource.getMessage(errorCodeMessage, null, LocaleContextHolder.getLocale());
            response = new ErrorsResponse<>(message, List.of(new MethodArgumentNotValidResponse(exception.getName(), errorMessage)));
        } catch (Exception ex) {
            response = new ErrorsResponse<>(message, List.of(new MethodArgumentNotValidResponse(exception.getName(), errorCodeMessage)));
        }
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        var message = messageSource.getMessage("bad-request", null, LocaleContextHolder.getLocale());
        if (exception.getCause() instanceof JsonMappingException) {
            var jsonMappingException = ((JsonMappingException) exception.getCause());
            var errorMessage = "";
            var fieldName = new StringBuilder();
            for (var path : jsonMappingException.getPath()) {
                if (path.getIndex() == -1) {
                    if (path.getFieldName() == null) {
                        continue;
                    }
                    if (fieldName.length() == 0) {
                        fieldName.append(path.getFieldName());
                    } else {
                        fieldName.append(".").append(path.getFieldName());
                    }
                } else {
                    fieldName.append("[").append(path.getIndex()).append("]");
                }
            }
            var errorCodeMessage = exception.getLocalizedMessage();
            if (jsonMappingException instanceof MismatchedInputException) {
                var mismatchedInputException = ((MismatchedInputException) jsonMappingException);
                switch (mismatchedInputException.getTargetType().getName()) {
                    case "java.time.Instant":
                        errorCodeMessage = "time.invalid";
                        break;
                    case "java.lang.Long":
                    case "java.lang.Integer":
                        errorCodeMessage = "number.invalid";
                        break;
                    case "java.util.ArrayList":
                        errorCodeMessage = "list.invalid";
                        break;
                    default: {
                        log.warn(exception.getMessage(), exception);
                        errorCodeMessage = "object.invalid";
                    }
                }
            }
            try {
                errorMessage = messageSource.getMessage(errorCodeMessage, null, LocaleContextHolder.getLocale());
            } catch (Exception ex) {
                errorMessage = errorCodeMessage;
            }
            var response = new ErrorsResponse<>(message, List.of(new MethodArgumentNotValidResponse(fieldName.toString(), errorMessage)));

            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        log.error(exception.getMessage(), exception);
        var response = new ErrorsResponse<>(
                HttpStatus.BAD_REQUEST.name(),
                null
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {UsernameNotFoundException.class})
    protected ResponseEntity<ErrorsResponse<List<MethodArgumentNotValidResponse>>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String message = messageSource.getMessage("bad-request", null, LocaleContextHolder.getLocale());

        ErrorsResponse<List<MethodArgumentNotValidResponse>> response = new ErrorsResponse<>(message, List.of(new MethodArgumentNotValidResponse("email", ex.getMessage())));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
