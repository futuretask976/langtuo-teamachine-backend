package com.gx.sp3.demo.biz.gtmf.impl.business.test.activity;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.ability.DefaultTestSubmitBlockAbility;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.SubmitBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.InputBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.SelectBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.SubmitDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.transfer.RequestTransfer;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.transfer.SubmitRequestDTO;
import com.gx.sp3.demo.gtmf.annotation.Activity;

import javax.annotation.Resource;

@Activity
public class TestSubmitBlockActivity {
    @Resource
    private RequestTransfer requestTransfer;

    @Resource
    private DefaultTestSubmitBlockAbility defaultTestSubmitBlockAbility;

    public SubmitDO submit(SubmitRequestDTO submitRequestDTO) {
        SubmitBlockContext submitBlockContext = requestTransfer.buildContext(submitRequestDTO);

        InputBlockDO inputBlockDO = defaultTestSubmitBlockAbility.buildInputBlockDO(submitBlockContext);
        System.out.println("!!! " + inputBlockDO.toString());

        SelectBlockDO selectBlockDO = defaultTestSubmitBlockAbility.buildSelectBlockDO(submitBlockContext);
        System.out.println("!!! " + selectBlockDO.toString());

        SubmitDO submitDO = new SubmitDO();
        submitDO.setInputBlockDO(inputBlockDO);
        submitDO.setSelectBlockDO(selectBlockDO);
        return submitDO;
    }
}
