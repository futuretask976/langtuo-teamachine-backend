package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.drink.SpecItemPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SpecItemMapper {
    /**
     *
     * @param tenantCode
     * @param specItemCode
     * @return
     */
    SpecItemPO selectOne(@Param("tenantCode") String tenantCode, @Param("specItemCode") String specItemCode, @Param("specItemName") String specItemName);

    /**
     *
     * @return
     */
    List<SpecItemPO> selectList(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SpecItemPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(SpecItemPO po);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("specCode") String specCode);
}
