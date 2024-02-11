package com.divforce.cr.voucherservice.web;

import com.divforce.cr.encrypt.util.CampaignCryptoException;
import com.divforce.cr.voucherservice.exception.NoSuchCampaignException;
import com.divforce.cr.voucherservice.exception.NoSuchVoucherException;
import com.divforce.cr.voucherservice.exception.VoucherAlreadyExistsException;
import com.divforce.cr.voucherservice.exception.VoucherNotUploadedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * @author Oscar Makala
 */
@Log4j2
@ControllerAdvice
public class RestControllerAdvice {
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
    public Problem onException(Exception e) {
        log.error("Caught exception while handling a request", e);
        String logref = e.getClass().getSimpleName();
        String msg = getExceptionMessage(e);
        return Problem.create().withTitle(logref).withDetail(msg);
    }

    /**
     * Handles the general error case. Log track trace at error level
     *
     * @param e the exception not handled by other exception handler methods
     * @return the error response in JSON format with media type
     * application/vnd.error+json
     */
    @ExceptionHandler(CampaignCryptoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Problem onCryptoException(Exception e) {
        log.error("Caught exception while handling a request", e);
        String logref = e.getClass().getSimpleName();
        String msg = getExceptionMessage(e);
        return Problem.create().withTitle(logref).withDetail(msg);
    }

    @ExceptionHandler({NoSuchCampaignException.class, NoSuchVoucherException.class, VoucherAlreadyExistsException.class, VoucherNotUploadedException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Problem onVoucherException(Exception e) {
        String logref = logWarnLevelExceptionMessage(e);
        if (log.isTraceEnabled()) {
            logTraceLevelStrackTrace(e);
        }
        String msg = getExceptionMessage(e);
        return Problem.create().withTitle(logref).withDetail(msg);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ResponseBody
    public Problem conflict(DataIntegrityViolationException e) {
        String logref = logWarnLevelExceptionMessage(e);
        if (log.isTraceEnabled()) {
            logTraceLevelStrackTrace(e);
        }
        String msg = getExceptionMessage(e);
        return Problem.create().withTitle(logref).withDetail(msg);
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
    public Problem onConstraintViolationException(ConstraintViolationException e) {
        String logref = logWarnLevelExceptionMessage(e);
        if (log.isTraceEnabled()) {
            logTraceLevelStrackTrace(e);
        }
        final StringBuilder errorMessage = new StringBuilder();
        boolean first = true;
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            if (!first) {
                errorMessage.append("; ");
            }
            errorMessage.append(violation.getMessage());
            first = false;
        }
        return Problem.create().withTitle(logref).withDetail(errorMessage.toString());
    }

    private String logWarnLevelExceptionMessage(Exception e) {
        log.warn("Caught exception while handling a request: " + getExceptionMessage(e));
        return e.getClass().getSimpleName();
    }

    private String logTraceLevelStrackTrace(Throwable t) {
        log.trace("Caught exception while handling a request", t);
        return t.getClass().getSimpleName();
    }

    private String getExceptionMessage(Exception e) {
        return StringUtils.hasText(e.getMessage()) ? e.getMessage() : e.getClass().getSimpleName();
    }
}
