package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.po.drink.ToppingAdjustRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
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
     * @param po
     * @return
     */
    int insert(ToppingAdjustRulePO po);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);
}
