package com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.base.BaseVO;
import lombok.Data;

/**
 * @author miya
 */
@Data
public class InputVO extends BaseVO {
    private static final long serialVersionUID = 7920454941230117958L;

    /**
     *
     */
    private String style;

    /**
     *
     */
    private String color;
}
