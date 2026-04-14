package com.colorsteel.erp.common.exception;

import com.colorsteel.erp.common.result.Result;
import com.colorsteel.erp.common.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusiness(BusinessException ex) {
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidation(Exception ex) {
        String msg = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException m) {
            var field = m.getBindingResult().getFieldError();
            if (field != null) {
                msg = field.getField() + ": " + field.getDefaultMessage();
            }
        }
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), msg);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleBadCredentials(BadCredentialsException ex) {
        return Result.fail(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Void> handleAccessDenied(AccessDeniedException ex) {
        return Result.fail(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
    }

    /**
     * 例如：在浏览器地址栏打开仅支持 POST 的接口会发 GET，避免误报为 500。
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result<Void> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity: {}", ex.getMessage());
        return Result.fail(ResultCode.CONFLICT.getCode(), "数据约束冲突（如编号重复）");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String supported = ex.getSupportedHttpMethods() != null
                ? ex.getSupportedHttpMethods().toString()
                : "POST";
        String msg = "当前请求为 " + ex.getMethod()
                + "，该路径不支持；请使用 " + supported + "（登录请用 POST + JSON body）";
        return Result.fail(ResultCode.METHOD_NOT_ALLOWED.getCode(), msg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleOthers(Exception ex) {
        log.error("Unhandled error", ex);
        return Result.fail(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
    }
}
