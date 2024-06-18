package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaToppingAdjustPO;
import com.langtuo.teamachine.dao.po.TeaToppingNormalPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaToppingAdjustMapper {
    /**
     *
     * @param tenantCode
     * @param teaUnitCode
     * @return
     */
    TeaToppingAdjustPO selectOne(@Param("tenantCode") String tenantCode, @Param("teaUnitCode") String teaUnitCode);

    /**
     *
     * @return
     */
    List<TeaToppingAdjustPO> selectList();

    /**
     *
     * @param teaToppingAdjustPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaToppingAdjustPO teaToppingAdjustPO);

    /**
     *
     * @param teaToppingAdjustPO
     * @return
     */
    int update(TeaToppingAdjustPO teaToppingAdjustPO);

    /**
     *
     * @param tenantCode
     * @param teaUnitCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("teaUnitCode") String teaUnitCode);
}
