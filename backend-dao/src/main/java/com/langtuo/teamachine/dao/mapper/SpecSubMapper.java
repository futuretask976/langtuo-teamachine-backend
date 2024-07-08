package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.SpecSubPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface SpecSubMapper {
    /**
     *
     * @param tenantCode
     * @param specSubCode
     * @return
     */
    SpecSubPO selectOne(@Param("tenantCode") String tenantCode, @Param("specSubCode") String specSubCode,
            @Param("specSubName") String specSubName);

    /**
     *
     * @return
     */
    List<SpecSubPO> selectList(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);

    /**
     *
     * @param specSubPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SpecSubPO specSubPO);

    /**
     *
     * @param specSubPO
     * @return
     */
    int update(SpecSubPO specSubPO);

    /**
     *
     * @param tenantCode
     * @param specSubCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode,
            @Param("specSubCode") String specSubCode);
}
