package com.langtuo.teamachine.dao.mapper.userset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.userset.OrgPO;
import com.langtuo.teamachine.dao.query.userset.OrgStrucQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OrgMapper {
    /**
     *
     * @param tenantCode
     * @param orgName
     * @return
     */
    OrgPO selectOne(@Param("tenantCode") String tenantCode, @Param("orgName") String orgName);

    /**
     *
     * @return
     */
    List<OrgPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<OrgPO> search(OrgStrucQuery orgStrucQuery);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrgPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(OrgPO po);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("orgName") String orgName);
}
