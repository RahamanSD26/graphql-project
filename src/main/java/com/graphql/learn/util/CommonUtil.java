package com.graphql.learn.util;

import com.graphql.learn.config.Constants;
import com.graphql.learn.domain.BaseEntity;
import com.graphql.learn.dto.BaseEntityDTO;
import com.graphql.learn.exception.response.ResponseStatusDTO;
import com.graphql.learn.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Component
public class CommonUtil {

    private static MessageSource errorMessageSource ;

    private static MessageSource successMessageSource ;

    @Autowired
    public CommonUtil(MessageSource errorMessageSource, MessageSource successMessageSource) {
        CommonUtil.errorMessageSource = errorMessageSource;
        CommonUtil.successMessageSource = successMessageSource;
    }

    public static void mapCommonFieldsToEntity(BaseEntity entity, BaseEntityDTO dto){
        //Common Fields
        entity.setId(dto.getId());
        entity.setGuid(dto.getGuid());
        entity.setIsActive(dto.getIsActive());
        if(dto.getCreatedOn()!=null) {
            entity.setCreatedOn(CommonUtil.convertDateTimeToMilliSeconds(dto.getCreatedOn()));
        }
        if(dto.getLastUpdatedOn()!=null) {
            entity.setLastUpdatedOn(CommonUtil.convertDateTimeToMilliSeconds(dto.getLastUpdatedOn()));
        }
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setLastUpdatedBy(dto.getLastUpdatedBy());
    }

    public static void mapCommonFieldsToDto(BaseEntity entity, BaseEntityDTO dto){
        //Common Fields
        dto.setId(entity.getId());
        dto.setGuid(entity.getGuid());
        dto.setIsActive(entity.getIsActive());
        if(entity.getCreatedOn()!=null) {
            dto.setCreatedOn(CommonUtil.convertMilliSecondsToDateTime(entity.getCreatedOn()));
        }
        if(entity.getLastUpdatedOn()!=null) {
            dto.setLastUpdatedOn(CommonUtil.convertMilliSecondsToDateTime(entity.getLastUpdatedOn()));
        }
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setLastUpdatedBy(entity.getLastUpdatedBy());
    }

    private static String convertMilliSecondsToDateTime(Long msDateTime){
        DateFormat obj = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date res = new Date(msDateTime);
        return obj.format(res);
    }

    public static String getCustomGeneratedId(String prefix, Integer partNumberDigitCount, Long serialNumber) throws Exception {
        String generatedId=null ;
        Integer serialNumberDigitCount=getDigitsCountFromInteger(serialNumber) ;
        if(prefix!=null){
            prefix=prefix.trim();
            if(prefix.length()==2){
                if(partNumberDigitCount>2){
                    generatedId=prefix ;
                    for(int i=1; i<=partNumberDigitCount-serialNumberDigitCount; i++){
                        generatedId = generatedId + Constants.NUMBER_CONSTANT_ZERO;
                    }
                    generatedId=generatedId+serialNumber ;
                    return generatedId;
                }else{
                    throw new Exception("ERR_ADMIN_0060");
                }
            }else{
                throw new Exception("ERR_ADMIN_0059");
            }
        }else{
            throw new Exception("ERR_ADMIN_0058");
        }
    }


