package com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author miya
 */
public abstract class BaseVO implements Serializable{
    private static final long serialVersionUID = -8566790249405804992L;

    /**
     *
     */
    @Getter
    @Setter
    private String bizCodeInfo;
}
