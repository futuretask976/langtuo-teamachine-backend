package com.langtuo.teamachine.dao.mapper.reportset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.reportset.InvalidActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
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
     * @param invalidActRecordPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(InvalidActRecordPO invalidActRecordPO);

    /**
     *
     * @param invalidActRecordPO
     * @return
     */
    int update(InvalidActRecordPO invalidActRecordPO);

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
