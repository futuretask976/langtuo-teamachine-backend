package com.langtuo.teamachine.dao.mapper.userset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.userset.AdminPO;
import com.langtuo.teamachine.dao.query.userset.AdminQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface AdminMapper {
    /**
     *
     * @param tenantCode
     * @param loginName
     * @return
     */
    AdminPO selectOne(@Param("tenantCode") String tenantCode, @Param("loginName") String loginName);

    /**
     *
     * @return
     */
    List<AdminPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @param query
     * @return
     */
    List<AdminPO> search(AdminQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(AdminPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(AdminPO po);

    /**
     *
     * @param tenantCode
     * @param loginName
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("loginName") String loginName);
}
