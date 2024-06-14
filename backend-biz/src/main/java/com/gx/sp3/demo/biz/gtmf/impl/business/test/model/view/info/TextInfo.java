package com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.info;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.TextVO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.base.ViewLike;
import lombok.Data;

/**
 *
 */
@Data
public class TextInfo implements ViewLike<TextVO> {
    /**
     *
     */
    private String style;

    /**
     *
     */
    private String color;

    private TextInfo() {
    }

    @Override
    public String getBizCode() {
        return null;
    }

    @Override
    public void setBizCode(String bizCode) {

    }

    @Override
    public TextVO toView() {
        TextVO textVO = new TextVO();
        return textVO;
    }
}
