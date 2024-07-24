package com.langtuo.teamachine.api.request.ruleset;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OpenRuleToppingPutRequest {
    /**
     *
     */
    private String toppingCode;

    /**
     *
     */
    private int flushTime;

    /**
     *
     */
    private int flushWeight;
}
