package com.langtuo.teamachine.dao.mapper.record;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.record.OrderToppingActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderToppingActRecordMapper {
    /**
     *
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "order_topping_act_record_sharding_", columns = {"tenantCode", "shopGroupCode"}, originName = "order_topping_act_record")
    List<OrderToppingActRecordPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("shopGroupCode") String shopGroupCode, @Param("idempotentMark") String idempotentMark);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "order_topping_act_record_sharding_", columns = {"tenantCode", "shopGroupCode"}, originName = "order_topping_act_record")
    int insert(OrderToppingActRecordPO po);

    /**
     * 
     * @param tenantCode
     * @param idempotentMark
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "order_topping_act_record_sharding_", columns = {"tenantCode", "shopGroupCode"}, originName = "order_topping_act_record")
    int delete(@Param("tenantCode") String tenantCode, @Param("shopGroupCode") String shopGroupCode,
            @Param("idempotentMark") String idempotentMark);
}
