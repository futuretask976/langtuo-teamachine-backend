package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplPO;
import com.langtuo.teamachine.dao.query.drink.AccuracyTplQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface AccuracyTplMapper {
    /**
     *
     * @param tenantCode
     * @param templateCode
     * @param templateName
     * @return
     */
    AccuracyTplPO selectOne(@Param("tenantCode") String tenantCode, @Param("templateCode") String templateCode, @Param("templateName") String templateName);

    /**
     *
     * @return
     */
    List<AccuracyTplPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<AccuracyTplPO> search(AccuracyTplQuery query);

    /**
     *
     * @param specPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(AccuracyTplPO specPO);

    /**
     *
     * @param specPO
     * @return
     */
    int update(AccuracyTplPO specPO);

    /**
     *
     * @param specCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);
}
