package com.langtuo.teamachine.dao.mapper.langtuo;

import com.langtuo.teamachine.dao.annotation.MySQLScan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@MySQLScan
@Repository
public interface SequenceDemoMapper {
    /**
     *
     * @return
     */
    public long getNextSequenceValue();
}
