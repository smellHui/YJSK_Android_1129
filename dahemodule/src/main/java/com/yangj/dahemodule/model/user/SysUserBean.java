package com.yangj.dahemodule.model.user;

import java.util.List;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class SysUserBean {

    private SysUser sysUser;
    private List<RolesBean> roles;

    public List<RolesBean> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesBean> roles) {
        this.roles = roles;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    @Override
    public String toString() {
        return "SysUserBean{" +
                "sysUser=" + sysUser +
                ", roles=" + roles +
                '}';
    }
}
