package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaToppingTypePO;
import com.langtuo.teamachine.dao.po.TenantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaToppingTypeMapper {
    /**
     *
     * @param tenantCode
     * @param toppingTypeCode
     * @return
     */
    TeaToppingTypePO selectOne(@Param("tenantCode") String tenantCode, @Param("toppingTypeCode") String toppingTypeCode);

    /**
     *
     * @return
     */
    List<TeaToppingTypePO> selectList();

    /**
     *
     * @param teaToppingTypePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaToppingTypePO teaToppingTypePO);

    /**
     *
     * @param teaToppingTypePO
     * @return
     */
    int update(TeaToppingTypePO teaToppingTypePO);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("toppingTypeCode") String toppingTypeCode);
}
