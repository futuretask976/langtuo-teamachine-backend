package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.product.general;

import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.context.RenderBlogPageContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.base.BaseGxBusinessTemplate;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.customized.GxBusinessRenderBlogPageExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogPageDO;
import com.gx.sp3.demo.gtmf.annotation.BusinessDef;
import com.gx.sp3.demo.gtmf.exception.GTmfException;
import org.assertj.core.util.Lists;

import java.util.Date;
import java.util.List;

/**
 * @author miya
 */
@BusinessDef(code = BusinessGeneralProduct.CODE, desc = "通用业务产品")
public class BusinessGeneralProduct extends BaseGxBusinessTemplate {
    public static final String CODE = "GxReactBusinessGeneralProduct";

    @Override
    public GxBusinessRenderBlogPageExtPoints getGxBusinessRenderBlogPageExtPoints() {
        return new GxBusinessRenderBlogPageExtPoints() {
            @Override
            public List<BlogBlockDO> fetchBlogBlockList(RenderBlogPageContext context) throws GTmfException {
                System.out.println("GxBusinessRenderBlogPageExtPoints#fetchBlogBlockList entering");
                return mockFetchBlogBlockList();
            }

            @Override
            public BlogPageDO fetchBlogPage(RenderBlogPageContext context) throws GTmfException {
                System.out.println("GxBusinessRenderBlogPageExtPoints#fetchBlogPage entering");
                return mockFetchBlogPage();
            }

            private List<BlogBlockDO> mockFetchBlogBlockList() {
                List<BlogBlockDO> list = Lists.newArrayList();
                list.add(BlogBlockDO.of(1, "images/image_1.jpg", new Date(), "管理员",
                        9L, "在家撒娇，想要更多吃的"));
                list.add(BlogBlockDO.of(2, "images/image_2.jpg", new Date(), "管理员",
                        8L, "坐船游小河，阳光棒棒哒"));
                list.add(BlogBlockDO.of(3, "images/image_3.jpg", new Date(), "管理员",
                        7L, "有点生气，怨气积累中"));
                list.add(BlogBlockDO.of(3, "images/image_4.jpg", new Date(), "管理员",
                        6L, "嘿嘿嘿嘿嘿"));
                list.add(BlogBlockDO.of(3, "images/image_5.jpg", new Date(), "管理员",
                        5L, "呜呜呜呜呜"));
                list.add(BlogBlockDO.of(3, "images/image_6.jpg", new Date(), "管理员",
                        4L, "有点小郁闷，纠结中"));
                list.add(BlogBlockDO.of(3, "images/image_7.jpg", new Date(), "管理员",
                        3L, "快乐每一天"));
                return list;
            }

            private BlogPageDO mockFetchBlogPage() {
                BlogPageDO blogPageDO = new BlogPageDO();
                blogPageDO.setTitle("阅读她的博客");
                blogPageDO.setDesc("博客是细腻而深情的心情记录，字里行间流淌着彼时彼刻的情感，进入了一个既沉痛又充满希望的心灵世界，见证了从阴霾走向光明的内心蜕变。");
                blogPageDO.setBlogBlockDOList(mockFetchBlogBlockList());
                return blogPageDO;
            }
        };
    }
}
