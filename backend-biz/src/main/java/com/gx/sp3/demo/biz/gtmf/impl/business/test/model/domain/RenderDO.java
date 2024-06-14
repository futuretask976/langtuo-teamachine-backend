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
public class RenderDO implements BlockLike {
    /**
     *
     */
    private TextBlockDO textBlockDO;

    /**
     *
     */
    private LinkBlockDO linkBlockDO;

    @Override
    public BaseVO toView() {
        return null;
    }

    @Override
    public Set<String> getBizCodeSet() {
        Set<String> bizCodeSet = Sets.newHashSet();
        if (textBlockDO != null && textBlockDO.getBizCodeSet() != null) {
            bizCodeSet.addAll(textBlockDO.getBizCodeSet());
        }
        if (linkBlockDO != null && linkBlockDO.getBizCodeSet() != null) {
            bizCodeSet.addAll(linkBlockDO.getBizCodeSet());
        }
        return bizCodeSet;
    }

    @Override
    public void setBizCodeSet(Set bizCodeSet) {
        // do nothing
    }

    @Override
    public String toString() {
        return "TradeInfoBlockDO["
                + "textBlockDO=" + textBlockDO
                + ", linkBlockDO=" + linkBlockDO + "]";
    }
}
