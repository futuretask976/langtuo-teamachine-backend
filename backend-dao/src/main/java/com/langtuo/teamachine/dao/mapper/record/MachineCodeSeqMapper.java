package com.langtuo.teamachine.dao.mapper.record;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@TeaMachineSQLScan
@Repository
public interface MachineCodeSeqMapper {
    /**
     *
     * @return
     */
    public long getNextSeqValue();
}
