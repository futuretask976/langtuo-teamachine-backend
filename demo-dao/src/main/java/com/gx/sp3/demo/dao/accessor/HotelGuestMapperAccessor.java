package com.gx.sp3.demo.dao.accessor;

import com.gx.sp3.demo.dao.mapper.HotelGuestMapper;
import com.gx.sp3.demo.dao.pojo.HotelGuestPojo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class HotelGuestMapperAccessor {
    @Resource
    private HotelGuestMapper mapper;

    public HotelGuestPojo getOne(@Param("id") int id) {
        return mapper.get(id);
    }
}
