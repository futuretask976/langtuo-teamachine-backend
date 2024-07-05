package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaTypeMapper {
    /**
     *
     * @param tenantCode
     * @param teaTypeCode
     * @return
     */
    TeaTypePO selectOne(@Param("tenantCode") String tenantCode, @Param("teaTypeCode") String teaTypeCode);

    /**
     *
     * @return
     */
    List<TeaTypePO> selectList();

    /**
     *
     * @param teaTypePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaTypePO teaTypePO);

    /**
     *
     * @param teaTypePO
     * @return
     */
    int update(TeaTypePO teaTypePO);

    /**
     *
     * @param tenantCode
     * @param teaTypeCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("toppingTypeCode") String teaTypeCode);
}
