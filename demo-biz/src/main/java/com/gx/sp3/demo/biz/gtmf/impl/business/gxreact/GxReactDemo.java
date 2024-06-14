package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact;

import com.gx.sp3.demo.api.request.RenderBlogPageRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.activity.RenderBlogPageActivity;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogPageDO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 该类目前可不用，报错如下：
 *     NoSuchBeanDefinitionException: No qualifying bean of type 'com.gx.sp3.demo.dao.mapper.HotelGuestMapper' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@jakarta.annotation.Resource(shareable=true, lookup="", name="", description="", authenticationType=CONTAINER, type=java.lang.Object.class, mappedName="")}
 */
public class GxReactDemo {
    public static void main(String args[]) {
        System.out.println("Testor@main开始被执行");
        try {
            testBySpring();
        } catch (Exception e) {
            System.out.println("GxReactDemo@main发生异常：" + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("GxReactDemo@main结束被执行");
    }

    public static void testBySpring() {
        ApplicationContext context = startBySpring();

        System.out.println("GxReactDemo#testBySpring begin testRenderBlockActivity");
        RenderBlogPageRequest renderBlogPageRequest = new RenderBlogPageRequest();
        renderBlogPageRequest.setUserId(1234567890L);
        renderBlogPageRequest.setModelId(1234L);

        RenderBlogPageActivity renderBlogPageActivity =
                (RenderBlogPageActivity) context.getBean(RenderBlogPageActivity.class);
        BlogPageDO blogPageDO = renderBlogPageActivity.renderBlogPage(renderBlogPageRequest);
        System.out.println("GxReactDemo#testBySpring blogPageDO=" + blogPageDO);

        System.out.println("GxReactDemo#testBySpring begin stop");
        stopBySpring(context);
    }

    public static ApplicationContext startBySpring() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
        if (context == null) {
            throw new RuntimeException("GxReactDemo@startBySpring初始化Spring失败");
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
            System.out.println("GxReactDemo@stopBySpring上下文参数异常");
        }
    }
}
