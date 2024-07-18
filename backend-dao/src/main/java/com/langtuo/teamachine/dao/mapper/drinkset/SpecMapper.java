package com.langtuo.teamachine.dao.mapper.drinkset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.drinkset.SpecPO;
import com.langtuo.teamachine.dao.query.drinkset.SpecQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
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
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
