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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TeaMachineDetailService implements UserDetailsService {
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
        String from = request.getParameter("from");

        UserDetails userDetails = null;
        if ("MACHINE".equals(from)) {
            userDetails = getMachineUserDetails(userName);
        } else {
            userDetails = getAdminUserDetails(userName);
        }
        return userDetails;
    }

    private UserDetails getMachineUserDetails(String userName) {
        String tenantCode = request.getParameter("tenantCode");
        String machineCode = userName;

        LangTuoResult<MachineDTO> result = machineMgtService.get(tenantCode, machineCode);
        if (result == null && !result.isSuccess() || result.getModel() == null) {
            return null;
        }
        MachineDTO machineDTO = result.getModel();

        MachineDetails adminDetails = new MachineDetails(machineDTO);
        return adminDetails;
    }

    private UserDetails getAdminUserDetails(String userName) {
        String tenantCode = request.getParameter("tenantCode");

        LangTuoResult<AdminDTO> adminResult = adminMgtService.get(tenantCode, userName);
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
