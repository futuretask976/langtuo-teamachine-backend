package com.gx.sp3.demo.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Deploy the war under tomcat webapp, the tomcat path is /gx
 * so here root path only need to be set as /, not /gx
 */
@RestController
@Slf4j
public class TestRestfulController {
    /**
     * Access this method by http://localhost:8080/gxsp3demo/test001
     * @return
     */
    @GetMapping(value = "/test001")
    public String test001() {
        log.info("~~~ TestRestfulController#test001 entering");
        try {
            System.out.println("!!! TestRestfulController#test001 entering");
            return "/test001 success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.toString();
        }
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/test002
     * @return
     */
    @GetMapping(value = "/test002")
    public String test002() {
        try {
            System.out.println("!!! TestRestfulController#test002 entering");
            return "/test002 success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.toString();
        }
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/test003
     * @return
     */
    @GetMapping(value = "/test003")
    public String test003() {
        try {
            System.out.println("!!! TestRestfulController#test003 entering");
            return "/test003 success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.toString();
        }
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/test004
     * @return
     */
    @GetMapping(value = "/test004")
    public String test004() {
        try {
            System.out.println("!!! TestRestfulController#test004 entering");
            return "/test004 success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.toString();
        }
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/test001?param=123
     * @param param
     * @return
     */
    @GetMapping(value = "/test101")
    public String test101(@RequestParam("param") String param) {
        try {
            System.out.println("!!! TestRestfulController#test101 entering");
            return "/test101 success: " + param;
        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.toString();
        }
    }

    /**
     * Access this method by http://localhost:8080/gxsp3demo/test102?param1=123&param2=456
     * @param param1
     * @param param2
     * @return
     */
    @GetMapping(value = "/test102")
    public String test102(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
        try {
            System.out.println("!!! TestRestfulController#test102 entering");
            return "/test102 success: " + param1 + ", " + param2;
        } catch (Exception e) {
            e.printStackTrace();
            return "fail: " + e.toString();
        }
    }
}
