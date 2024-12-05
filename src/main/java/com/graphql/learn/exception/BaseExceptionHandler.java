package com.graphql.learn.exception;

import com.graphql.learn.config.Constants;
import com.graphql.learn.exception.response.ResponseStatusDTO;
import com.graphql.learn.pagination.CustomPage;
import com.graphql.learn.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class BaseExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(BaseExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomPage<String>> handleErrors(Exception ex, WebRequest request) throws Exception {
        ResponseStatusDTO responseStatus=null;
        HttpStatus httpStatus=null;
        if(ex instanceof AuthenticationException){
            String authErrorCode = (String)request.getAttribute(Constants.AUTH_ERROR_CODE_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST);
            responseStatus = getErrorResponseByCode(authErrorCode,ex.getClass().getName(),ex);
            httpStatus=HttpStatus.UNAUTHORIZED ;
        }else{
            responseStatus = getErrorResponseByCode(ex.getMessage(),ex.getClass().getName(),ex);
            httpStatus = CommonUtil.getHttpStatusByException(ex);
        }
        return new ResponseEntity<>(new CustomPage<>(null, responseStatus), httpStatus);
    }

    private ResponseStatusDTO getErrorResponseByCode(String errorCode, String exceptionClassName, Exception ex){
        String errorMessage="Some Unexpected Error Occurred" ;
        ResponseStatusDTO responseStatusDTO = new ResponseStatusDTO(Constants.DEFAULT_ERROR_MESSAGE_CODE,errorMessage,null) ;
        try{
            responseStatusDTO = CommonUtil.getStatusObject(errorCode,ex);
        }catch(Exception exp){
            return responseStatusDTO;
        }
        return responseStatusDTO;
    }
}
