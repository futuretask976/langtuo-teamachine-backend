package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.PermActPO;
import com.langtuo.teamachine.dao.po.TenantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface PermActMapper {
    /**
     *
     * @param permActGroupCode
     * @return
     */
    PermActPO selectOne(@Param("permActGroupCode") String permActGroupCode);

    /**
     *
     * @return
     */
    List<PermActPO> selectList();

    /**
     *
     * @param permActPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(PermActPO permActPO);

    /**
     *
     * @param permActPO
     * @return
     */
    int update(PermActPO permActPO);

    /**
     *
     * @param permActGroupCode
     * @return
     */
    int delete(String permActGroupCode);
}
