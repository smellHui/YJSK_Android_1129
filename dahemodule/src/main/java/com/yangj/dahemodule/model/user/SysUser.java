package com.yangj.dahemodule.model.user;

/**
 * Author:xch
 * Date:2019/9/4
 * Description:
 */
public class SysUser {


    /**
     * id : 5
     * username : dhxc
     * createTime : 2019-08-30 08:54:41
     * updateTime : 2019-08-30 10:55:34
     * delFlag : 0
     * lockFlag : 0
     * phone : 13212345682
     * avatar : null
     * deptId : 1
     * wxOpenid : null
     * qqOpenid : null
     */

    private int id;
    private String username;
    private String createTime;
    private String updateTime;
    private String delFlag;
    private String lockFlag;
    private String phone;
    private Object avatar;
    private int deptId;
    private Object wxOpenid;
    private Object qqOpenid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(String lockFlag) {
        this.lockFlag = lockFlag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public Object getWxOpenid() {
        return wxOpenid;
    }

    public void setWxOpenid(Object wxOpenid) {
        this.wxOpenid = wxOpenid;
    }

    public Object getQqOpenid() {
        return qqOpenid;
    }

    public void setQqOpenid(Object qqOpenid) {
        this.qqOpenid = qqOpenid;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", lockFlag='" + lockFlag + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar=" + avatar +
                ", deptId=" + deptId +
                ", wxOpenid=" + wxOpenid +
                ", qqOpenid=" + qqOpenid +
                '}';
    }
}
