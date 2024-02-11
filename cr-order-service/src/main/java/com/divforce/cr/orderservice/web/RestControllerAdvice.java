package com.divforce.cr.orderservice.web;

import com.divforce.cr.orderservice.domain.NoSuchVoucherException;
import com.divforce.cr.orderservice.domain.VoucherAlreadyUseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oscar Makala
 */
@Log4j2
@ControllerAdvice
public class RestControllerAdvice {

    @Value("${notification.voucher_not_found_sw}")
    private String voucherNotFoundSw;
    @Value("${notification.voucher_used_sw}")
    private String voucherUsedSw;
    /**
     * Handles the general error case. Log track trace at error level
     *
     * @param e the exception not handled by other exception handler methods
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CreateOrderResponse onException(Exception e) {
        log.info("Caught exception while handling a request {}", e.getMessage());
        String msg = getExceptionMessage(e);
        CreateOrderResponse response = new CreateOrderResponse(
                "" + HttpStatus.INTERNAL_SERVER_ERROR.value(),
                msg,
                null
        );
        log.info("createResponse({})", response);
        return response;
    }

    @ExceptionHandler(NoSuchVoucherException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CreateOrderResponse onVoucherNotFoundException(NoSuchVoucherException e) {
        log.info("Caught exception while handling a request {}", e.getMessage());
        CreateOrderResponse response = new CreateOrderResponse(
                "" + HttpStatus.INTERNAL_SERVER_ERROR.value(),
                voucherNotFoundSw,
                null
        );
        log.info("createResponse({})", response);
        return response;
    }

    @ExceptionHandler(VoucherAlreadyUseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CreateOrderResponse onVoucherAlreadyUsedException(VoucherAlreadyUseException e) {
        log.info("Caught exception while handling a request {}", e.getMessage());
        String msg = getExceptionMessage(e);
        CreateOrderResponse response = new CreateOrderResponse(
                "" + HttpStatus.INTERNAL_SERVER_ERROR.value(),
                voucherUsedSw,
                null
        );
        log.info("createResponse({})", response);
        return response;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ResponseBody
    public CreateOrderResponse conflict(DataIntegrityViolationException e) {
        log.info("Caught exception while handling a request {}", e.getMessage());
        CreateOrderResponse response = new CreateOrderResponse(
                "" + HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Data integrity violation",
                null
        );

        log.info("createResponse({})", response);
        return response;
    }


    /**
     * Client did not formulate a correct request. Log the exception message at warn level
     * and stack trace as trace level. Return response status HttpStatus.BAD_REQUEST
     * (400).
     *
     * @param e one of the exceptions, {@link MissingServletRequestParameterException},
     *          {@link UnsatisfiedServletRequestParameterException},
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CreateOrderResponse onClientGenericBadRequest(Exception e) {
        log.info("Caught exception while handling a request {}", e.getMessage());
        if (log.isTraceEnabled()) {
            logTraceLevelStrackTrace(e);
        }

        String message = null;
        if (e instanceof MethodArgumentTypeMismatchException) {
            final MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = (MethodArgumentTypeMismatchException) e;
            final Class<?> requiredType = methodArgumentTypeMismatchException.getRequiredType();

            final Class<?> enumType;

            if (requiredType.isEnum()) {
                enumType = requiredType;
            } else if (requiredType.isArray() && requiredType.getComponentType().isEnum()) {
                enumType = requiredType.getComponentType();
            } else {
                enumType = null;
            }

            if (enumType != null) {
                final String enumValues = StringUtils.arrayToDelimitedString(enumType.getEnumConstants(), ", ");
                message = String.format("The parameter '%s' must contain one of the following values: '%s'.", methodArgumentTypeMismatchException.getName(), enumValues);
            }
        } else if (e instanceof MethodArgumentNotValidException) {
            final MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<String> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            message = String.join(",", errors);
        }

        if (message == null) {
            message = getExceptionMessage(e);
        }


        CreateOrderResponse response = new CreateOrderResponse(
                "" + HttpStatus.BAD_REQUEST.value(),
                message,
                null
        );
        log.info("createResponse({})", response);
        return response;
    }

    /**
     * The exception handler is trigger if a JSR303 {@link ConstraintViolationException}
     * is being raised.
     * <p>
     * Log the exception message at warn level and stack trace as trace level. Return
     * response status HttpStatus.BAD_REQUEST (400).
     *
     * @param e the exceptions, {@link ConstraintViolationException}
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CreateOrderResponse onConstraintViolationException(ConstraintViolationException e) {
        log.info("Caught exception while handling a request {}", e.getMessage());
        if (log.isTraceEnabled()) {
            logTraceLevelStrackTrace(e);
        }
        var errorMessage = new StringBuilder();
        var first = true;
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            if (!first) {
                errorMessage.append("; ");
            }
            errorMessage.append(violation.getMessage());
            first = false;
        }
        CreateOrderResponse response = new CreateOrderResponse(
                "" + HttpStatus.BAD_REQUEST.value(),
                errorMessage.toString(),
                null
        );
        log.info("createResponse({})", response);
        return response;
    }


    private String logWarnLevelExceptionMessage(Exception e) {
        log.info("Caught exception while handling a request {}", e.getMessage());
        log.warn("Caught exception while handling a request: " + getExceptionMessage(e));
        return e.getClass().getSimpleName();
    }

    private String logTraceLevelStrackTrace(Throwable t) {
        log.info("Caught exception while handling a request {}", t.getMessage());
        return t.getClass().getSimpleName();
    }

    private String getExceptionMessage(Exception e) {
        return StringUtils.hasText(e.getMessage()) ? e.getMessage() : e.getClass().getSimpleName();
    }

}
