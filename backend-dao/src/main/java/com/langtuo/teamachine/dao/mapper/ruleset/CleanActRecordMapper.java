package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.ruleset.CleanActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface CleanActRecordMapper {
    /**
     *
     * @param tenantCode
     * @param machineCode
     * @param shopCode
     * @param cleanStartTime
     * @param toppingCode
     * @param pipelineNum
     * @return
     */
    CleanActRecordPO selectOne(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode,
            @Param("shopCode") String shopCode, @Param("cleanStartTime") String cleanStartTime,
            @Param("toppingCode") String toppingCode, @Param("pipelineNum") int pipelineNum);

    /**
     *
     * @return
     */
    List<CleanActRecordPO> selectList();

    /**
     *
     * @param cleanActRecordPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(CleanActRecordPO cleanActRecordPO);

    /**
     *
     * @param tenantCode
     * @param machineCode
     * @param shopCode
     * @param cleanStartTime
     * @param toppingCode
     * @param pipelineNum
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode,
               @Param("shopCode") String shopCode, @Param("cleanStartTime") String cleanStartTime,
               @Param("toppingCode") String toppingCode, @Param("pipelineNum") int pipelineNum);
}
