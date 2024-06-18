package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaSpecPO;
import com.langtuo.teamachine.dao.po.TeaSpecSubPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaSpecSubMapper {
    /**
     *
     * @param tenantCode
     * @param specSubCode
     * @return
     */
    TeaSpecSubPO selectOne(@Param("tenantCode") String tenantCode, @Param("specSubCode") String specSubCode);

    /**
     *
     * @return
     */
    List<TeaSpecSubPO> selectList();

    /**
     *
     * @param teaSpecSubPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaSpecSubPO teaSpecSubPO);

    /**
     *
     * @param teaSpecSubPO
     * @return
     */
    int update(TeaSpecSubPO teaSpecSubPO);

    /**
     *
     * @param tenantCode
     * @param specSubCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("specSubCode") String specSubCode);
}
