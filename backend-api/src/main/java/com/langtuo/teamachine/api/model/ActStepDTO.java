package com.langtuo.teamachine.api.model;

import lombok.Data;

import java.util.List;

@Data
public class ActStepDTO {
    /**
     *
     */
    private long stepIndex;

    /**
     *
     */
    private List<ToppingBaseRuleDTO> toppingBaseRuleList;
}
