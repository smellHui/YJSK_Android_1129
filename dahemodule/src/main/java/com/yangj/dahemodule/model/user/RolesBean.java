package com.yangj.dahemodule.model.user;

import com.google.common.base.Strings;

/**
 * Author:xch
 * Date:2019/10/16
 * Description:
 */
public class RolesBean {

    /**
     * ROLE_JS
     * ROLE_XC
     */
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 是否是巡查责任人
     *
     * @return
     */
    public boolean isXC() {
        return !Strings.isNullOrEmpty(code) && code.equals("ROLE_XC");
    }

    @Override
    public String toString() {
        return "RolesBean{" +
                "code='" + code + '\'' +
                '}';
    }
}
