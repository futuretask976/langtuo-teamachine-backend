package com.gx.sp3.demo.dao.mapper.langtuo;

import com.gx.sp3.demo.dao.annotation.MySQLScan;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineToppingPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
