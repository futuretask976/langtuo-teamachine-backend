package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.PermitActPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface PermitActMapper {
    /**
     *
     * @param permActGroupCode
     * @return
     */
    PermitActPO selectOne(@Param("permActGroupCode") String permActGroupCode);

    /**
     *
     * @return
     */
    List<PermitActPO> selectList();

    /**
     *
     * @param permitActPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(PermitActPO permitActPO);

    /**
     *
     * @param permitActPO
     * @return
     */
    int update(PermitActPO permitActPO);

    /**
     *
     * @param permActGroupCode
     * @return
     */
    int delete(String permActGroupCode);
}
