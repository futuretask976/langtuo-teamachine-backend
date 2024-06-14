package com.gx.sp3.demo.dao.accessor.langtuo;

import com.gx.sp3.demo.dao.mapper.langtuo.MachineTeaMapper;
import com.gx.sp3.demo.dao.mapper.langtuo.SequenceDemoMapper;

import javax.annotation.Resource;

public class SequenceDemoAccesstor {
    @Resource
    private SequenceDemoMapper sequenceDemoMapper;

    public long getNextSequenceValue() {
        long rtn = this.sequenceDemoMapper.getNextSequenceValue();
        return rtn;
    }
}
