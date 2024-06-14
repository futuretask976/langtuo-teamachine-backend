package com.gx.sp3.demo.dao.mapper.langtuo;

import com.gx.sp3.demo.dao.annotation.MySQLScan;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineTeaPojo;
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
