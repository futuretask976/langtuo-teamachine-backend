package com.langtuo.teamachine.dao.mapper.userset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.userset.AdminRoleActRelPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface AdminRoleActRelMapper {
    /**
     *
     * @param tenantCode
     * @param roleCode
     * @return
     */
    AdminRoleActRelPO selectOne(@Param("tenantCode") String tenantCode, @Param("roleCode") String roleCode,
            @Param("permitActCode") String permitActCode);

    /**
     *
     * @return
     */
    List<AdminRoleActRelPO> selectList(@Param("tenantCode") String tenantCode, @Param("roleCode") String roleCode);

    /**
     *
     * @param adminRoleActRelPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(AdminRoleActRelPO po);

    /**
     *
     * @param adminRoleActRelPO
     * @return
     */
    int update(AdminRoleActRelPO po);

    /**
     *
     * @param tenantCode
     * @param roleCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("roleCode") String roleCode);
}
