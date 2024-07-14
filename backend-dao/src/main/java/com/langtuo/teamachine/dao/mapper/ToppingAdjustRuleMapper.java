package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.ToppingAdjustRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface ToppingAdjustRuleMapper {
    /**
     *
     * @param tenantCode
     * @param teaUnitCode
     * @return
     */
    ToppingAdjustRulePO selectOne(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode,
            @Param("teaUnitCode") String teaUnitCode, @Param("toppingCode") String toppingCode);

    /**
     *
     * @return
     */
    List<ToppingAdjustRulePO> selectList(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode,
            @Param("teaUnitCode") String teaUnitCode);

    /**
     *
     * @param toppingAdjustRulePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ToppingAdjustRulePO toppingAdjustRulePO);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);
}
