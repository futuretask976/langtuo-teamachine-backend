package com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.base.BaseVO;
import com.gx.sp3.demo.gtmf.extension.BizCodeSetLike;

/**
 * 区块类信息
 * @author miya
 */
public interface BlockLike<T extends BaseVO> extends BizCodeSetLike {

    /**
     * 区块数据来源
     * @return
     */
    default BlockDataSource getDataSource(){
        return BlockDataSource.RAW_DATA;
    }

    /**
     * 转换为VO
     * @return
     */
    T toView();

    /**
     * 区块数据来源
     */
    enum BlockDataSource{
        /**
         * 服务端直接下发组件数据
         */
        RAW_DATA,
        /**
         * 供Weex使用的jsBundle
         */
        JS_BUNDLE,
        /**
         * 需调用外部mtop接口获取数据
         */
        MTOP,
        ;
    }
}
