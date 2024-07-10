package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaToppingRelPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaToppingRelMapper {
    /**
     *
     * @param tenantCode
     * @param teaCode
     * @param toppingCode
     * @return
     */
    TeaToppingRelPO selectOne(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode,
            @Param("toppingCode") String toppingCode);

    /**
     *
     * @return
     */
    List<TeaToppingRelPO> selectList(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);

    /**
     *
     * @param teaToppingRelPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaToppingRelPO teaToppingRelPO);

    /**
     *
     * @param teaToppingRelPO
     * @return
     */
    int update(TeaToppingRelPO teaToppingRelPO);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @param toppingCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode,
            @Param("toppingCode") String toppingCode);
}
