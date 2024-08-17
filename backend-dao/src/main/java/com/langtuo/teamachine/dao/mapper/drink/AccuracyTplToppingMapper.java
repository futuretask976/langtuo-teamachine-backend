package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplPO;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplToppingPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface AccuracyTplToppingMapper {
    /**
     *
     * @return
     */
    List<AccuracyTplPO> selectList(@Param("tenantCode") String tenantCode, @Param("templateCode") String templateCode);

    /**
     *
     * @param specPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(AccuracyTplToppingPO specPO);

    /**
     *
     * @param templateCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("templateCode") String templateCode);
}
