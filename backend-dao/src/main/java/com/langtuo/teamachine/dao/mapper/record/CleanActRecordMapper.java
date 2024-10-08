package com.langtuo.teamachine.dao.mapper.record;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.record.CleanActRecordPO;
import com.langtuo.teamachine.dao.query.record.CleanActRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CleanActRecordMapper {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "clean_act_record_shard_", columns = "tenantCode", defaultName = "clean_act_record")
    CleanActRecordPO selectOne(@Param("tenantCode") String tenantCode,
            @Param("idempotentMark") String idempotentMark);

    /**
     *
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "clean_act_record_shard_", columns = "tenantCode", defaultName = "clean_act_record")
    List<CleanActRecordPO> search(CleanActRecordQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "clean_act_record_shard_", columns = "tenantCode", defaultName = "clean_act_record")
    int insert(CleanActRecordPO po);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "clean_act_record_shard_", columns = "tenantCode", defaultName = "clean_act_record")
    int delete(@Param("tenantCode") String tenantCode,
            @Param("idempotentMark") String idempotentMark);
}
