package life.khabanh.usersservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import life.khabanh.usersservices.dto.response.ApiFormResponse;
import org.springframework.web.servlet.View;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiFormResponse<String>> handlingRuntimeException(Exception exception) {
        ApiFormResponse<String> apiFormResponse = ApiFormResponse.<String>builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .result(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiFormResponse); // ✅ Return 500 instead of 400
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiFormResponse<Void>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiFormResponse<Void> apiFormResponse = ApiFormResponse.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        HttpStatus status = switch (errorCode) {
            case USER_NOT_EXISTED, INVITE_CODE_EXISTED -> HttpStatus.NOT_FOUND; // 404
            case INCORRECT_PASSWORD, UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED; // 401
            case USER_EXISTED -> HttpStatus.CONFLICT; // 409
            case INVALID_REQUEST, MISSING_REQUIRED_FIELDS -> HttpStatus.BAD_REQUEST; // 400
            default -> HttpStatus.INTERNAL_SERVER_ERROR; // 500
        };

        return ResponseEntity.status(status).body(apiFormResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiFormResponse<String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        ApiFormResponse<String> apiFormResponse = ApiFormResponse.<String>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result("Validation failed: " + exception.getBindingResult().getAllErrors().getFirst().getDefaultMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiFormResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiFormResponse<String>> handleMissingRequestBody(HttpMessageNotReadableException exception) {
        ApiFormResponse<String> response = ApiFormResponse.<String>builder()
                .code(ErrorCode.MISSING_REQUEST_BODY.getCode())
                .message("Missing request body")
                .result(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
