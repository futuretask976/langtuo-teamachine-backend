package com.langtuo.teamachine.dao.mapper.reportset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.reportset.SupplyActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
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
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SupplyActRecordPO supplyActRecordPO);

    /**
     *
     * @param po
     * @return
     */
    int update(SupplyActRecordPO po);

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
