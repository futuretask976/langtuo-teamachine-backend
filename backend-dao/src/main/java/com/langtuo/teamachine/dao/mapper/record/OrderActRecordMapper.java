package com.langtuo.teamachine.dao.mapper.record;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.record.OrderActRecordPO;
import com.langtuo.teamachine.dao.query.record.OrderActRecordQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderActRecordMapper {
    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    OrderActRecordPO selectOne(@Param("tenantCode") String tenantCode, @Param("idempotentMark") String idempotentMark);

    /**
     *
     * @return
     */
    List<OrderActRecordPO> search(OrderActRecordQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrderActRecordPO po);

    /**
     *
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("idempotentMark") String idempotentMark);
}
