package com.langtuo.teamachine.dao.mapper.drinkset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drinkset.ToppingAccuracyTemplatePO;
import com.langtuo.teamachine.dao.query.drinkset.ToppingAccuracyTemplateQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface ToppingAccuracyTemplateMapper {
    /**
     *
     * @param tenantCode
     * @param templateCode
     * @param templateName
     * @return
     */
    ToppingAccuracyTemplatePO selectOne(@Param("tenantCode") String tenantCode, @Param("templateCode") String templateCode, @Param("templateName") String templateName);

    /**
     *
     * @return
     */
    List<ToppingAccuracyTemplatePO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<ToppingAccuracyTemplatePO> search(ToppingAccuracyTemplateQuery query);

    /**
     *
     * @param specPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ToppingAccuracyTemplatePO specPO);

    /**
     *
     * @param specPO
     * @return
     */
    int update(ToppingAccuracyTemplatePO specPO);

    /**
     *
     * @param specCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);
}
