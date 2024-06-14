package com.gx.sp3.demo.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.gx.sp3.demo.dao.accessor.HotelGuestMapperAccessor;
import com.gx.sp3.demo.dao.pojo.HotelGuestPojo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/database")
public class DatabaseRestfulController {
    @Resource
    private HotelGuestMapperAccessor hotelGuestMapperAccessor;

    /**
     * Access this method by http://localhost:8080/gxsp3demo/database/hotel2000w/pick?id=70844
     * @return
     */
    @GetMapping(value = "/hotel2000w/pick")
    public String hotel2000wPick(@RequestParam("id") String id) {
        try {
            HotelGuestPojo hotelGuestPojo = hotelGuestMapperAccessor.getOne(Integer.valueOf(id));

            System.out.println("DatabaseController#hotel2000wPick entering");
            return "success: hotel2000wPick=" + JSONObject.toJSONString(hotelGuestPojo);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.toString();
        }
    }
}
