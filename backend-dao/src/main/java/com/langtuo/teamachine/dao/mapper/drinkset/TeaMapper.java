package com.langtuo.teamachine.dao.mapper.drinkset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drinkset.TeaPO;
import com.langtuo.teamachine.dao.query.drinkset.TeaQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
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
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
