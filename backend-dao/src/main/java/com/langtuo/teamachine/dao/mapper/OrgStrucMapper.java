package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.OrgStrucPO;
import com.langtuo.teamachine.dao.po.TenantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface OrgStrucMapper {
    /**
     *
     * @param tenantCode
     * @param orgName
     * @return
     */
    OrgStrucPO selectOne(@Param("tenantCode") String tenantCode, @Param("orgName") String orgName);

    /**
     *
     * @return
     */
    List<OrgStrucPO> selectList();

    /**
     *
     * @param orgStrucPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrgStrucPO orgStrucPO);

    /**
     *
     * @param orgStrucPO
     * @return
     */
    int update(OrgStrucPO orgStrucPO);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("orgName") String orgName);
}
