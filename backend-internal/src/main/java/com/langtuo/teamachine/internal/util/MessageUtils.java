package com.langtuo.teamachine.internal.util;

import cn.hutool.extra.spring.SpringUtil;
import com.langtuo.teamachine.api.model.ErrorMsgDTO;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Slf4j
public class MessageUtils {
    public static ErrorMsgDTO getErrorMsgDTO(ErrorCodeEnum errorCodeEnum) {
        MessageSource messageSource = getMessageSource();
        ErrorMsgDTO errorMsgDTO = new ErrorMsgDTO();
        errorMsgDTO.setErrorCode(errorCodeEnum.getErrorCode());
        errorMsgDTO.setErrorMsg(messageSource.getMessage(errorCodeEnum.getErrorCode(), null,
                Locale.getDefault()));
        return errorMsgDTO;
    }

    private static MessageSource getMessageSource() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        MessageSource messageSource = appContext.getBean(MessageSource.class);
        return messageSource;
    }
}