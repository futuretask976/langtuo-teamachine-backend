package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.PermActGroupPO;
import com.langtuo.teamachine.dao.po.PermActPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface PermActGroupMapper {
    /**
     *
     * @param permActGroupCode
     * @return
     */
    PermActGroupPO selectOne(@Param("permActGroupCode") String permActGroupCode);

    /**
     *
     * @return
     */
    List<PermActGroupPO> selectList();

    /**
     *
     * @param permActGroupPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(PermActGroupPO permActGroupPO);

    /**
     *
     * @param permActGroupPO
     * @return
     */
    int update(PermActGroupPO permActGroupPO);

    /**
     *
     * @param permActGroupCode
     * @return
     */
    int delete(String permActGroupCode);
}
