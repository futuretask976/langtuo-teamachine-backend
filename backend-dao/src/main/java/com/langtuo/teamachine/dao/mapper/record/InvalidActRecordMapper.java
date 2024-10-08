package com.langtuo.teamachine.dao.mapper.record;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.record.InvalidActRecordPO;
import com.langtuo.teamachine.dao.query.record.InvalidActRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InvalidActRecordMapper {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "invalid_act_record_shard_", columns = "tenantCode", defaultName = "invalid_act_record")
    InvalidActRecordPO selectOne(@Param("tenantCode") String tenantCode,
            @Param("idempotentMark") String idempotentMark);

    /**
     * Only for test
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "invalid_act_record_shard_", columns = "tenantCode", defaultName = "invalid_act_record")
    List<InvalidActRecordPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "invalid_act_record_shard_", columns = "tenantCode", defaultName = "invalid_act_record")
    List<InvalidActRecordPO> search(InvalidActRecordQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "invalid_act_record_shard_", columns = "tenantCode", defaultName = "invalid_act_record")
    int insert(InvalidActRecordPO po);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "invalid_act_record_shard_", columns = "tenantCode", defaultName = "invalid_act_record")
    int delete(@Param("tenantCode") String tenantCode,
            @Param("idempotentMark") String idempotentMark);
}
