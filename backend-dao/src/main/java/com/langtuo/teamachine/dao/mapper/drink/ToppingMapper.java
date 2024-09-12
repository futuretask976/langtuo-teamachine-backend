package com.langtuo.teamachine.dao.mapper.drink;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.query.drink.ToppingQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface ToppingMapper {
    /**
     *
     * @param tenantCode
     * @param toppingCode
     * @return
     */
    ToppingPO selectOne(@Param("tenantCode") String tenantCode, @Param("toppingCode") String toppingCode,
            @Param("toppingName") String toppingName);

    /**
     *
     * @return
     */
    List<ToppingPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<ToppingPO> search(ToppingQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ToppingPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(ToppingPO po);

    /**
     *
     * @param toppingCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("toppingCode") String toppingCode);

    /**
     *
     * @param tenantCode
     * @param toppingTypeCode
     * @return
     */
    int countByToppingTypeCode(@Param("tenantCode") String tenantCode, @Param("toppingTypeCode") String toppingTypeCode);
}
