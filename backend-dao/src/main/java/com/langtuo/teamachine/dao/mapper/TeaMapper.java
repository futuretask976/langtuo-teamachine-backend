package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.TeaPO;
import com.langtuo.teamachine.dao.po.TeaTypePO;
import com.langtuo.teamachine.dao.query.TeaQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TeaMapper {
    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    TeaPO selectOne(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode,
            @Param("teaName") String teaName);

    /**
     *
     * @return
     */
    List<TeaPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<TeaPO> search(TeaQuery query);

    /**
     *
     * @param teaPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TeaPO teaPO);

    /**
     *
     * @param teaPO
     * @return
     */
    int update(TeaPO teaPO);

    /**
     *
     * @param tenantCode
     * @param teaCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("teaCode") String teaCode);
}
