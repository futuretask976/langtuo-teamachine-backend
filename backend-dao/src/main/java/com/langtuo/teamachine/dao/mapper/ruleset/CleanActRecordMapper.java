package com.langtuo.teamachine.dao.mapper.ruleset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.ruleset.CleanActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
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
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
