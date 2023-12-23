package tech.dut.fasto.errors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.util.constants.ApplicationConstants;


import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

    private static final String ERRORS_KEY = "error";
    private static final String MESSAGE_KEY = "message";

    private final Environment env;
    private final MessageService messageService;

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, NativeWebRequest request) {
        BindingResult result = exception.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors()
                .stream()
                .map(f -> new FieldErrorVM(f.getObjectName().replace("DTO$", ""), f.getField(), StringUtils.isNotBlank(f.getDefaultMessage()) ? messageService.getMessage(f.getDefaultMessage(), List.of(f.getField())): f.getCode())).toList();
        Problem problem = Problem
                .builder()
                .withStatus(Status.BAD_REQUEST)
                .with(ERRORS_KEY, messageService.getMessage("error.code.advice.argument"))
                .with(MESSAGE_KEY, fieldErrors)
                .build();
        return create(exception, problem, request);
    }

    @Override
    public ResponseEntity<Problem> handleAuthentication(AuthenticationException e, NativeWebRequest request) {
        String errorMessage = e.getMessage();
        if (e instanceof BadCredentialsException) {
            errorMessage = messageService.getMessage("error.password.username.not.correct");
        }

        Problem problem = Problem
                .builder()
                .withStatus(Status.UNAUTHORIZED)
                .with(ERRORS_KEY, messageService.getMessage("error.code.advice.authenticate.failed"))
                .with(MESSAGE_KEY, errorMessage)
                .build();
        return create(e, problem, request);
    }

    @Override
    public ResponseEntity<Problem> handleAccessDenied(AccessDeniedException e, NativeWebRequest request) {
        Problem problem = Problem
                .builder()
                .withStatus(Status.FORBIDDEN)
                .with(ERRORS_KEY, messageService.getMessage("error.code.advice.authenticate.forbidden"))
                .with(MESSAGE_KEY, messageService.getMessage("error.authenticate.forbidden"))
                .build();
        return create(e, problem, request);
    }


    @ExceptionHandler(value = FastoAlertException.class)
    public ResponseEntity<Problem> handleSafeFoodAlertException(FastoAlertException exception, NativeWebRequest request) {
        Problem problem = Problem
                .builder()
                .withStatus(Status.BAD_REQUEST)
                .with(ERRORS_KEY, exception.getError())
                .with(MESSAGE_KEY, exception.getMessage())
                .build();
        return create(exception, problem, request);
    }


    @Override
    public ProblemBuilder prepare(Throwable throwable, StatusType status, URI type) {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        if (activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_PRODUCTION)) {
            if (throwable instanceof DataAccessException) {
                return Problem
                        .builder()
                        .withStatus(status)
                        .with(MESSAGE_KEY, status.getReasonPhrase())
                        .with(ERRORS_KEY, messageService.getMessage("error.code.advice.database.access.failed"))
                        .withCause(
                                Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                        );
            }

            if (throwable instanceof HttpMessageConversionException) {
                return Problem
                        .builder()
                        .withStatus(status)
                        .with(MESSAGE_KEY, status.getReasonPhrase())
                        .with(ERRORS_KEY, messageService.getMessage("error.code.advice.http.convert.failed"))
                        .withCause(
                                Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                        );
            }


            if (containsPackageName(throwable.getMessage())) {
                return Problem
                        .builder()
                        .withStatus(status)
                        .with(MESSAGE_KEY, status.getReasonPhrase())
                        .with(ERRORS_KEY, messageService.getMessage("error.code.advice.runtime"))
                        .withCause(
                                Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                        );
            }
        }

        return Problem
                .builder()
                .withStatus(status)
                .with(MESSAGE_KEY, status.getReasonPhrase())
                .with(ERRORS_KEY, throwable.getMessage())
                .withCause(
                        Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled()).map(this::toProblem).orElse(null)
                );
    }

    private boolean containsPackageName(String message) {
        return StringUtils.containsAny(
                message,
                "org.",
                "java.",
                "net.",
                "javax.",
                "com.",
                "io.",
                "de.",
                "tech.dut.safefood"
        );
    }

    @Override
    public boolean isCausalChainsEnabled() {
        return true;
    }
}
