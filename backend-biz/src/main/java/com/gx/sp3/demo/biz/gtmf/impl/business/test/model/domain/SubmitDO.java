package com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.base.BlockLike;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.view.base.BaseVO;
import lombok.Data;
import org.assertj.core.util.Sets;

import java.util.Set;

/**
 * @author miya
 */
@Data
public class SubmitDO implements BlockLike {
    /**
     *
     */
    private InputBlockDO inputBlockDO;

    /**
     *
     */
    private SelectBlockDO selectBlockDO;

    @Override
    public BaseVO toView() {
        return null;
    }

    @Override
    public Set<String> getBizCodeSet() {
        Set<String> bizCodeSet = Sets.newHashSet();
        if (inputBlockDO != null && inputBlockDO.getBizCodeSet() != null) {
            bizCodeSet.addAll(inputBlockDO.getBizCodeSet());
        }
        if (selectBlockDO != null && selectBlockDO.getBizCodeSet() != null) {
            bizCodeSet.addAll(selectBlockDO.getBizCodeSet());
        }
        return bizCodeSet;
    }

    @Override
    public void setBizCodeSet(Set bizCodeSet) {
        // do nothing
    }

    @Override
    public String toString() {
        return "textColor["
                + "inputBlockDO=" + inputBlockDO
                + ", selectBlockDO=" + selectBlockDO + "]";
    }
}
