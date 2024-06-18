package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaToppingNormalPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaToppingNormalMapper {
    /**
     *
     * @param tenantCode
     * @param teaCode
     * @param toppingCode
     * @return
     */
    TeaToppingNormalPO selectOne(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode, @Param("toppingCode") String toppingCode);

    /**
     *
     * @return
     */
    List<TeaToppingNormalPO> selectList();

    /**
     *
     * @param teaToppingNormalPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaToppingNormalPO teaToppingNormalPO);

    /**
     *
     * @param teaToppingNormalPO
     * @return
     */
    int update(TeaToppingNormalPO teaToppingNormalPO);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @param toppingCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode, @Param("toppingCode") String toppingCode);
}
