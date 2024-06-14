package com.gx.sp3.demo.gtmf.config.diamond;

import java.io.IOException;

/**
 * Diamond的Mock类
 * @author miya
 */
public class Diamond {
    /**
     *
     * @param dataId
     * @param dataGroup
     * @param timeout
     * @return
     */
    public static String getConfig(String dataId, String dataGroup, int timeout) {
        String config = null;
        if ("biz_priority_config_data".equals(dataId) && "DEFAULT_GROUP".equals(dataGroup)) {
            config = "{\n" +
                    "\t\"alipayH5Client\": {\n" +
                    "\t\t\"BaseBlockRenderBusinessTemplate.getTestBusinessRenderBlockTextExtPoints\": [\"BusinessAProduct\", \"BusinessBProduct\"],\n" +
                    "\t\t\"BaseBlockRenderBusinessTemplate.getTestBusinessRenderBlockLinkExtPoints\": [\"BusinessAProduct\", \"BusinessBProduct\"],\n" +
                    "\t\t\"BaseBlockRenderBusinessTemplate.getTestBusinessSubmitBlockInputExtPoints\": [\"BusinessBProduct\"],\n" +
                    "\t\t\"BaseBlockRenderBusinessTemplate.getTestBusinessSubmitBlockSelectExtPoints\": [\"BusinessBProduct\"],\n" +
                    "\t\t\"BaseGxBusinessTemplate.getGxBusinessRenderBlogPageExtPoints\": [\"GxReactBusinessGeneralProduct\"]\n" +
                    "\t}\n" +
                    "}";
        }
        System.out.println("Diamond#getConfig config=" + config);
        return config;
    }

    /**
     *
     * @param dataId
     * @param dataGroup
     * @param managerListenerAdapter
     */
    public static void addListener(String dataId, String dataGroup, ManagerListenerAdapter managerListenerAdapter)
            throws IOException {
        // TODO 产品化前需要实现
        return;
    }
}
