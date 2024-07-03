package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.ShopPO;
import com.langtuo.teamachine.dao.po.TenantPO;
import com.langtuo.teamachine.dao.query.ShopQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface ShopMapper {
    /**
     *
     * @param tenantCode
     * @return
     */
    ShopPO selectOne(@Param("tenantCode") String tenantCode, @Param("shopCode") String shopCode,
            @Param("shopName") String shopName);

    /**
     *
     * @return
     */
    List<ShopPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @param query
     * @return
     */
    List<ShopPO> search(ShopQuery query);

    /**
     *
     * @param shopPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ShopPO shopPO);

    /**
     *
     * @param shopPO
     * @return
     */
    int update(ShopPO shopPO);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("shopCode") String shopCode);
}
