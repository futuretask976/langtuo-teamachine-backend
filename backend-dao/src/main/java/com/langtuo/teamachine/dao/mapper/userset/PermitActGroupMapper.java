package com.langtuo.teamachine.dao.mapper.userset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.userset.PermitActGroupPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
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
     * @param permitActGroupPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(PermitActGroupPO permitActGroupPO);

    /**
     *
     * @param permitActGroupPO
     * @return
     */
    int update(PermitActGroupPO permitActGroupPO);

    /**
     *
     * @param permitActGroupCode
     * @return
     */
    int delete(String permitActGroupCode);
}
