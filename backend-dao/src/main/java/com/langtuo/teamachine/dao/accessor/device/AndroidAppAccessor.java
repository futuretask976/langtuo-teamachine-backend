package com.langtuo.teamachine.dao.accessor.device;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.device.AndroidAppMapper;
import com.langtuo.teamachine.dao.po.device.AndroidAppPO;
import com.langtuo.teamachine.dao.query.device.AndroidAppQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AndroidAppAccessor {
    @Resource
    private AndroidAppMapper mapper;

    /**
     *
     * @param version
     * @return
     */
    public AndroidAppPO selectOne(String version) {
        AndroidAppPO po = mapper.selectOne(version);
        return po;
    }

    public PageInfo<AndroidAppPO> search(String version, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        AndroidAppQuery query = new AndroidAppQuery();
        query.setVersion(version);
        List<AndroidAppPO> poList = mapper.search(query);

        PageInfo<AndroidAppPO> pageInfo = new PageInfo(poList);
        return pageInfo;
    }

    public int insert(AndroidAppPO po) {
        int inserted = mapper.insert(po);
        return inserted;
    }

    public int update(AndroidAppPO po) {
        int updated = mapper.update(po);
        return updated;
    }

    public int deleteByVersion(String version) {
        int deleted = mapper.delete(version);
        return deleted;
    }
}
