package com.langtuo.teamachine.dao.mapper.menu;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuDispatchMapper {
    /**
     *
     * @return
     */
    List<MenuDispatchPO> selectList(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode,
            @Param("shopGroupCodeList") List<String> shopGroupCodeList);

    /**
     *
     * @return
     */
    List<MenuDispatchPO> selectListByShopGroupCode(@Param("tenantCode") String tenantCode,
            @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @param menuDispatchPO
     * @return
     */
    int insert(MenuDispatchPO menuDispatchPO);

    /**
     *
     * @param menuDispatchPO
     * @return
     */
    int update(MenuDispatchPO menuDispatchPO);

    /**
     *
     * @param tenantCode
     * @param menuCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode,
            @Param("shopGroupCodeList") List<String> shopGroupCodeList);
}
