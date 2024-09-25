package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drink.SpecItemRulePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface SpecItemRuleMapper {
    /**
     *
     * @return
     */
    List<SpecItemRulePO> selectListByTeaCode(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);

    /**
     *
     * @param po
     * @return
     */
    int insert(SpecItemRulePO po);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    int deleteByTeaCode(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);

    /**
     *
     * @param tenantCode
     * @param specItemCodeList
     * @return
     */
    int countBySpecItemCode(@Param("tenantCode") String tenantCode,
            @Param("specItemCodeList") List<String> specItemCodeList);

    /**
     *
     * @param tenantCode
     * @param specCode
     * @return
     */
    int countBySpecCode(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);
}
