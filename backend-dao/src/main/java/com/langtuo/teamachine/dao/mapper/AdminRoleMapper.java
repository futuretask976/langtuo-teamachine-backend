package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.AdminRolePO;
import com.langtuo.teamachine.dao.query.AdminRoleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface AdminRoleMapper {
    /**
     *
     * @param tenantCode
     * @param roleCode
     * @return
     */
    AdminRolePO selectOne(@Param("tenantCode") String tenantCode, @Param("roleCode") String roleCode);

    /**
     *
     * @return
     */
    List<AdminRolePO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @param query
     * @return
     */
    List<AdminRolePO> search(AdminRoleQuery query);

    /**
     *
     * @param adminRolePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(AdminRolePO adminRolePO);

    /**
     *
     * @param adminRolePO
     * @return
     */
    int update(AdminRolePO adminRolePO);

    /**
     *
     * @param tenantCode
     * @param roleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("roleCode") String roleCode);
}
