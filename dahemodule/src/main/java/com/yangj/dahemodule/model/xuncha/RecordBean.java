package com.yangj.dahemodule.model.xuncha;

/**
 * Author:xch
 * Date:2019/9/3
 * Description:巡查列表项
 */
public class RecordBean {
    private String status;
    private String createBy;
    private String creatorName;
    private String createTime;
    private String updateBy;
    private String updaterName;
    private String updateTime;
    private String id;
    private String reservoirCode;
    private String code;
    private String name;
    private String omRouteId;
    private String omPath;
    /**
     * 1-执行中
     * 2-已提交
     * 3-运维已审核
     * 4-已生成报告
     */
    private String executeStatus;
    private String routePath;
    private String itemList;
    private String reservoirStructureList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservoirCode() {
        return reservoirCode;
    }

    public void setReservoirCode(String reservoirCode) {
        this.reservoirCode = reservoirCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOmRouteId() {
        return omRouteId;
    }

    public void setOmRouteId(String omRouteId) {
        this.omRouteId = omRouteId;
    }

    public String getOmPath() {
        return omPath;
    }

    public void setOmPath(String omPath) {
        this.omPath = omPath;
    }

    public String getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus) {
        this.executeStatus = executeStatus;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public String getReservoirStructureList() {
        return reservoirStructureList;
    }

    public void setReservoirStructureList(String reservoirStructureList) {
        this.reservoirStructureList = reservoirStructureList;
    }

    @Override
    public String toString() {
        return "RecordBean{" +
                "status='" + status + '\'' +
                ", createBy='" + createBy + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id='" + id + '\'' +
                ", reservoirCode='" + reservoirCode + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", omRouteId='" + omRouteId + '\'' +
                ", omPath='" + omPath + '\'' +
                ", executeStatus='" + executeStatus + '\'' +
                ", routePath='" + routePath + '\'' +
                ", itemList='" + itemList + '\'' +
                ", reservoirStructureList='" + reservoirStructureList + '\'' +
                '}';
    }
}
