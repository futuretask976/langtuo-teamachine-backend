package com.langtuo.teamachine.dao.mapper.report;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.record.SupplyActRecordPO;
import com.langtuo.teamachine.dao.query.record.SupplyActRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OrderSpecItemReportMapper {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    SupplyActRecordPO selectOne(@Param("tenantCode") String tenantCode,
            @Param("idempotentMark") String idempotentMark);

    /**
     * Only for test
     * @return
     */
    List<SupplyActRecordPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<SupplyActRecordPO> search(SupplyActRecordQuery query);

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
     * @param idempotentMark
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode,
            @Param("idempotentMark") String idempotentMark);
}
