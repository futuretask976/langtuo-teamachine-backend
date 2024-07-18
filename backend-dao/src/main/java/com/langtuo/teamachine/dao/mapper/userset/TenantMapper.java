package com.langtuo.teamachine.dao.mapper.userset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.userset.TenantPO;
import com.langtuo.teamachine.dao.query.userset.TenantQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface TenantMapper {
    /**
     *
     * @param tenantCode
     * @return
     */
    TenantPO selectOne(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<TenantPO> selectList();

    /**
     *
     * @return
     */
    List<TenantPO> search(TenantQuery tenantQuery);

    /**
     *
     * @param po
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(TenantPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(TenantPO po);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(String tenantCode);
}
