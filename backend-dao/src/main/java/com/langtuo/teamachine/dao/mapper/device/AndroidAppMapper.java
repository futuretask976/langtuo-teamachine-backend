package com.langtuo.teamachine.dao.mapper.device;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.device.AndroidAppPO;
import com.langtuo.teamachine.dao.query.device.AndroidAppQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface AndroidAppMapper {
    /**
     *
     * @param version
     * @return
     */
    AndroidAppPO selectOne(@Param("version") String version);

    /**
     *
     * @return
     */
    List<AndroidAppPO> search(AndroidAppQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(AndroidAppPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(AndroidAppPO po);

    /**
     *
     * @param version
     * @return
     */
    int delete(@Param("version") String version);
}
