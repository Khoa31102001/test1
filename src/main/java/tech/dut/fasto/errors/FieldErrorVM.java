package tech.dut.fasto.errors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@Setter
public class FieldErrorVM implements Serializable {
    @Serial
    private static final long serialVersionUID = 8492982089489107247L;
    String errorObjectName;

    String errorField;

    String errorMessage;

}
