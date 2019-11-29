package com.yangj.dahemodule.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.ToastUtils;
import com.tepia.base.view.arcgisLayout.ArcgisLayout;
import com.tepia.base.view.arcgisLayout.GoogleMapLayer;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RouteListBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataBean;
import com.tepia.guangdong_module.amainguangdong.route.RoutepointDataManager;
import com.tepia.guangdong_module.amainguangdong.route.TaskBean;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.ArcgisUtils;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.adapter.PartCardAdapter;
import com.yangj.dahemodule.util.GaodeHelper;
import com.yangj.dahemodule.util.UiHelper;
import com.yangj.dahemodule.view.ForecastView;
import com.yangj.dahemodule.view.PatrolRateView;
import com.yangj.dahemodule.wrap.PatrolWrap;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:xch
 * Date:2019/11/8
 * Description:巡查点操作页-下
 */
public class PatrolMapControlActivity extends BaseActivity implements
        DiscreteScrollView.OnItemChangedListener,
        DiscreteScrollView.ScrollStateChangeListener,
        BaseQuickAdapter.OnItemClickListener,
        GaodeHelper.LocationListener {
    private int[] picIds = {com.example.guangdong_module.R.drawable.ksxc_icon_map_dxc, com.example.guangdong_module.R.drawable.ksxc_icon_map_yxc, com.example.guangdong_module.R.drawable.ksxc_icon_map_ljxc};
    private final String WORK_ORDER_ID = "workorderid";
    private String workOrderId;
    private TaskBean taskBean;
    private RouteListBean routeListBean;
    private Boolean isCompleteOfTaskBean;
    private List<Point> walkPoints;

    private PatrolRateView patrolRateView;
    private DiscreteScrollView partPicker;
    private ArcgisLayout arcgisLayout;
    private MapView mapView;
    private ForecastView forecastView;
    private PartCardAdapter partCardAdapter;

    //  标准路线图层
    private GraphicsOverlay standardRouteOverlay;
    // 实际路线图层
    private GraphicsOverlay actualRouteOverlay;
    private GraphicsOverlay dxcOverlay;
    private GraphicsOverlay yxcOverlay;
    private GraphicsOverlay ljxcOverlay;
    private GraphicsOverlay locationOverlay;
    private GraphicsOverlay textGraphics;

    private List<RoutePosition> routePositions;
    /**
     * 已巡查点
     */
    private List<RoutePosition> yxcRoutePositions;
    /**
     * 待巡查点
     */
    private List<RoutePosition> dxcRoutePositions;

    private List<Point> exeline;
    private GaodeHelper gaodeHelper;

    private CheckBox autoLocation;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_map_control;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);

        exeline = new ArrayList<>();
        dxcRoutePositions = new ArrayList<>();
        yxcRoutePositions = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            workOrderId = intent.getStringExtra(WORK_ORDER_ID);
            taskBean = SqlManager.getInstance().queryTaskSqlByWorkOrderId(workOrderId);
            routeListBean = SqlManager.getInstance().queryRouteSqlByWorkOrderId(workOrderId);
            routePositions = SqlManager.getInstance().queryRoutePositionsByWorkid(workOrderId);
            isCompleteOfTaskBean = taskBean.isHasExecuted();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(PatrolWrap patrolWrap) {
        int patrolIndex = patrolWrap.getPatrolIndex();
        partPicker.scrollToPosition(patrolIndex);
        partCardAdapter.notifyItemChanged(patrolIndex);
        patrolRateView.updateRateView();
        forecastView.setForecast(routePositions.get(patrolIndex));
    }

    @Override
    public void initView() {

        showBack();

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

//        autoLocation = findViewById(R.id.checkbox_auto);
        autoLocation.setVisibility(isCompleteOfTaskBean ? View.GONE : View.VISIBLE);
        patrolRateView = findViewById(R.id.view_patrol_rate);
        patrolRateView.setWorkOrderId(workOrderId, isCompleteOfTaskBean);
        forecastView = findViewById(R.id.view_forecast);
        arcgisLayout = findViewById(R.id.arcgis_layout);
        mapView = arcgisLayout.getMapView();

        partPicker = findViewById(R.id.part_picker);
        partPicker.setSlideOnFling(true);
        partCardAdapter = new PartCardAdapter();
        partCardAdapter.setOnItemClickListener(this);
        partPicker.setAdapter(partCardAdapter);
        partPicker.addOnItemChangedListener(this);
        partPicker.addScrollStateChangeListener(this);
        partPicker.setOrientation(DSVOrientation.HORIZONTAL);
        partPicker.setItemTransitionTimeMillis(400);
        partPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        if (!CollectionsUtil.isEmpty(routePositions)) {
            forecastView.setForecast(routePositions.get(0));
            partCardAdapter.setNewData(routePositions);
        }

        initMap();
        if (!isCompleteOfTaskBean) {
            startLocation();
        }
        initStandardRoute();
        setArcgisMapClick();
        drawLineOnMap();
        newTaskTimeOfNearPosition();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder holder, int position) {
        if (holder != null) {
            forecastView.setForecast(routePositions.get(position));
            RoutePosition routePosition = routePositions.get(position);
            spanglePoint(routePosition);
        }
    }

    @Override
    public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScrollEnd(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public void onScroll(float position, int currentIndex, int newIndex, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
        RoutePosition current = routePositions.get(currentIndex);
        if (newIndex >= 0 && newIndex < partCardAdapter.getItemCount()) {
            RoutePosition next = routePositions.get(newIndex);
            forecastView.onScroll(1f - Math.abs(position), current, next);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        UiHelper.goToPatrolUpControlView(this, position, isCompleteOfTaskBean, (ArrayList<RoutePosition>) routePositions);
    }

    private void startLocation() {
        gaodeHelper = new GaodeHelper(this, PatrolMapControlActivity.class, locationOverlay, this);
        //android 6.0动态申请权限
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        } else {
            gaodeHelper.startLocation();
        }
    }

    /**
     * 第一次进界面添加标准路线和待巡查点
     */
    private void initStandardRoute() {
        //第一次进界面添加标准路线和待巡查点
        if (routeListBean != null) {
            String routeContent = routeListBean.getRouteContent();
            List<Point> result = new ArrayList();
            try {
                List<Double[]> routes = JSON.parseArray(routeContent, Double[].class);
                for (Double[] route : routes) {
                    Point point = new Point(route[0], route[1], SpatialReference.create(4326));
                    result.add(point);
                }
                int color = ContextCompat.getColor(getContext(), com.example.guangdong_module.R.color.route_color_one);
                arcgisLayout.addPolylineToGraphicsOverlay(standardRouteOverlay, result, SimpleLineSymbol.Style.SOLID, color, 4);
                if (!CollectionsUtil.isEmpty(result)) {
                    Map map = new HashMap();
                    arcgisLayout.addPicToGraphicsOverlay(standardRouteOverlay, com.example.guangdong_module.R.mipmap.ic_me_history_startpoint, result.get(0), map);
                    arcgisLayout.addPicToGraphicsOverlay(standardRouteOverlay, com.example.guangdong_module.R.mipmap.ic_me_history_finishpoint, result.get(result.size() - 1), map);
                }
                arcgisLayout.setMapViewVisibleExtent(result);
            } catch (Exception e) {

            }
            //刷新待巡查点和已巡查点
            refreshPointsOnMap();
            //巡查点添加文字app/patrol/add
            if (!CollectionsUtil.isEmpty(routePositions)) {
                for (RoutePosition routePosition : routePositions) {
                    try {
                        List<TaskItemBean> itemList = SqlManager.getInstance().queryTaskItem(workOrderId, routePosition.getPositionId());
                        Point point = routePosition.parasePoint();
                        int color = ContextCompat.getColor(getContext(), com.example.guangdong_module.R.color.text_map_bg);
                        if (!CollectionsUtil.isEmpty(itemList)) {
                            TaskItemBean taskItemBean = itemList.get(0);
                            if (taskItemBean != null) {
                                String positionTreeNames = taskItemBean.getPositionTreeNames();
                                Map<String, Object> attrs = new HashMap<>();
                                attrs.put("positionId", routePosition.getPositionId());
//                                ArcgisLayout.setTextMarker(point, textGraphics, positionTreeNames, color, attrs, 28);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    /**
     * 设置地图点击事件
     */
    private void setArcgisMapClick() {
        arcgisLayout.setOnMapViewClickListener((e, mapView) -> {
            android.graphics.Point screenPoint = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
            Point clickPoint = mapView.screenToLocation(screenPoint);
            Point locationPoint = (Point) GeometryEngine.project(clickPoint, SpatialReference.create(4326));
            ListenableFuture<List<IdentifyGraphicsOverlayResult>> listListenableFuture = mapView.identifyGraphicsOverlaysAsync(screenPoint, 5, false);
            try {
                List<IdentifyGraphicsOverlayResult> overlayResults = listListenableFuture.get();
                if (overlayResults != null && overlayResults.size() > 0) {
                    List<Graphic> graphics = overlayResults.get(0).getGraphics();
                    if (graphics != null && graphics.size() > 0) {
                        Map<String, Object> attributes = graphics.get(0).getAttributes();
                        if (attributes != null) {
                            String positionId = (String) attributes.get("positionId");
                            if (routePositions != null && routePositions.size() > 0) {
                                for (RoutePosition routePosition : routePositions) {
                                    String positionId1 = routePosition.getPositionId();
                                    if (positionId.equals(positionId1)) {
                                        locationPoint = routePosition.parasePoint();
                                        searchNearbyRoutePosition(routePositions, locationPoint);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e1) {

            }
            //刷新待巡查点和已巡查点
            refreshPointsOnMap();
        });
    }

    private void drawLineOnMap() {
        if (taskBean == null) {
            return;
        }
        if (routeListBean == null || routeListBean.getItemListByworkid(workOrderId) == null) {
            return;
        }
        arcgisLayout.removeGraphics();
        arcgisLayout.post(() -> {
            if (!TextUtils.isEmpty(taskBean.getWorkOrderRoute())) {
                if (taskBean.isHasExecuted()) {
                    //已完成状态时
                    drawCompleteLine(taskBean.getWorkOrderRoute());
                    //默认第一个巡查点
                    if (!CollectionsUtil.isEmpty(routePositions)) {
                        RoutePosition routePosition = routePositions.get(0);
                        Point locationPoint = routePosition.parasePoint();
                        searchNearbyRoutePosition(routePositions, locationPoint);
                    }
                } else {
                    drawNotCompleteLine();
                }
            } else {
                drawNotCompleteLine();
            }
        });
    }

    /**
     * 已完成工单画线
     *
     * @param
     */
    private void drawCompleteLine(String workOrderRoute) {
        if (TextUtils.isEmpty(workOrderRoute)) {
            return;
        }
        String temp = workOrderRoute.replaceAll("\\{", "[").replaceAll("\\}", "]");
        List<Double[]> list = JSON.parseArray(temp, Double[].class);
        if (list != null && list.size() >= 2) {
            ArrayList<Point> exeline = new ArrayList();
            for (Double[] bean : list) {
                Point point1 = new Point(bean[0], bean[1], SpatialReference.create(4326));
                exeline.add(point1);
            }
            int color = ContextCompat.getColor(getContext(), com.example.guangdong_module.R.color.route_color_two);
            arcgisLayout.addPolyline(exeline, SimpleLineSymbol.Style.SOLID, color, 4);
            if (exeline != null && exeline.size() > 0) {
                arcgisLayout.setMapViewVisibleExtent(exeline);
                arcgisLayout.addPic(com.example.guangdong_module.R.mipmap.ic_me_history_startpoint, exeline.get(0), new HashMap<>());
                arcgisLayout.addPic(com.example.guangdong_module.R.mipmap.ic_me_history_finishpoint, exeline.get(exeline.size() - 1), new HashMap<>());
            }
        }
    }

    /**
     * 定时闪烁临近巡查点
     */
    private boolean isLjxcVisible;

    private void newTaskTimeOfNearPosition() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isLjxcVisible = !isLjxcVisible;
                ljxcOverlay.setVisible(isLjxcVisible);
                //每隔1s循环执行run方法
                handler.postDelayed(this, 500);
            }
        };
        //延时一秒
        handler.postDelayed(runnable, 1000);
    }

    /**
     * 未完成工单划线
     */
    private void drawNotCompleteLine() {
        List<RoutepointDataBean> list = RoutepointDataManager.getInstance().getRoutePointListWithOutD(workOrderId);
        if (list != null && list.size() >= 2) {
            exeline.clear();
            for (RoutepointDataBean bean : list) {
                Point point1 = bean.parasePoint();
                exeline.add(point1);
            }
            try {
                arcgisLayout.setMapViewVisibleExtent(exeline);
                int color = ContextCompat.getColor(getContext(), com.example.guangdong_module.R.color.route_color_two);

                arcgisLayout.addPolyline(exeline, SimpleLineSymbol.Style.SOLID, color, 4);
                if (exeline != null && exeline.size() > 0) {
                    arcgisLayout.addPic(com.example.guangdong_module.R.mipmap.ic_me_history_startpoint, exeline.get(0));
                }
            } catch (Exception e) {
            }
        }
    }

    //刷新待巡查点和已巡查点
    private void refreshPointsOnMap() {
        setYxcListAndDxcList();
        setRoutePosition(dxcRoutePositions, picIds[0], dxcOverlay);
        setRoutePosition(yxcRoutePositions, picIds[1], yxcOverlay);
    }

    /**
     * 设置已巡查点集合和待巡查点集合
     */
    private void setYxcListAndDxcList() {
        dxcRoutePositions.clear();
        yxcRoutePositions.clear();
        if (!CollectionsUtil.isEmpty(routePositions)) {
            //得到待巡查点集合和已巡查点集合
            for (RoutePosition routePosition : routePositions) {
                //未完成
                List<TaskItemBean> itemList = SqlManager.getInstance().queryTaskItem(workOrderId, routePosition.getPositionId(), "0");
                if (CollectionsUtil.isEmpty(itemList)) {
                    yxcRoutePositions.add(routePosition);
                } else {
                    dxcRoutePositions.add(routePosition);
                }
            }
        }
    }

    /**
     * 设置巡查点图标
     *
     * @param mRoutePosition
     */
    private void setRoutePosition(List<RoutePosition> mRoutePosition, int pid, GraphicsOverlay overlay) {
        overlay.getGraphics().clear();
        if (mRoutePosition != null && mRoutePosition.size() > 0) {
            for (RoutePosition routePosition : mRoutePosition) {
                try {
                    Point point = routePosition.parasePoint();
                    Map<String, Object> attrs = new HashMap<>();
                    attrs.put("positionId", routePosition.getPositionId());
                    arcgisLayout.addPicToGraphicsOverlay(overlay, pid, point, attrs);
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 搜索附近的巡查点
     *
     * @param mRoutePosition 巡查点集合
     * @param mPoint         当前点
     */
    private Vibrator vibrator;
    private String nearDxcPositionStr = "";

    private void searchNearbyRoutePosition(List<RoutePosition> mRoutePosition, Point mPoint) {
        List<Double> distanceList = new ArrayList<>();
        RoutePosition nearbyRoutePosition = null;
        int index = -1;
        if (mPoint != null) {
            if (mRoutePosition != null && mRoutePosition.size() > 0 && mPoint != null) {
                distanceList.clear();
                for (RoutePosition routePosition : mRoutePosition) {
                    try {
                        Point point = routePosition.parasePoint();
                        double distance = ArcgisUtils.distancePoints(point, mPoint);
                        routePosition.setDistance(distance);
                        distanceList.add(distance);
                    } catch (Exception ex) {
                        routePosition.setDistance(Integer.MAX_VALUE + 0.0);
                        distanceList.add(Integer.MAX_VALUE + 0.0);
                    }
                }
                if (distanceList != null && distanceList.size() > 0) {
                    Double min = Collections.min(distanceList);
                    index = distanceList.lastIndexOf(min);
                    //保存500米之内最近的巡查点 min单位是千米
                    if (index < mRoutePosition.size() && min < 1) {
                        nearbyRoutePosition = mRoutePosition.get(index);
                    } else {
                        nearbyRoutePosition = null;
                    }
                } else {
                    nearbyRoutePosition = null;
                }
            }
            spanglePoint(nearbyRoutePosition);
            partPicker.scrollToPosition(index);

            List<Double> dcxDistanceList = new ArrayList<>();
            //靠近最近的待巡查点震动
            if (dxcRoutePositions != null && dxcRoutePositions.size() > 0 && mPoint != null) {
                dcxDistanceList.clear();
                for (RoutePosition routePosition : dxcRoutePositions) {
                    try {
                        Point point = routePosition.parasePoint();
                        double distance = ArcgisUtils.distancePoints(point, mPoint);
                        routePosition.setDistance(distance);
                        dcxDistanceList.add(distance);
                    } catch (Exception ex) {
                        routePosition.setDistance(Integer.MAX_VALUE + 0.0);
                        dcxDistanceList.add(Integer.MAX_VALUE + 0.0);
                    }
                }
                if (dcxDistanceList != null && dcxDistanceList.size() > 0) {
                    Double min = Collections.min(dcxDistanceList);
                    int i = dcxDistanceList.lastIndexOf(min);
                    //保存500米之内最近的巡查点 min单位是千米
                    if (i < dxcRoutePositions.size() && min < 1) {
                        RoutePosition routePosition = dxcRoutePositions.get(i);
                        String positionId = routePosition.getPositionId();
                        if (positionId != null) {
                            if (!nearDxcPositionStr.equals(positionId)) {
                                if (vibrator.hasVibrator()) {
                                    vibrator.vibrate(1000);
                                }
                                nearDxcPositionStr = positionId;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 闪烁点，选中点
     *
     * @param routePosition
     */
    private void spanglePoint(RoutePosition routePosition) {
        ljxcOverlay.getGraphics().clear();
        if (routePosition == null)
            return;
        Point point = routePosition.parasePoint();
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("positionId", routePosition.getPositionId());
        arcgisLayout.addPicToGraphicsOverlay(ljxcOverlay, picIds[2], point, attrs);
    }

    private void initMap() {
        //google地图
        WebTiledLayer titleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.VECTOR);
        //google影像图
        WebTiledLayer imgTitleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.IMAGE);
        WebTiledLayer webTitleLayer = GoogleMapLayer.createWebTiteLayer(GoogleMapLayer.Type.IMAGE_ANNOTATION);
        Basemap basemap = new Basemap();
        ArcGISMap arcGISMap = new ArcGISMap(basemap);
        arcGISMap.getOperationalLayers().add(imgTitleLayer);
        arcGISMap.getOperationalLayers().add(webTitleLayer);
//        if (defaultReservoir!=null){
//            String reservoirCode = defaultReservoir.getReservoirCode();
//            String localTpkPath = Environment.getExternalStorageDirectory() + "/SKXCDownloads/tpk/"+reservoirCode+".tpk";
//            TileCache tileCache = new TileCache(localTpkPath);
//            ArcGISTiledLayer mArcGISTiledLayer = new ArcGISTiledLayer(tileCache);
//            arcGISMap.getOperationalLayers().add(mArcGISTiledLayer);
//        }
        arcGISMap.setMinScale(ArcgisLayout.minScale);
        Point point1 = new Point(1.1992433073197773E7, 4885139.106039485, SpatialReference.create(3857));
        Point point2 = new Point(1.3532164647994766E7, 2329702.2487083403, SpatialReference.create(3857));
        Envelope initEnvelope = new Envelope(point1, point2);
        arcGISMap.setInitialViewpoint(new Viewpoint(initEnvelope));
        mapView.setMap(arcGISMap);
        locationOverlay = arcgisLayout.getLocationOverlay();
        //标准路线
        standardRouteOverlay = new GraphicsOverlay();
        //实际路线
        actualRouteOverlay = new GraphicsOverlay();
        //待巡查点
        dxcOverlay = new GraphicsOverlay();
        //已巡查点
        yxcOverlay = new GraphicsOverlay();
        //临近巡查点
        ljxcOverlay = new GraphicsOverlay();
        //巡查点文字
        textGraphics = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(standardRouteOverlay);
        mapView.getGraphicsOverlays().add(actualRouteOverlay);
        mapView.getGraphicsOverlays().add(dxcOverlay);
        mapView.getGraphicsOverlays().add(yxcOverlay);
        mapView.getGraphicsOverlays().add(ljxcOverlay);
        mapView.getGraphicsOverlays().add(textGraphics);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gaodeHelper != null) {
            gaodeHelper.stopLocation();
            gaodeHelper.closeLocation();
        }
        EventBus.getDefault().unregister(this);
    }

    private boolean isFirstLocation = true;

    @Override
    public void locationCallback(Point point) {
        if (autoLocation.isChecked()) {
            refreshPointsOnMap();
            searchNearbyRoutePosition(routePositions, point);
            arcgisLayout.setCenterPoint(point, mapView.getMapScale());
        }
        if (isFirstLocation) {
            isFirstLocation = false;
            arcgisLayout.setCenterPoint(point, 5000);
        }
        if (!taskBean.isHasExecuted()) {
            if (exeline != null) {
                int length = exeline.size();
                if (length == 1) {
                    arcgisLayout.addPic(com.example.guangdong_module.R.mipmap.ic_me_history_startpoint, exeline.get(0));
                }
                if (length > 0) {
                    Point beforPoint = exeline.get(length - 1);
                    if (beforPoint.getX() != point.getX() || beforPoint.getY() != point.getY()) {
                        arcgisLayout.addPolylineToGraphicsOverlayNew(actualRouteOverlay, beforPoint, point, SimpleLineSymbol.Style.SOLID, R.color.route_color_two, 4);
                        exeline.add(point);
                        RoutepointDataManager.getInstance().addPoint(new RoutepointDataBean(workOrderId, point.getX() + "", point.getY() + ""));
                    }
                } else {
                    exeline.add(point);
                    RoutepointDataManager.getInstance().addPoint(new RoutepointDataBean(workOrderId, point.getX() + "", point.getY() + ""));
                }
            }
        }
    }
}
