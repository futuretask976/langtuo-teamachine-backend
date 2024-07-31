package com.langtuo.teamachine.web.security.model;

import com.langtuo.teamachine.api.model.deviceset.MachineDTO;
import com.langtuo.teamachine.api.model.userset.AdminDTO;
import org.assertj.core.util.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security提供了一个默认的UserDetails实现User，一般情况下使用该类即可满足需求，
 * 不过为了扩展，这里依然使用自定义的GxUserDetails
 * @author miya
 */
public class MachineDetails implements UserDetails {
    /**
     *
     */
    private MachineDTO machine;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = Lists.newArrayList();
        grantedAuthorityList.add(new GrantedAuthority() {
            public String getAuthority() {
                // 系统在外面比较时，会加上ROLE_前缀，所以这里返回时需要加上
                return "ROLE_MACHINE";
            }
        });
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return machine.getElecBoardCode();
    }

    @Override
    public String getUsername() {
        return machine.getMachineName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
