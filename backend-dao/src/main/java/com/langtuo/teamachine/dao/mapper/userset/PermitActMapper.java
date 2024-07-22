package com.langtuo.teamachine.dao.mapper.userset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.userset.PermitActPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface PermitActMapper {
    /**
     *
     * @param permActGroupCode
     * @return
     */
    PermitActPO selectOne(@Param("permActGroupCode") String permActGroupCode);

    /**
     *
     * @return
     */
    List<PermitActPO> selectList();

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(PermitActPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(PermitActPO po);

    /**
     *
     * @param permActGroupCode
     * @return
     */
    int delete(String permActGroupCode);
}
