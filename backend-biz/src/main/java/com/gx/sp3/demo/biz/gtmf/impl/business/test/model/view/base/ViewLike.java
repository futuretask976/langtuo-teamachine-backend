package com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.base;

import com.gx.sp3.demo.gtmf.extension.BizCodeLike;

/**
 * @author muchen.zyy
 * @date 2018/4/25
 */
public interface ViewLike<T extends BaseVO> extends BizCodeLike {

    /**
     * 先校验数据再进行转换
     * @return
     */
    default T toViewWithLegalCheck(){
        checkIfLegal();
        return toView();
    }

    /**
     * 转换VO
     * @return
     */
    T toView();

    /**
     * 校验数据是否合法
     */
    default void checkIfLegal(){}
}
