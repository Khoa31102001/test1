package tech.dut.fasto.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ResourceBundleMessageSource resourceBundleMessageSource;

    @Override
    public String getMessage(String messageKey) {
        return resourceBundleMessageSource.getMessage(messageKey, null , LocaleContextHolder.getLocale());
    }

    @Override
    public String getMessage(String messageKey, List<String> args) {
        return resourceBundleMessageSource.getMessage(messageKey, args.toArray() , LocaleContextHolder.getLocale());
    }
}
