package com.yangj.dahemodule.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pgyersdk.update.PgyUpdateManager;
import com.tepia.base.AppRoutePath;
import com.tepia.base.CacheConsts;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.RetrofitManager;
import com.tepia.base.utils.Utils;
import com.tepia.photo_picker.utils.SPUtils;
import com.yangj.dahemodule.APPCostant;
import com.yangj.dahemodule.http.UserHttpService;
import com.yangj.dahemodule.model.JsonBean;
import com.yangj.dahemodule.model.NewNoticeBean;
import com.yangj.dahemodule.model.UserDataBean;
import com.yangj.dahemodule.model.UserInfoBean;
import com.yangj.dahemodule.model.UserLoginResponse;
import com.yangj.dahemodule.model.WeatherWarnBean;
import com.yangj.dahemodule.model.main.MainDataBean;
import com.yangj.dahemodule.model.user.SysUserDataBean;
import com.yangj.dahemodule.model.xuncha.AreaBean;
import com.yangj.dahemodule.model.xuncha.DataBeanOflistReservoirRoute;
import com.yangj.dahemodule.model.xuncha.RecordDataBean;
import com.yangj.dahemodule.model.xuncha.ReservoirBean;
import com.yangj.dahemodule.model.xuncha.ReservoirListResponse;
import com.yangj.dahemodule.model.xuncha.ReservoirOfflineResponse;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.yangj.dahemodule.fragment.OperatesFragment.ALL_OPERATE;
import static com.yangj.dahemodule.fragment.OperatesFragment.MINE_OPERATE;

/**
 * Created by Joeshould on 2018/5/22.
 */

public class UserManager {
    private UserHttpService mRetrofitService;
    private static final UserManager ourInstance = new UserManager();
    private static final UserManager ourInstance_admin = new UserManager(APPCostant.API_SERVER_USER_ADMIN);
    private static final UserManager ourInstance_works = new UserManager(APPCostant.API_SERVER_TASK_AREA);
    private static final UserManager ourInstance_monitor = new UserManager(APPCostant.API_SERVER_MONITOR_AREA);
    private static final UserManager ourInstance_json = new UserManager("http://202.98.201.103:7000/", true);
    public static final String USERINFO = "USERINFO";
    private static final String TOKEN = "TOKEN";
    private static final String DEFAULT_RES = "DEFAULT_RES";

    public static UserManager getInstance() {
        return ourInstance;
    }

    public static UserManager getInstance_ADMIN() {
        return ourInstance_admin;
    }

    public static UserManager getInstance_Works() {
        return ourInstance_works;
    }

    public static UserManager getInstance_Monitor() {
        return ourInstance_monitor;
    }

    public static UserManager getInstance_Json() {
        return ourInstance_json;
    }

