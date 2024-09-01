package com.langtuo.teamachine.biz.service.util;

import com.langtuo.teamachine.api.model.ErrorMsgDTO;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class ApiUtils {
    public static ErrorMsgDTO getErrorMsgDTO(ErrorCodeEnum errorCodeEnum, MessageSource messageSource) {
        ErrorMsgDTO errorMsgDTO = new ErrorMsgDTO();
        errorMsgDTO.setErrorCode(errorCodeEnum.getErrorCode());
        errorMsgDTO.setErrorMsg(messageSource.getMessage(errorCodeEnum.getErrorCode(), null,
                Locale.getDefault()));
        return errorMsgDTO;
    }
}
