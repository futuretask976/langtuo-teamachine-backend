package com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.base.BlockLike;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.base.BaseVO;
import lombok.Data;

import java.util.Set;

@Data
public class TextBlockDO implements BlockLike {
    /**
     *
     */
    private String textStyle;

    /**
     *
     */
    private String textColor;

    @Override
    public BaseVO toView() {
        return null;
    }

    @Override
    public Set<String> getBizCodeSet() {
        return null;
    }

    @Override
    public void setBizCodeSet(Set bizCodeSet) {

    }

    @Override
    public String toString() {
        return "TextBlockDO["
                + "textStyle=" + textStyle
                + ", textColor=" + textColor + "]";
    }
}
