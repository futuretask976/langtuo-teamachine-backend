package com.langtuo.teamachine.dao.mapper.recordset;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.recordset.OrderSpecItemActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OrderSpecItemActRecordMapper {
    /**
     *
     * @return
     */
    List<OrderSpecItemActRecordPO> selectList(@Param("tenantCode") String tenantCode, @Param("orderId") String orderId);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrderSpecItemActRecordPO po);

    /**
     *
     * @param tenantCode
     * @param orderId
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("orderId") String orderId);
}
