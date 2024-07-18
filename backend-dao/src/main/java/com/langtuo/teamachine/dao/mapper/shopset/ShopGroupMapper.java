package com.langtuo.teamachine.dao.mapper.shopset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.shopset.ShopGroupPO;
import com.langtuo.teamachine.dao.query.shopset.ShopGroupQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface ShopGroupMapper {
    /**
     *
     * @param tenantCode
     * @return
     */
    ShopGroupPO selectOne(@Param("tenantCode") String tenantCode, @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @return
     */
    List<ShopGroupPO> selectList(String tenantCode);

    /**
     *
     * @param query
     * @return
     */
    List<ShopGroupPO> search(ShopGroupQuery query);

    /**
     *
     * @param po
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ShopGroupPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(ShopGroupPO po);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("shopGroupCode") String shopGroupCode);
}
