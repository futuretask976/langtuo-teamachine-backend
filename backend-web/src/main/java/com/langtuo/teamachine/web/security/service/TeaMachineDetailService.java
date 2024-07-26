package com.langtuo.teamachine.web.security.service;

import com.langtuo.teamachine.web.security.model.TeaMachineUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TeaMachineDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        TeaMachineUserDetails teaMachineUserDetails = new TeaMachineUserDetails();
        return teaMachineUserDetails;
    }
}
