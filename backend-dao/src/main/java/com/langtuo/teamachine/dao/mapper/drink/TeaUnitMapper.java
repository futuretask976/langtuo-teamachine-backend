package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drink.TeaUnitPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface TeaUnitMapper {
    /**
     *
     * @param tenantCode
     * @param teaUnitCode
     * @return
     */
    TeaUnitPO selectOne(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode,
            @Param("teaUnitCode") String teaUnitCode, @Param("teaUnitName") String teaUnitName);

    /**
     *
     * @return
     */
    List<TeaUnitPO> selectList(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaUnitPO po);

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
     * @param specCode
     * @return
     */
    int countBySpecCode(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);

    /**
     *
     * @param tenantCode
     * @param specItemCodeList
     * @return
     */
    int countBySpecItemCode(@Param("tenantCode") String tenantCode,
            @Param("specItemCodeList") List<String> specItemCodeList);
}
