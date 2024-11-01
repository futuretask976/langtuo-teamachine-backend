package com.langtuo.teamachine.dao.mapper.rule;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.rule.DrainRuleToppingPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DrainRuleToppingMapper {
    /**
     *
     * @return
     */
    List<DrainRuleToppingPO> selectList(@Param("tenantCode") String tenantCode,
            @Param("drainRuleCode") String drainRuleCode);

    /**
     *
     * @param po
     * @return
     */
    int insert(DrainRuleToppingPO po);

    /**
     *
     * @param tenantCode
     * @param drainRuleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("drainRuleCode") String drainRuleCode);
}
