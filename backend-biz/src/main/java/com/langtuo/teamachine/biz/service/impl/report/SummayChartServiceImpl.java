package com.langtuo.teamachine.biz.service.impl.report;

import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.report.SummaryChartService;
import com.langtuo.teamachine.dao.accessor.SummaryChartAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class SummayChartServiceImpl implements SummaryChartService {
    @Resource
    private SummaryChartAccessor summaryChartAccessor;

    @Override
    public TeaMachineResult<List<Map<String, String>>> select4TeaAmtByDayChart(String tenantCode) {
        List<Map<String, String>> result = summaryChartAccessor.select4TeaAmtByDayChart(tenantCode);
        return TeaMachineResult.success(result);
    }

    @Override
    public TeaMachineResult<List<Map<String, String>>> select4OrgShopChart(String tenantCode) {
        List<Map<String, String>> result = summaryChartAccessor.select4OrgShopChart(tenantCode);
        return TeaMachineResult.success(result);
    }

    @Override
    public TeaMachineResult<List<Map<String, String>>> select4DeployChart(String tenantCode) {
        List<Map<String, String>> result = summaryChartAccessor.select4DeployChart(tenantCode);
        return TeaMachineResult.success(result);
    }

    @Override
    public TeaMachineResult<List<Map<String, String>>> select4ShopMachineChart(String tenantCode) {
        List<Map<String, String>> result = summaryChartAccessor.select4ShopMachineChart(tenantCode);
        return TeaMachineResult.success(result);
    }

    @Override
    public TeaMachineResult<List<Map<String, String>>> select4OrderAmtChart(String tenantCode) {
        List<Map<String, String>> result = summaryChartAccessor.select4OrderAmtChart(tenantCode);
        return TeaMachineResult.success(result);
    }

    @Override
    public TeaMachineResult<List<Map<String, String>>> select4TeaAmtChart(String tenantCode) {
        List<Map<String, String>> result = summaryChartAccessor.select4TeaAmtChart(tenantCode);
        return TeaMachineResult.success(result);
    }

    @Override
    public TeaMachineResult<List<Map<String, String>>> select4ToppingAmtChart(String tenantCode) {
        List<Map<String, String>> result = summaryChartAccessor.select4ToppingAmtChart(tenantCode);
        return TeaMachineResult.success(result);
    }

    @Override
    public TeaMachineResult<List<Map<String, String>>> select4SpecItemAmtChart(String tenantCode) {
        List<Map<String, String>> result = summaryChartAccessor.select4SpecItemAmtChart(tenantCode);
        return TeaMachineResult.success(result);
    }
}
