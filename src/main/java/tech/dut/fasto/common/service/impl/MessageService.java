package tech.dut.fasto.common.service.impl;

import java.util.List;

public interface MessageService {
    String getMessage(String messageKey);

    String getMessage(String messageKey, List<String> args);
}
