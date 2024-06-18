package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaSpecRelPO;
import com.langtuo.teamachine.dao.po.TeaTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaSpecRelMapper {
    /**
     *
     * @param tenantCode
     * @param teaUnitCode
     * @return
     */
    TeaSpecRelPO selectOne(@Param("tenantCode") String tenantCode, @Param("teaUnitCode") String teaUnitCode);

    /**
     *
     * @return
     */
    List<TeaSpecRelPO> selectList();

    /**
     *
     * @param teaSpecRelPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaSpecRelPO teaSpecRelPO);

    /**
     *
     * @param teaSpecRelPO
     * @return
     */
    int update(TeaSpecRelPO teaSpecRelPO);

    /**
     *
     * @param tenantCode
     * @param teaUnitCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("teaUnitCode") String teaUnitCode);
}
