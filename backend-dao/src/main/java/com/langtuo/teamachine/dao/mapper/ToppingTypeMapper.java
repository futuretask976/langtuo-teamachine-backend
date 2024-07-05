package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.ToppingTypePO;
import com.langtuo.teamachine.dao.query.TeaToppingTypeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface ToppingTypeMapper {
    /**
     *
     * @param tenantCode
     * @param toppingTypeCode
     * @return
     */
    ToppingTypePO selectOne(@Param("tenantCode") String tenantCode,
            @Param("toppingTypeCode") String toppingTypeCode, @Param("toppingTypeName") String toppingTypeName);

    /**
     *
     * @return
     */
    List<ToppingTypePO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<ToppingTypePO> search(TeaToppingTypeQuery query);

    /**
     *
     * @param toppingTypePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ToppingTypePO toppingTypePO);

    /**
     *
     * @param toppingTypePO
     * @return
     */
    int update(ToppingTypePO toppingTypePO);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("toppingTypeCode") String toppingTypeCode);
}
