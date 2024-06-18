package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import com.langtuo.teamachine.dao.po.TenantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TenantMapper {
    /**
     *
     * @param tenantCode
     * @return
     */
    TenantPO selectOne(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<TenantPO> selectList();

    /**
     *
     * @param tenantPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TenantPO tenantPO);

    /**
     *
     * @param tenantPO
     * @return
     */
    int update(TenantPO tenantPO);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(String tenantCode);
}
