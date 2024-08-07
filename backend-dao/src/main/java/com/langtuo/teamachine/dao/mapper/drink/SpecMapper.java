package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drink.SpecPO;
import com.langtuo.teamachine.dao.query.drink.SpecQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface SpecMapper {
    /**
     *
     * @param tenantCode
     * @param specCode
     * @return
     */
    SpecPO selectOne(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode, @Param("specName") String specName);

    /**
     *
     * @return
     */
    List<SpecPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<SpecPO> search(SpecQuery query);

    /**
     *
     * @param specPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SpecPO specPO);

    /**
     *
     * @param specPO
     * @return
     */
    int update(SpecPO specPO);

    /**
     *
     * @param specCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);
}
