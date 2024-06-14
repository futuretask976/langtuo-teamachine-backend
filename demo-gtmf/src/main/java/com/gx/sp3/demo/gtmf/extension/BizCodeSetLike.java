package com.gx.sp3.demo.gtmf.extension;

import java.util.Set;

public interface BizCodeSetLike {
    /**
     * 获取业务身份集合
     * @return
     */
    Set<String> getBizCodeSet();

    /**
     * 设置业务身份集合
     * @param bizCodeSet
     */
    void setBizCodeSet(Set<String> bizCodeSet);
}
