package com.langtuo.teamachine.dao.mapper.reportset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.reportset.InvalidActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface InvalidActRecordMapper {
    /**
     *
     * @param tenantCode
     * @param machineCode
     * @param shopCode
     * @param invalidTime
     * @param toppingCode
     * @param pipelineNum
     * @return
     */
    InvalidActRecordPO selectOne(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode,
        @Param("shopCode") String shopCode, @Param("invalidTime") String invalidTime,
        @Param("toppingCode") String toppingCode, @Param("pipelineNum") int pipelineNum);

    /**
     *
     * @return
     */
    List<InvalidActRecordPO> selectList();

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(InvalidActRecordPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(InvalidActRecordPO po);

    /**
     *
     * @param tenantCode
     * @param machineCode
     * @param shopCode
     * @param invalidTime
     * @param toppingCode
     * @param pipelineNum
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode,
               @Param("shopCode") String shopCode, @Param("invalidTime") String invalidTime,
               @Param("toppingCode") String toppingCode, @Param("pipelineNum") int pipelineNum);
}
