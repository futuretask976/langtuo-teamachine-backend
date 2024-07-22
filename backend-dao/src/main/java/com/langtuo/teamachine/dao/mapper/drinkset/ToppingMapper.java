package com.langtuo.teamachine.dao.mapper.drinkset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.drinkset.ToppingPO;
import com.langtuo.teamachine.dao.query.drinkset.ToppingQuery;
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
     * @param toppingPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ToppingPO toppingPO);

    /**
     *
     * @param toppingPO
     * @return
     */
    int update(ToppingPO toppingPO);

    /**
     *
     * @param toppingCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("toppingCode") String toppingCode);
}
