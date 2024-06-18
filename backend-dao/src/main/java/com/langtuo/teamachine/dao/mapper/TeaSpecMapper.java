package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaSpecPO;
import com.langtuo.teamachine.dao.po.TeaToppingTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaSpecMapper {
    /**
     *
     * @param tenantCode
     * @param specCode
     * @return
     */
    TeaSpecPO selectOne(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);

    /**
     *
     * @return
     */
    List<TeaSpecPO> selectList();

    /**
     *
     * @param teaSpecPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaSpecPO teaSpecPO);

    /**
     *
     * @param teaSpecPO
     * @return
     */
    int update(TeaSpecPO teaSpecPO);

    /**
     *
     * @param specCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);
}
