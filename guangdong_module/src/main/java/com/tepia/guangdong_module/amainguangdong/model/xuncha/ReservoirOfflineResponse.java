package com.tepia.guangdong_module.amainguangdong.model.xuncha;

import com.tepia.base.http.BaseResponse;

import java.util.List;

/**
 * Created by      android studio
 *
 * @author :       ly
 * Date            :       2019-04-04
 * Time            :       上午10:51
 * Version         :       1.0
 * location        :       武汉研发中心
 * 功能描述         :       离线实体类，转为json后存储在com.tepia.main.model.detai.ReservoirBean表中的jsonAboutInfo字段中
 **/
public class ReservoirOfflineResponse extends BaseResponse {




    /**
     * data : {"routeList":[],"dangerWarnPage":{"positions":[{"positionName":"闸门/阀门","positionId":"0cf9261e8ca94ace9a2d204ab46ca83e"},{"positionName":"坝体与坝基、坝端结合处","positionId":"1a6590064a4141698a839a33c27b2768"},{"positionName":"监测自动化系统","positionId":"1af8df4c299746f6b7f1b0b31910e851"},{"positionName":"坝顶","positionId":"28447e5777044fc6a98e08b0bbb6f5d7"},{"positionName":"闸门或阀门","positionId":"3ecac23f55354eab97b68689354e6381"},{"positionName":"坝体与输水洞（管）结合处","positionId":"3f4dd93bbe0b40fc86b71f1414f42988"},{"positionName":"监测设施","positionId":"40786230a073411daf07ff88fc96b2d2"},{"positionName":"安全警示牌、宣传牌","positionId":"41e3d11579fb437289669fc6179d76e4"},{"positionName":"背水坡","positionId":"569e4acdad1344edac279e3ad6eca489"},{"positionName":"渗流监测","positionId":"8562ddb2600343e78e4b17dd218bff45"},{"positionName":"变形监测","positionId":"8ca026ae8dbe470692f983fb40d6d433"},{"positionName":"输水涵洞","positionId":"8ed91ca53fd24ae684ab568638134ecc"},{"positionName":"坝体与溢洪道结合处","positionId":"a82e700d73b7477da7315ec08be2f09f"},{"positionName":"溢洪道","positionId":"adec091d5a524a5f9ca5c3ee6ba554e7"},{"positionName":"压力（应力）监测","positionId":"b93119f720c94e9c92c3bdc3b5f8dcfb"},{"positionName":"环境量监测","positionId":"d6a7c8215881411f9d3b036781087ba1"},{"positionName":"迎水坡","positionId":"d75e434723ea4617800ba9a04f60ed1f"},{"positionName":"坝体","positionId":"d8c496813fbe443683a35baee3e8c577"},{"positionName":"水面及库岸边","positionId":"ff39eae9bf19460ba0529f9399fc81a0"}],"userList":[{"userName":"tester","mobile":"","jobName":"技术责任人"},{"userName":"邹伟清","mobile":"13435348823","jobName":"技术责任人"},{"userName":"朱赵平","mobile":"15876226577","jobName":"行政责任人"}]}}
     */



    private DataBeanOflistReservoirRoute data;

    public DataBeanOflistReservoirRoute getData() {
        return data;
    }

    public void setData(DataBeanOflistReservoirRoute data) {
        this.data = data;
    }


}
