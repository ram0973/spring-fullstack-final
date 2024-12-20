package ra.web.common.exceptions;

import ra.web.common.exceptions.dto.ApiExceptionResponseDto;
import ra.web.common.exceptions.dto.FieldViolation;
import ra.web.common.exceptions.dto.ValidationExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ControllerAdviceConfig extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        List<FieldViolation> fieldViolations = ex.getBindingResult().getFieldErrors().stream().map(o -> new FieldViolation(o.getField(), o.getDefaultMessage())).toList();
        return new ResponseEntity<>(new ValidationExceptionResponseDto(getUrl(request), status, fieldViolations), status);
    }

    // HttpStatus.resolve(status.value())

    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected ApiExceptionResponseDto handleThereIsNoSuchEntityException(Exception ex, WebRequest request) {
        return new ApiExceptionResponseDto(getUrl(request), ex.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected ApiExceptionResponseDto handleEmailAlreadyInUseException(Exception ex, WebRequest request) {
        return new ApiExceptionResponseDto(getUrl(request), ex.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    protected ApiExceptionResponseDto handleBadCredentialsException(Exception ex, WebRequest request) {
        return new ApiExceptionResponseDto(getUrl(request), ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected ApiExceptionResponseDto handleMethodArgumentTypeMismatchException(Exception ex, WebRequest request) {
        return new ApiExceptionResponseDto(getUrl(request), ex.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    protected ApiExceptionResponseDto handleEmptyResultDataAccessException(Exception ex, WebRequest request) {
        return new ApiExceptionResponseDto(getUrl(request), ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    protected ApiExceptionResponseDto handleForbiddenOperationException(Exception ex, WebRequest request) {
        return new ApiExceptionResponseDto(getUrl(request), ex.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ApiExceptionResponseDto(getUrl(request), ex.getLocalizedMessage(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(new ApiExceptionResponseDto(getUrl(request), ex.getLocalizedMessage(),
            HttpStatus.UNPROCESSABLE_ENTITY), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String getUrl(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
