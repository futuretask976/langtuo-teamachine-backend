package com.langtuo.teamachine.api.model;

import java.util.Date;
import java.util.List;

public class ActStepDTO {
    /**
     *
     */
    private long stepIndex;

    /**
     *
     */
    private List<ToppingBaseRuleDTO> toppingBaseRuleList;

    public long getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(long stepIndex) {
        this.stepIndex = stepIndex;
    }

    public List<ToppingBaseRuleDTO> getToppingBaseRuleList() {
        return toppingBaseRuleList;
    }

    public void setToppingBaseRuleList(List<ToppingBaseRuleDTO> toppingBaseRuleList) {
        this.toppingBaseRuleList = toppingBaseRuleList;
    }
}
