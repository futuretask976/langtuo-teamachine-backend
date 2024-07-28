package com.langtuo.teamachine.dao.mapper.userset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.userset.RoleActRelPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface RoleActRelMapper {
    /**
     *
     * @param tenantCode
     * @param roleCode
     * @return
     */
    RoleActRelPO selectOne(@Param("tenantCode") String tenantCode, @Param("roleCode") String roleCode,
            @Param("permitActCode") String permitActCode);

    /**
     *
     * @return
     */
    List<RoleActRelPO> selectList(@Param("tenantCode") String tenantCode, @Param("roleCode") String roleCode);

    /**
     *
     * @param adminRoleActRelPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(RoleActRelPO po);

    /**
     *
     * @param adminRoleActRelPO
     * @return
     */
    int update(RoleActRelPO po);

    /**
     *
     * @param tenantCode
     * @param roleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("roleCode") String roleCode);
}
