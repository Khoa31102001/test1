package tech.dut.fasto.errors;

import java.io.Serial;

public class FastoAlertException extends BaseException {

    @Serial
    private static final long serialVersionUID = 7370579852315610957L;

    public FastoAlertException(String error, String message) {
        super(error, message);
    }
}
