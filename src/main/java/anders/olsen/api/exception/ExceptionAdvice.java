package anders.olsen.api.exception;

import anders.olsen.api.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
}
