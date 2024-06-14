package com.gx.sp3.demo.biz.gtmf.impl.business.test;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.activity.TestRenderBlockActivity;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.activity.TestSubmitBlockActivity;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.transfer.RenderRequestDTO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.transfer.SubmitRequestDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 该类目前可不用，报错如下：
 *     NoSuchBeanDefinitionException: No qualifying bean of type 'com.gx.sp3.demo.dao.mapper.HotelGuestMapper' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@jakarta.annotation.Resource(shareable=true, lookup="", name="", description="", authenticationType=CONTAINER, type=java.lang.Object.class, mappedName="")}
 */
public class TestDemo {
    public static void main(String args[]) {
        System.out.println("TestDemo#main开始被执行");
        try {
            testBySpring();
        } catch (Exception e) {
            System.out.println("Testor#main发生异常：" + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Testor#main结束被执行");
    }

    public static void testBySpring() {
        ApplicationContext context = startBySpring();

        System.out.println("TestDemo#testBySpring begin testRenderBlockActivity");
        TestRenderBlockActivity testRenderBlockActivity =
                (TestRenderBlockActivity) context.getBean(TestRenderBlockActivity.class);
        RenderRequestDTO renderRequestDTO = new RenderRequestDTO();
        renderRequestDTO.setUserId(1234567890L);
        testRenderBlockActivity.render(renderRequestDTO);

        System.out.println("TestDemo#testBySpring begin testSubmitBlockActivity");
        TestSubmitBlockActivity testSubmitBlockActivity =
                (TestSubmitBlockActivity) context.getBean(TestSubmitBlockActivity.class);
        SubmitRequestDTO submitRequestDTO = new SubmitRequestDTO();
        submitRequestDTO.setUserId(1234567890L);
        testSubmitBlockActivity.submit(submitRequestDTO);

        System.out.println("Testor#testBySpring begin stop");
        stopBySpring(context);
    }
    
    public static ApplicationContext startBySpring() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
        if (context == null) {
            throw new RuntimeException("TestDemo#startBySpring初始化Spring失败");
        }
        return context;
    }

    public static void stopBySpring(ApplicationContext context) {
        if (context == null) {
            return;
        }
        if (context instanceof ClassPathXmlApplicationContext) {
            ((ClassPathXmlApplicationContext) context).close();
        } else {
            System.out.println("TestDemo#stopBySpring上下文参数异常");
        }
    }
}
