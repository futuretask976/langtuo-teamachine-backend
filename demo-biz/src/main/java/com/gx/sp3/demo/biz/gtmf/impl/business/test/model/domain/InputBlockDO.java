package com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.base.BlockLike;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.base.BaseVO;
import lombok.Data;

import java.util.Set;

@Data
public class InputBlockDO implements BlockLike {
    /**
     *
     */
    private String inputStyle;

    /**
     *
     */
    private String inputVal;

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
        return "InputBlockDO["
                + "inputStyle=" + inputStyle
                + ", inputVal=" + inputVal + "]";
    }
}