    private UserManager() {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL).create(UserHttpService.class);
    }

    private UserManager(String adminstr) {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + adminstr).create(UserHttpService.class);
    }

    private UserManager(String str, boolean distinct) {
        this.mRetrofitService = RetrofitManager.getRetrofit(str).create(UserHttpService.class);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public Observable<UserDataBean> login(String username, String password) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", password);
        hashMap.put("randomStr", "");
        hashMap.put("grant_type", "password");
        hashMap.put("scope", "server");
        return mRetrofitService.login(getRequestBody(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private RequestBody getRequestBody(HashMap<String, String> hashMap) {
        StringBuffer data = new StringBuffer();
        if (hashMap != null && hashMap.size() > 0) {
            Iterator iter = hashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                data.append(key).append("=").append(val).append("&");
            }
        }
        String jso = data.substring(0, data.length() - 1);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), jso);

        return requestBody;
    }

    /**
     * 【查询】巡检记录列表
     *
     * @param pageType
     * @param reservoirCode
     * @param pageNum
     * @param pageSize
     * @param startDate
     * @param endDate
     * @return
     */
    public Observable<RecordDataBean> getRecordList(int pageType, String reservoirCode, int pageNum, int pageSize, String startDate, String endDate) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("reservoirCode", reservoirCode);
        hashMap.put("pageNum", pageNum + "");
        hashMap.put("pageSize", pageSize + "");
        hashMap.put("startDate", startDate);
        hashMap.put("endDate", endDate);
        if (pageType == MINE_OPERATE) {
            return mRetrofitService.getRecordListByMe("Bearer 95e8a444-33d0-4fa9-be46-6127ead69664", hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        if (pageType == ALL_OPERATE) {
            return mRetrofitService.getRecordList("Bearer 95e8a444-33d0-4fa9-be46-6127ead69664", hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return null;
    }

    /**
     * 【查询】加载数据接口
     *
     * @param reservoirCode
     * @return
     */
    public Observable<MainDataBean> loadData(String reservoirCode) {
        return mRetrofitService.loadData("Bearer 95e8a444-33d0-4fa9-be46-6127ead69664", reservoirCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 【查询】用户信息
     *
     * @return
     */
    public Observable<SysUserDataBean> getUserInfo() {
        return mRetrofitService.getUserInfo("Bearer 95e8a444-33d0-4fa9-be46-6127ead69664")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * @return
     */
    public Observable<ReservoirListResponse> getReservoirList() {
        String token = getToken();

        return mRetrofitService.getReservoirList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * APP查询某个水库全部信息
     *
     * @param reservoirId
     * @return
     */
    public Observable<ReservoirOfflineResponse> getAllReservoirData(String reservoirId) {
        String token = getToken();

        return mRetrofitService.getAllReservoirData(token, reservoirId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询水库最新通知
     *
     * @param reservoirId
     * @return
     */
    public Observable<NewNoticeBean> getNewNotice(String reservoirId) {
        String token = getToken();

        return mRetrofitService.getNewNotice(token, reservoirId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 天气预警
     *
     * @return
     */
    public Observable<WeatherWarnBean> getWeatherWarn() {
        String token = getToken();

        return mRetrofitService.getWeatherWarn(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取当前用户行政区划下拉(省市三级联动)
     *
     * @return
     */
    public Observable<AreaBean> getAreaSelect() {
        String token = getToken();
        /*token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJndWFuZ2RvbmdhZG" +
                "1pbiIsInVzZXJJZCI6ImFlNTFiYzZiNWE5ZjRjZjA5YTk2YWQ4YjUwNzVjMTYxIiwibmFtZSI" +
                "6IuW5v-S4nOecgeeuoeeQhuWRmCIsImxvZ2luU2NvcGUiOiIwIiwiZXhwIjoxNTU5MjM0OTgwfQ.BwxHE" +
                "rO2eSm5M8WFh4GK-MCv2qJw1X36h97J8MmG1hbWBaqVlf3loJcIAPh-PNZPNmmYTvS2CA-UCzJf3qe-zc" +
                "nYJBNdSFmKYCHkiF3ticSYpbNW2khxF6N_WOjBMr82G3UK5yDKLI_0_iYmW7QaVe9UQ6eHOzBpxwBQfMqIbcs";*/

        return mRetrofitService.getAreaSelect(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> updateNotice(String id, String workOrderId) {
        String token = getToken();

        return mRetrofitService.updateNotice(token, id, workOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取用户登录信息
     *
     * @return
     */
    public Observable<UserInfoBean> getLoginUser() {
        String token = getToken();

        return mRetrofitService.getLoginUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void saveToken(UserLoginResponse userBean) {
        if (userBean != null) {
            SPUtils.getInstance(Utils.getContext()).putString(TOKEN, userBean.getData());
            SPUtils.getInstance(Utils.getContext()).putString(USERINFO, new Gson().toJson(userBean));

        }

    }

    /**
     * 获取用户token
     *
     * @return
     */
    public String getToken() {
        String temp = SPUtils.getInstance(Utils.getContext()).getString("TOKEN", "");
        return temp;
    }


    /**
     * 退出登录时清空缓存和断开极光推送
     */
    public void clearCacheAndStopPush() {
        SPUtils.getInstance(Utils.getContext()).remove(USERINFO);
        SPUtils.getInstance(Utils.getContext()).remove(TOKEN);
        SPUtils.getInstance(Utils.getContext()).remove(CacheConsts.AREA);

       /* DataSupport.deleteAll(CheckTaskPicturesBean.class);
        DataSupport.deleteAll(DataBeanOflistReservoirRoute.class);
        DataSupport.deleteAll(BizCheckTaskSonWorksBean.class);
        DataSupport.deleteAll(BizCheckTaskRsvrRelsBean.class);
        DataSupport.deleteAll(BizCheckTaskReservoirWrittenBean.class);
        DataSupport.deleteAll(BizCheckTaskMainWorksListBean.class);
        DataSupport.deleteAll(DataBeanPublic.class);
        DataSupport.deleteAll(OfflineStandardInit.class);*/

//        JPushInterface.stopPush(Utils.getContext());
        PgyUpdateManager.unregister();


    }

    /**
     * 将用户信息 存到 sp中
     *
     * @param userBean
     */
    private UserInfoBean userBean;

    public void setUserBean(UserInfoBean userBean) {
        if (userBean != null) {
            this.userBean = userBean;
            SPUtils.getInstance(Utils.getContext()).putString(USERINFO, new Gson().toJson(userBean).toString());
        }
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfoBean getUserBean() {
        if (userBean != null) {
            return userBean;
        } else {
            try {
                String temp = SPUtils.getInstance(Utils.getContext()).getString(USERINFO, "");
                if (!TextUtils.isEmpty(temp)) {
                    return new Gson().fromJson(temp, UserInfoBean.class);
                } else {
                    return userBean;
                }
            } catch (Exception e) {

            }

        }
        return userBean;
    }

    /**
     * 约定 0：管理人员
     * 1：行政  测试账号 13265489652
     * 2：技术
     * 3：巡查  测试账号 test001
     * 01 或10 管理和行政兼任
     * 02 或20 管理和技术兼任
     * 03 待定
     *
     * @return
     */
    public String getRoleType() {
        UserInfoBean userInfoBean = getUserBean();
        if (userInfoBean.getData() != null) {
            List<UserInfoBean.DataBean.SysRolesBean> sysRolesBeanList = userInfoBean.getData().getSysRoles();
            StringBuilder stringBuilder = new StringBuilder();
            for (UserInfoBean.DataBean.SysRolesBean sysRolesBean : sysRolesBeanList) {
                stringBuilder.append(sysRolesBean.getRoleType());
            }
            return stringBuilder.toString();
        }
        return "";
    }

    public void savaArea(ArrayList<AreaBean.DataBeanX.DataBean> jsonBean) {
        com.tepia.base.utils.SPUtils.getInstance().putString(CacheConsts.AREA, new Gson().toJson(jsonBean));

    }

    public ArrayList<AreaBean.DataBeanX.DataBean> getArea() {

        try {
            String temp = SPUtils.getInstance(Utils.getContext()).getString(CacheConsts.AREA, "");
            if (!TextUtils.isEmpty(temp)) {
                ArrayList<AreaBean.DataBeanX.DataBean> dataBeans = new Gson().fromJson(temp, new TypeToken<ArrayList<AreaBean.DataBeanX.DataBean>>() {
                }.getType());
                return dataBeans;
            } else {
                return null;
            }
        } catch (Exception e) {

        }


        return null;
    }

    /**
     * 保存 我们负责的水库
     *
     * @param data
     */
    public void saveReservoirList(List<ReservoirBean> data) {
        SPUtils.getInstance().putString("RESERVOIRLIST", new Gson().toJson(data));

    }

    /**
     * 获取保存 我们负责的水库
     */
    public ArrayList<ReservoirBean> getLocalReservoirList() {
        ArrayList<ReservoirBean> reservoirBeans = null;
        String temp = SPUtils.getInstance().getString("RESERVOIRLIST", "");
        if (!TextUtils.isEmpty(temp)) {
            reservoirBeans = new Gson().fromJson(temp, new TypeToken<ArrayList<ReservoirBean>>() {
            }.getType());
        }
        return reservoirBeans;
    }

    /**
     * @param reservoirBean
     */
    public void saveDefaultReservoir(ReservoirBean reservoirBean) {
        SPUtils.getInstance().putString("DEFAULTRESERVOIR", new Gson().toJson(reservoirBean));
    }

    public ReservoirBean getDefaultReservoir() {
        ReservoirBean reservoirBean = null;
        String temp = SPUtils.getInstance().getString("DEFAULTRESERVOIR", "");
        try {
            if (!TextUtils.isEmpty(temp)) {
                reservoirBean = new Gson().fromJson(temp, ReservoirBean.class);
            }
        } catch (Exception e) {
        }
        return reservoirBean;
    }


    public void saveReservoir(String res) {
        SPUtils.getInstance().putString(DEFAULT_RES, res);
    }

    public String getReservoir() {

        return SPUtils.getInstance().getString(DEFAULT_RES, "");
    }


    /**
     * 根据水库id 查询离线数据实体
     *
     * @param reservoirId
     * @return
     */
    public DataBeanOflistReservoirRoute getOfflineReservoir(String reservoirId, String userCode, Context context) {
        DataBeanOflistReservoirRoute dataBeanList = null;
        ReservoirBean reservoirBean = DataSupport.where("reservoirId = ? and userCode=?", reservoirId, userCode).findFirst(ReservoirBean.class);
        if (reservoirBean != null) {
            String jsonAboutInfo = reservoirBean.getJsonAboutInfo();
            if (TextUtils.isEmpty(jsonAboutInfo)) {

                showOffine(context);
                return dataBeanList;
            }
            ReservoirOfflineResponse reservoirOfflineResponse = new Gson().fromJson(jsonAboutInfo, ReservoirOfflineResponse.class);
            if (reservoirOfflineResponse != null) {
                dataBeanList = reservoirOfflineResponse.getData();

            }
        }
        return dataBeanList;
    }

    /**
     * 提醒下载离线包
     */
    private void showOffine(Context context) {
        ReservoirBean reservoirBean = getDefaultReservoir();
        if (context != null && !((Activity) context).isFinishing()) {
            String reservori = "";
            if (reservoirBean != null) {
                reservori = reservoirBean.getReservoir();
            }
            new AlertDialog.Builder(context)
                    .setMessage("请先下载" + reservori + "的基本信息离线包")
                    .setCancelable(true)
                    .setPositiveButton("去下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SPUtils.getInstance().putBoolean(CacheConsts.HAS_ASK, true);
                            ARouter.getInstance().build(AppRoutePath.app_select_reservor_downoffline)
                                    .withSerializable("selectedResrvoir", reservoirBean)
                                    //flag为10表示 离线数据包管理 为"0" 表示选择水库
                                    .withString("offlineFlag", "10")
                                    .navigation();

                        }
                    })
                    .show();
        }

    }

    /**
     * 人员位置上报接口
     *
     * @param reservoirId
     * @param longitude
     * @param latitude
     * @return
     */
    public Observable<BaseResponse> uploadCheckManLocation(String reservoirId, String longitude, String latitude) {
        String token = getToken();

        return mRetrofitService.uploadCheckManLocation(token, reservoirId, longitude, latitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取登录界面json
     *
     * @return
     */
    public Observable<JsonBean> getJson() {

        return mRetrofitService.getJson()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
