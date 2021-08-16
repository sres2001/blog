package ru.skillbox.blog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.skillbox.blog.api.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<?> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new BaseResponse(e.getErrors()));
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<?> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e, HttpServletRequest request) {
        if (request.getRequestURI().contains("profile")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(
                    Map.of("photo", "Фото слишком большое, нужно не более 5 Мб")));
        }
        if (request.getRequestURI().contains("image")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(
                    Map.of("image", "Размер файла превышает допустимый размер")));
        }
        return null;
    }
}
