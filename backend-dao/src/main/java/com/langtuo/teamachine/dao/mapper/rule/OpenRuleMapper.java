package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.OpenRulePO;
import com.langtuo.teamachine.dao.query.rule.OpenRuleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OpenRuleMapper {
    /**
     *
     * @param tenantCode
     * @param openRuleCode
     * @param openRuleName
     * @return
     */
    OpenRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("openRuleCode") String openRuleCode,
            @Param("openRuleName") String openRuleName);

    /**
     *
     * @return
     */
    List<OpenRulePO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<OpenRulePO> search(OpenRuleQuery query);

    /**
     *
     * @param cleanRulePO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OpenRulePO cleanRulePO);

    /**
     *
     * @param cleanRulePO
     * @return
     */
    int update(OpenRulePO cleanRulePO);

    /**
     *
     * @param tenantCode
     * @param openRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("openRuleCode") String openRuleCode);
}
