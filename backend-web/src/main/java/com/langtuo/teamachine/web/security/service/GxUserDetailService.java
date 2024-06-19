package com.langtuo.teamachine.web.security.service;

import com.langtuo.teamachine.web.security.model.GxUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class GxUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.printf("!!! GxUserDetailService#loadUserByUsername username=%s\n", username);
        GxUserDetails gxUserDetails = new GxUserDetails();
        return gxUserDetails;
    }
}
