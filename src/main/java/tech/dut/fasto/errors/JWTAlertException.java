package tech.dut.fasto.errors;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

public class JWTAlertException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 5975801353965637181L;

    public JWTAlertException(String msg) {
        super(msg);
    }

    /**
     * Constructs an <code>InsufficientAuthenticationException</code> with the specified
     * message and root cause.
     *
     * @param msg   the detail message
     * @param cause root cause
     */
    public JWTAlertException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
