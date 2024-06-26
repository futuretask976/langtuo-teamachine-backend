package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.InvalidActRecordPO;
import com.langtuo.teamachine.dao.po.SupplyActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface SupplyActRecordMapper {
    /**
     *
     * @param tenantCode
     * @param machineCode
     * @param shopCode
     * @param supplyTime
     * @param toppingCode
     * @param pipelineNum
     * @return
     */
    SupplyActRecordPO selectOne(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode,
        @Param("shopCode") String shopCode, @Param("supplyTime") String supplyTime,
        @Param("toppingCode") String toppingCode, @Param("pipelineNum") int pipelineNum);

    /**
     *
     * @return
     */
    List<SupplyActRecordPO> selectList();

    /**
     *
     * @param supplyActRecordPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SupplyActRecordPO supplyActRecordPO);

    /**
     *
     * @param supplyActRecordPO
     * @return
     */
    int update(SupplyActRecordPO supplyActRecordPO);

    /**
     *
     * @param tenantCode
     * @param machineCode
     * @param shopCode
     * @param supplyTime
     * @param toppingCode
     * @param pipelineNum
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode,
               @Param("shopCode") String shopCode, @Param("supplyTime") String supplyTime,
               @Param("toppingCode") String toppingCode, @Param("pipelineNum") int pipelineNum);
}
