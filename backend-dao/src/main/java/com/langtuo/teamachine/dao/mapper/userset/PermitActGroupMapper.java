package com.langtuo.teamachine.dao.mapper.userset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.userset.PermitActGroupPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface PermitActGroupMapper {
    /**
     *
     * @param permitActGroupCode
     * @return
     */
    PermitActGroupPO selectOne(@Param("permitActGroupCode") String permitActGroupCode);

    /**
     *
     * @return
     */
    List<PermitActGroupPO> selectList();

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(PermitActGroupPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(PermitActGroupPO po);

    /**
     *
     * @param permitActGroupCode
     * @return
     */
    int delete(String permitActGroupCode);
}
