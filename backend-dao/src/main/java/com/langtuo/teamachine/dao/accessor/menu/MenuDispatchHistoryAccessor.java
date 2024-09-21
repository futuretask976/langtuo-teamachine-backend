package com.langtuo.teamachine.dao.accessor.menu;

import com.langtuo.teamachine.dao.mapper.menu.MenuDispatchHistoryMapper;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchHistoryPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuDispatchHistoryAccessor {
    @Resource
    private MenuDispatchHistoryMapper mapper;

    public MenuDispatchHistoryPO getByFileName(String tenantCode, int init, String fileName) {
        MenuDispatchHistoryPO menuDispatchHistoryPO = mapper.selectOne(tenantCode, init, fileName);
        return menuDispatchHistoryPO;
    }

    public int insert(MenuDispatchHistoryPO po) {
        int inserted = mapper.insert(po);
        return inserted;
    }

    public int delete(String tenantCode, int init, String fileName) {
        int deleted = mapper.delete(tenantCode, init, fileName);
        return deleted;
    }

    public int deleteByFileNameList(String tenantCode, int init, List<String> fileNameList) {
        int deleted = mapper.deleteByFileNameList(tenantCode, init, fileNameList);
        return deleted;
    }
}
