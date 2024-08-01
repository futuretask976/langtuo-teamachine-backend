package com.langtuo.teamachine.web.security.service;

import com.langtuo.teamachine.api.model.deviceset.MachineDTO;
import com.langtuo.teamachine.api.model.userset.AdminDTO;
import com.langtuo.teamachine.api.model.userset.RoleDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.deviceset.MachineMgtService;
import com.langtuo.teamachine.api.service.userset.AdminMgtService;
import com.langtuo.teamachine.api.service.userset.RoleMgtService;
import com.langtuo.teamachine.web.security.model.AdminDetails;
import com.langtuo.teamachine.web.security.model.MachineDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TeaMachineUserDetailService implements UserDetailsService {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AdminMgtService adminMgtService;

    @Autowired
    private RoleMgtService roleMgtService;

    @Autowired
    private MachineMgtService machineMgtService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 会调用这里的是登录页面，或者没有经过认证，所以可以特殊处理
        String tenantCode = request.getParameter("tenantCode");
        if (StringUtils.isBlank(tenantCode)) {
            throw new IllegalArgumentException("tenantCode is blank");
        }
        String from = request.getParameter("from");
        if ("MACHINE".equals(from)) {
            return loadMachineUserDetails(tenantCode, userName);
        } else {
            return loadAdminUserDetails(tenantCode, userName);
        }
    }

    public UserDetails loadMachineUserDetails(String tenantCode, String machineCode) {
        LangTuoResult<MachineDTO> result = machineMgtService.get(tenantCode, machineCode);
        if (result == null && !result.isSuccess() || result.getModel() == null) {
            return null;
        }
        MachineDTO machineDTO = result.getModel();

        MachineDetails adminDetails = new MachineDetails(machineDTO);
        return adminDetails;
    }

    public UserDetails loadAdminUserDetails(String tenantCode, String loginName) {
        LangTuoResult<AdminDTO> adminResult = adminMgtService.get(tenantCode, loginName);
        if (adminResult == null && !adminResult.isSuccess() || adminResult.getModel() == null) {
            return null;
        }
        AdminDTO adminDTO = adminResult.getModel();

        LangTuoResult<RoleDTO> roleResult = roleMgtService.get(tenantCode, adminDTO.getRoleCode());
        if (roleResult == null && !roleResult.isSuccess() || roleResult.getModel() == null) {
            return null;
        }
        RoleDTO roleDTO = roleResult.getModel();


        AdminDetails adminDetails = new AdminDetails(adminDTO, roleDTO);
        return adminDetails;
    }
}
