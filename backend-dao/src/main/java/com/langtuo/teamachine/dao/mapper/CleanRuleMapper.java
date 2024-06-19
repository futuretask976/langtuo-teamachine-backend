package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.CleanRulePO;
import com.langtuo.teamachine.dao.po.ShopPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface CleanRuleMapper {
    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    CleanRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode);

    /**
     *
     * @return
     */
    List<CleanRulePO> selectList();

    /**
     *
     * @param cleanRulePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(CleanRulePO cleanRulePO);

    /**
     *
     * @param cleanRulePO
     * @return
     */
    int update(CleanRulePO cleanRulePO);

    /**
     *
     * @param tenantCode
     * @param cleanRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("cleanRuleCode") String cleanRuleCode);
}
