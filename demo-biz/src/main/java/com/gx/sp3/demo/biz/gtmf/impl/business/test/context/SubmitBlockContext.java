package com.gx.sp3.demo.biz.gtmf.impl.business.test.context;

import com.gx.sp3.demo.gtmf.context.GTmfContext;
import com.gx.sp3.demo.gtmf.param.RequestSource;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author miya
 */
public class SubmitBlockContext implements GTmfContext {
    @Getter
    @Setter
    private long userId;

    @Getter
    @Setter
    private RequestSource requestSource;

    @Getter
    @Setter
    private Map<String, Object> extMap = new HashMap<>();

    @Getter
    @Setter
    private Map<String, Object> extraParams = new HashMap<>();

    @Getter
    @Setter
    private String reqSource;

    @Getter
    @Setter
    private Set<String> bizCodeSet;

    @Override
    public String toMonitorString() {
        return String.format("RenderBlockContext{source=%s}",
                requestSource);
    }

    /**
     *
     * @param code
     * @param defaultValue
     * @param <T>
     * @return
     */
    public <T> T getExtByCode(String code, T defaultValue){
        return (T)extMap.getOrDefault(code, defaultValue);
    }

    /**
     *
     * @param code
     * @param value
     */
    public void putExtByCode(String code, Object value){
        extMap.put(code, value);
    }
}
