package com.gx.sp3.demo.gtmf.extension;

public interface BizCodeLike {
    /**
     * 获取所属的业务身份
     * @return
     */
    String getBizCode();

    /**
     * 设置所属的业务身份
     * @param bizCode
     */
    void setBizCode(String bizCode);
}
