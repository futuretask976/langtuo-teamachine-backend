package com.langtuo.teamachine.dao.mapper.menu;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchHistoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@TeaMachineSQLScan
@Repository
public interface MenuDispatchOssMapper {
    /**
     *
     * @param tenantCode
     * @param init
     * @param ossPath
     * @return
     */
    MenuDispatchHistoryPO selectOne(@Param("tenantCode") String tenantCode, @Param("init") int init,
                                    @Param("ossPath") String ossPath);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MenuDispatchHistoryPO po);

    /**
     *
     * @param tenantCode
     * @param init
     * @param ossPath
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("init") int init,
            @Param("ossPath") String ossPath);
}
