package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaToppingPO;
import com.langtuo.teamachine.dao.po.TeaToppingTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaToppingMapper {
    /**
     *
     * @param tenantCode
     * @param toppingCode
     * @return
     */
    TeaToppingPO selectOne(@Param("tenantCode") String tenantCode, @Param("toppingCode") String toppingCode);

    /**
     *
     * @return
     */
    List<TeaToppingPO> selectList();

    /**
     *
     * @param teaToppingPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaToppingPO teaToppingPO);

    /**
     *
     * @param teaToppingPO
     * @return
     */
    int update(TeaToppingPO teaToppingPO);

    /**
     *
     * @param toppingCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("toppingCode") String toppingCode);
}
