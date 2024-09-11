package com.langtuo.teamachine.biz.service.util;

import com.langtuo.teamachine.api.model.ErrorMsgDTO;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Slf4j
public class MessageUtils {
    public static ErrorMsgDTO getErrorMsgDTO(ErrorCodeEnum errorCodeEnum) {
        MessageSource messageSource = SpringUtils.getMessageSource();
        ErrorMsgDTO errorMsgDTO = new ErrorMsgDTO();
        errorMsgDTO.setErrorCode(errorCodeEnum.getErrorCode());
        errorMsgDTO.setErrorMsg(messageSource.getMessage(errorCodeEnum.getErrorCode(), null,
                Locale.getDefault()));
        return errorMsgDTO;
    }
}
