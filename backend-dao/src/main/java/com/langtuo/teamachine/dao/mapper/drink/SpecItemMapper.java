package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drink.SpecItemPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface SpecItemMapper {
    /**
     *
     * @param tenantCode
     * @param specItemCode
     * @return
     */
    SpecItemPO selectOne(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode,
            @Param("specItemCode") String specItemCode);

    /**
     *
     * @return
     */
    List<SpecItemPO> selectList(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);

    /**
     *
     * @param specItemPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SpecItemPO specItemPO);

    /**
     *
     * @param specItemPO
     * @return
     */
    int update(SpecItemPO specItemPO);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);
}
