package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drink.TeaTypePO;
import com.langtuo.teamachine.dao.query.drink.TeaTypeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface TeaTypeMapper {
    /**
     *
     * @param tenantCode
     * @param teaTypeCode
     * @return
     */
    TeaTypePO selectOne(@Param("tenantCode") String tenantCode, @Param("teaTypeCode") String teaTypeCode,
            @Param("teaTypeName") String teaTypeName);

    /**
     *
     * @return
     */
    List<TeaTypePO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<TeaTypePO> search(TeaTypeQuery query);

    /**
     *
     * @param teaTypePO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
    int delete(@Param("tenantCode") String tenantCode, @Param("teaTypeCode") String teaTypeCode);
}
