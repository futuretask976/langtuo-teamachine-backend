package com.langtuo.teamachine.web.security.service;

import com.langtuo.teamachine.api.model.deviceset.MachineDTO;
import com.langtuo.teamachine.api.model.userset.AdminDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.deviceset.MachineMgtService;
import com.langtuo.teamachine.api.service.userset.AdminMgtService;
import com.langtuo.teamachine.web.security.model.AdminDetails;
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

    @Resource
    private AdminMgtService adminMgtService;

    @Resource
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

        AdminDetails adminDetails = new AdminDetails();
        return adminDetails;
    }

    private UserDetails getAdminUserDetails(String userName) {
        String tenantCode = request.getParameter("tenantCode");

        LangTuoResult<AdminDTO> result = adminMgtService.get(tenantCode, userName);
        if (result == null && !result.isSuccess() || result.getModel() == null) {
            return null;
        }
        AdminDTO adminDTO = result.getModel();

        AdminDetails adminDetails = new AdminDetails();
        return adminDetails;
    }

    private AdminDetails convert(AdminDTO dto) {
        if (dto == null) {
            return null;
        }

        AdminDetails userDetails = new AdminDetails();
        return null;
    }

    private AdminDetails convert(MachineDTO dto) {
        if (dto == null) {
            return null;
        }

        AdminDetails userDetails = new AdminDetails();
        return null;
    }
}