    private static Long convertDateTimeToMilliSeconds(String dateTime){
        //String myDate = "2014/10/29 18:10:45";
        //LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss") );
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        /*
          With this new Date/Time API, when using a date, you need to
          specify the Zone where the date/time will be used. For your case,
          seems that you want/need to use the default zone of your system.
          Check which zone you need to use for specific behaviour e.g.
          CET or America/Lima
        */
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String getNewGeneratedId(BaseRepository baseRepository, String idPrefix, Integer partNumberCount) throws Exception {
        Optional<BaseEntity> lastEntity = (Optional<BaseEntity>) baseRepository.findTopByOrderByCreatedOnDesc() ;
        Long serialNumber= Constants.NUMBER_CONSTANT_LONG_ONE ;
        if(lastEntity.isPresent()){
            Long lastSerialNumber = Long.valueOf(lastEntity.get().getGuid().replace(idPrefix,Constants.BLANK_CONSTANT));
            serialNumber = lastSerialNumber+1;
            if(serialNumber>getLimitFromPartNumberCount(partNumberCount)){
                throw new Exception("ERR_ADMIN_0062") ;
            }
        }
        return getCustomGeneratedId(idPrefix,partNumberCount,serialNumber) ;
    }

    private static Long getLimitFromPartNumberCount(Integer partNumberCount){
        Long limit = 0L;
        String limitString="";
        final String NINE="9";
        for(int i=0; i<partNumberCount; i++){
            limitString=limitString+NINE;
        }
        limit=Long.parseLong(limitString);
        return limit;
    }

    public static Integer getDigitsCountFromInteger(Long number){
        Integer count = 0 ;
        while (number != 0) {
            number /= 10; ++count;
        }
        return count;
    }

    public static Locale getPreferredLocale(){
        return Locale.ENGLISH ;
    }
    public static ResponseStatusDTO getStatusObject(String statusCode, Exception ex){
        ResponseStatusDTO responseDTO = new ResponseStatusDTO();
        String statusMessage = Constants.BLANK_CONSTANT;
        boolean isKnownMessage=true;
        statusCode=statusCode.trim();

        //Setting Default Messages
        if(statusCode.startsWith(Constants.SUCCESS_CODE_PREFIX)){
            statusMessage = successMessageSource.getMessage(Constants.DEFAULT_SUCCESS_MESSAGE_CODE,null, getPreferredLocale());
            responseDTO = new ResponseStatusDTO(Constants.DEFAULT_SUCCESS_MESSAGE_CODE,statusMessage,null) ;
        }else if(statusCode.startsWith(Constants.ERROR_CODE_PREFIX)){
            statusMessage = errorMessageSource.getMessage(Constants.DEFAULT_ERROR_MESSAGE_CODE,null, getPreferredLocale());
            responseDTO = new ResponseStatusDTO(Constants.DEFAULT_ERROR_MESSAGE_CODE,statusMessage,null) ;
        }else{
            isKnownMessage=false;
            statusMessage = errorMessageSource.getMessage(Constants.DEFAULT_ERROR_MESSAGE_CODE,null, getPreferredLocale());
            responseDTO = new ResponseStatusDTO(Constants.DEFAULT_ERROR_MESSAGE_CODE,statusMessage,statusCode) ;
        }
        //Setting StatusCode Message
        if(isKnownMessage){
            //Checking Error Code
            if(statusCode!=null){
                statusCode = statusCode.trim() ;
                if(statusCode.length()>3){
                    //Extracting Data
                    String extraData=null;
                    if(statusCode.contains(Constants.COLON_CONSTANT)){
                        String[] dataArray=statusCode.split(Constants.COLON_CONSTANT);
                        statusCode=dataArray[0];
                        extraData=dataArray[1];
                    }
                    //Getting Error & Success Code Messages
                    if(statusCode.startsWith(Constants.ERROR_CODE_PREFIX)){
                        //Getting Specific Error Message By Code
                        statusMessage = errorMessageSource.getMessage(statusCode,null, getPreferredLocale()) ;
                    }else if(statusCode.startsWith(Constants.SUCCESS_CODE_PREFIX)){
                        //Getting Specific Success Message By Code
                        statusMessage = successMessageSource.getMessage(statusCode,null, getPreferredLocale()) ;
                    }else{
                        responseDTO.setActualMessage(statusCode);
                    }
                    //Setting Final Message & Code
                    responseDTO.setCode(statusCode);
                    if(extraData!=null){
                        statusMessage=statusMessage+extraData;
                    }
                    responseDTO.setMessage(statusMessage);
                }
            }
        }else{
            statusCode=getExceptionSpecificErrorCode(ex);
            if(statusCode!=null){
                responseDTO.setCode(statusCode);
                responseDTO.setMessage(errorMessageSource.getMessage(statusCode,null, getPreferredLocale()));
                responseDTO.setActualMessage(null);
            }
        }
        return responseDTO;
    }

    private static String getExceptionSpecificErrorCode(Exception ex){
        String errorCode = null ;
        if(ex instanceof MethodArgumentNotValidException){
            errorCode = getErrorCodeMessageFromMANVException((MethodArgumentNotValidException)ex) ;
        }
        return errorCode ;
    }

    private static String getErrorCodeMessageFromMANVException(MethodArgumentNotValidException exception){
        final Optional<ObjectError> firstError = exception.getBindingResult().getAllErrors().stream().findFirst();
        if (firstError.isPresent()) {
            return firstError.get().getDefaultMessage();
        }
        return null ;
    }

    public static String getRandomUUID(Integer digits){
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static HttpStatus getHttpStatusByException(Exception ex){
        return HttpStatus.BAD_REQUEST ;
    }
}
