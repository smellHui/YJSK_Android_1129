package com.yangj.dahemodule.model.user;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class SysUserBean {

    private SysUser sysUser;

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
                '}';
    }
}
