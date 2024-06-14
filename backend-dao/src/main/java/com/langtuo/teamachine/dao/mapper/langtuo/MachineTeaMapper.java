package com.langtuo.teamachine.dao.mapper.langtuo;

import com.langtuo.teamachine.dao.annotation.MySQLScan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface MachineTeaMapper {
    /**
     *
     * @param teaCode
     * @return
     */
    MachineTeaPojo get(@Param("machineCode") String machineCode, @Param("teaCode") String teaCode);

    /**
     * 查询所有用户
     * @return
     */
    List<MachineTeaPojo> list();

    /**
     *
     * @param menu
     * @return
     */
    int insert(MachineTeaPojo menu);

    /**
     *
     * @param menu
     * @return
     */
    int update(MachineTeaPojo menu);

    /**
     *
     * @param teaCode
     * @return
     */
    int delete(String teaCode);
}
