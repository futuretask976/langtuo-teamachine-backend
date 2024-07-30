package com.langtuo.teamachine.web.security.service;

import com.langtuo.teamachine.api.model.userset.AdminDTO;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.userset.AdminMgtService;
import com.langtuo.teamachine.web.security.model.TeaMachineUserDetails;
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

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        String tenantCode = request.getParameter("tenantCode");

        LangTuoResult<AdminDTO> result = adminMgtService.get(tenantCode, userName);
        if (result == null && !result.isSuccess() || result.getModel() == null) {
            return null;
        }
        AdminDTO adminDTO = result.getModel();

        TeaMachineUserDetails teaMachineUserDetails = new TeaMachineUserDetails();
        return teaMachineUserDetails;
    }

    private TeaMachineUserDetails convert(AdminDTO dto) {
        if (dto == null) {
            return null;
        }

        TeaMachineUserDetails userDetails = new TeaMachineUserDetails();
        return null;
    }
}
