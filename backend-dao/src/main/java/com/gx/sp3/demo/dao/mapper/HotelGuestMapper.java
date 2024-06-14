package com.gx.sp3.demo.dao.mapper;

import com.gx.sp3.demo.dao.annotation.MySQLScan;
import com.gx.sp3.demo.dao.pojo.HotelGuestPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface HotelGuestMapper {
    /**
     * 查询一个用户
     * @param id
     * @return
     */
    HotelGuestPojo get(@Param("id") int id);

    /**
     * 查询所有用户
     * @return
     */
    List<HotelGuestPojo> list();

    /**
     * 添加一个用户
     * @param user
     * @return
     */
    int insert(HotelGuestPojo user);

    /**
     * 修改一个用户
     * @param user
     * @return
     */
    int update(HotelGuestPojo user);

    /**
     * 删除一个用户
     * @param id
     * @return
     */
    int delete(int id);
}
