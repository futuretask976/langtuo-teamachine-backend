package com.langtuo.teamachine.dao.mapper.shop;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.query.shop.ShopGroupQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface ShopGroupMapper {
    /**
     *
     * @param tenantCode
     * @return
     */
    ShopGroupPO selectOne(@Param("tenantCode") String tenantCode, @Param("shopGroupCode") String shopGroupCode,
            @Param("shopGroupName") String shopGroupName);

    /**
     *
     * @return
     */
    List<ShopGroupPO> selectList(String tenantCode);

    /**
     *
     * @return
     */
    List<ShopGroupPO> selectListByOrgNameList(@Param("tenantCode") String tenantCode,
            @Param("orgNameList") List<String> orgNameList);

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
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
