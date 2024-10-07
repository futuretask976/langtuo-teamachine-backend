package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.drink.ToppingBaseRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ToppingBaseRuleMapper {
    /**
     *
     * @return
     */
    List<ToppingBaseRulePO> selectList(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ToppingBaseRulePO po);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);

    /**
     *
     * @param tenantCode
     * @param toppingCode
     * @return
     */
    int countByToppingCode(@Param("tenantCode") String tenantCode, @Param("toppingCode") String toppingCode);
}
