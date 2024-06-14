package com.langtuo.teamachine.dao.accessor;

import com.langtuo.teamachine.dao.mapper.langtuo.SequenceDemoMapper;

import javax.annotation.Resource;

public class SequenceDemoAccessor {
    @Resource
    private SequenceDemoMapper sequenceDemoMapper;

    public long getNextSequenceValue() {
        long rtn = this.sequenceDemoMapper.getNextSequenceValue();
        return rtn;
    }
}
