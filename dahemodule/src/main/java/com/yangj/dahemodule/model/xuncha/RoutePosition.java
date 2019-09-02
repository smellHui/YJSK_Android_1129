package com.yangj.dahemodule.model.xuncha;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
  * Created by      Android studio
  *
  * @author :wwj (from Center Of Wuhan)
  * Date    :2019/5/7
  * Version :1.0
  * 功能描述 : 巡查点
 **/
public class RoutePosition extends DataSupport implements Serializable {
    @Override
    public synchronized boolean saveOrUpdate(String... conditions) {
        return super.saveOrUpdate(conditions);
    }

    String fatherId;

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    String reservoirId;
    String userCode;

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    private String positionId;
    private String positionLttd;
    private String positionLgtd;
    private String workOrderId;
    private String uuid;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionLttd() {
        return positionLttd;
    }

    public void setPositionLttd(String positionLttd) {
        this.positionLttd = positionLttd;
    }

    public String getPositionLgtd() {
        return positionLgtd;
    }

    public void setPositionLgtd(String positionLgtd) {
        this.positionLgtd = positionLgtd;
    }
}
