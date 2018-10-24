package anders.olsen.api.exception;

import anders.olsen.api.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * Global handling of exceptions
 */
@ControllerAdvice
public class ExceptionAdvice {

    /**
     * @return {@link ApiResponse} when {@link BadCredentialsException} is thrown
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> badCredentials() {
        return new ResponseEntity<>(
                new ApiResponse(false, "Unauthorized", HttpServletResponse.SC_UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);

    }

    /**
     * @return {@link ApiResponse} when {@link MethodArgumentNotValidException} is thrown
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> badInput() {
        return new ResponseEntity<>(
                new ApiResponse(false, "Improper format of input!", HttpServletResponse.SC_BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * @return {@link ApiResponse} when {@link AccessDeniedException} is thrown
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> accessDenied() {
        return new ResponseEntity<>(
                new ApiResponse(false, "Access denied, forbidden!", HttpServletResponse.SC_FORBIDDEN),
                HttpStatus.FORBIDDEN
        );
    }

    /**
     * @return {@link ApiResponse} when {@link UsernameNotFoundException} is thrown
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> usernameNotFound() {
        return new ResponseEntity<>(
                new ApiResponse(false, "Username not found", HttpServletResponse.SC_BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * @return {@link ApiResponse} when {@link HttpMessageNotReadableException} is thrown
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> httpNotReadable() {
        return new ResponseEntity<>(
                new ApiResponse(false, "Invalid JSON", HttpServletResponse.SC_BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
}
