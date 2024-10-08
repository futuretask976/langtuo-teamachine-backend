package com.langtuo.teamachine.dao.mapper.record;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.record.SupplyActRecordPO;
import com.langtuo.teamachine.dao.query.record.SupplyActRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SupplyActRecordMapper {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "supply_act_record_shard_", columns = "tenantCode", defaultName = "supply_act_record")
    SupplyActRecordPO selectOne(@Param("tenantCode") String tenantCode,
            @Param("idempotentMark") String idempotentMark);

    /**
     * Only for test
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "supply_act_record_shard_", columns = "tenantCode", defaultName = "supply_act_record")
    List<SupplyActRecordPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "supply_act_record_shard_", columns = "tenantCode", defaultName = "supply_act_record")
    List<SupplyActRecordPO> search(SupplyActRecordQuery query);

    /**
     *
     * @param supplyActRecordPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "supply_act_record_shard_", columns = "tenantCode", defaultName = "supply_act_record")
    int insert(SupplyActRecordPO supplyActRecordPO);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "supply_act_record_shard_", columns = "tenantCode", defaultName = "supply_act_record")
    int update(SupplyActRecordPO po);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "supply_act_record_shard_", columns = "tenantCode", defaultName = "supply_act_record")
    int delete(@Param("tenantCode") String tenantCode,
            @Param("idempotentMark") String idempotentMark);
}
