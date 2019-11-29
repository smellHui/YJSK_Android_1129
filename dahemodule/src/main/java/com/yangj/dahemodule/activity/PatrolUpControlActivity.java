package com.yangj.dahemodule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bilibili.boxing_impl.view.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.view.dialog.permissiondialog.Px2dpUtils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.RoutePosition;
import com.tepia.guangdong_module.amainguangdong.route.TaskItemBean;
import com.tepia.guangdong_module.amainguangdong.utils.SqlManager;
import com.tepia.guangdong_module.amainguangdong.wrap.PatroltemEvent;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.adapter.PartCardAdapter;
import com.yangj.dahemodule.adapter.PatrolControlAdapter;
import com.yangj.dahemodule.util.UiHelper;
import com.yangj.dahemodule.view.ForecastView;
import com.yangj.dahemodule.view.WrapLayoutManager;
import com.yangj.dahemodule.wrap.PatrolWrap;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;


/**
 * Author:xch
 * Date:2019/11/19
 * Description:巡查点操作页-上
 */
public class PatrolUpControlActivity extends BaseActivity implements
        DiscreteScrollView.OnItemChangedListener,
        DiscreteScrollView.ScrollStateChangeListener {

    public static final String PATROL_INDEX = "patrol_index";
    public static final String IS_COMPLETE_OF_TASKBEAN = "is_Complete_Of_TaskBean";
    public static final String ROUTE_POSITIONS = "route_positions";

    private boolean isCompleteOfTaskBean;
    private int patrolIndex;
    private List<RoutePosition> routePositions;
    private DiscreteScrollView partPicker;
    private ForecastView forecastView;
    private PartCardAdapter partCardAdapter;

    private RecyclerView rv;
    private PatrolControlAdapter patrolControlAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_patrol_up_control;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                patrolIndex = bundle.getInt(PATROL_INDEX, -1);
                isCompleteOfTaskBean = bundle.getBoolean(IS_COMPLETE_OF_TASKBEAN, false);
                routePositions = (List<RoutePosition>) intent.getSerializableExtra(ROUTE_POSITIONS);
            }
        }
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        forecastView = findViewById(R.id.view_forecast);
        rv = findViewById(R.id.rv);
        findViewById(R.id.tv_left_text).setOnClickListener(v -> onBackPressed());
        patrolControlAdapter = new PatrolControlAdapter(isCompleteOfTaskBean);
        patrolControlAdapter.setOnItemChildClickListener(this::onItemChildClick);
        rv.setLayoutManager(new WrapLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new SpacesItemDecoration(4));
        int zeroPx = Px2dpUtils.dip2px(getContext(), 0);
        rv.setPadding(zeroPx, zeroPx, zeroPx, zeroPx);
        rv.setAdapter(patrolControlAdapter);

        partPicker = findViewById(R.id.part_picker);
        partPicker.setSlideOnFling(true);
        partCardAdapter = new PartCardAdapter();
        partPicker.setAdapter(partCardAdapter);
        partPicker.addOnItemChangedListener(this);
        partPicker.addScrollStateChangeListener(this);
        partPicker.setOrientation(DSVOrientation.HORIZONTAL);
        partPicker.setItemTransitionTimeMillis(400);
        partPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        partPicker.scrollToPosition(patrolIndex);

        if (!CollectionsUtil.isEmpty(routePositions)) {
            partCardAdapter.setNewData(routePositions);
            forecastView.setForecast(routePositions.get(0));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPatrolItem(PatroltemEvent patroltemEvent) {
        int patrolPosition = patroltemEvent.getPosition();
        String problemDescription = patroltemEvent.getProblemDescription();
        String imgPaths = patroltemEvent.getImgPaths();
        TaskItemBean taskItemBean = patrolControlAdapter.getItem(patrolPosition);
        if (taskItemBean == null) return;
        taskItemBean.setExecuteResultDescription(problemDescription);
        taskItemBean.setBeforelist(imgPaths);
        updateStatuClick(taskItemBean, "1", (rowsAffected) -> {
            controlCallback(patrolPosition);
        });
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
            this.patrolIndex = position;
            RoutePosition routePosition = routePositions.get(position);
            if (routePosition == null) {
                patrolControlAdapter.setNewData(null);
                return;
            }
            List<TaskItemBean> taskItemBeans = SqlManager.getInstance().queryTaskItem(routePosition.getWorkOrderId(), routePosition.getPositionId());
            patrolControlAdapter.setNewData(taskItemBeans);
        }
    }

    @Override
    public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

    }

    @Override
    public void onScroll(float position, int currentIndex, int newIndex, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
        RoutePosition current = routePositions.get(currentIndex);
        if (newIndex >= 0 && newIndex < partCardAdapter.getItemCount()) {
            RoutePosition next = routePositions.get(newIndex);
            forecastView.onScroll(1f - Math.abs(position), current, next);
        }
    }

    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        TaskItemBean taskItemBean = (TaskItemBean) adapter.getItem(position);
        if (taskItemBean == null) return;
        int id = view.getId();
        if (id == R.id.btn_normal) {
            updateStatuClick(taskItemBean, "0", (rowsAffected) -> {
                controlCallback(position);
            });
        }
        if (id == R.id.btn_abnormal) {
            UiHelper.goToTroubleRecordView(getContext(), taskItemBean, position, "1");
        }
        if (id == R.id.btn_to_normal) {
            updateStatuClick(taskItemBean, "0", (rowsAffected) -> {
                controlCallback(position);
            });
        }
        if (id == R.id.btn_to_abnormal) {
            UiHelper.goToTroubleRecordView(getContext(), taskItemBean, position, "1");
        }
        if (id == R.id.drag_item) {
            if (!taskItemBean.isNormal() && taskItemBean.getExecuteResultType() != null) {
                UiHelper.goToTroubleRecordView(getContext(), taskItemBean, position, "1");
            }
        }
    }

    /**
     * 更新数据库数据，然后返回回调方法
     *
     * @param taskItemBean
     * @param executeResultType
     * @param updateOrDeleteCallback
     */
    private void updateStatuClick(@NonNull TaskItemBean taskItemBean, String executeResultType, UpdateOrDeleteCallback updateOrDeleteCallback) {
        taskItemBean.setExecuteResultType(executeResultType);
        taskItemBean.setCompleteStatus("1");
        SqlManager.getInstance().updateTaskItemAsyn(taskItemBean, updateOrDeleteCallback);
    }

    private void controlCallback(int position) {
        patrolControlAdapter.notifyItemChanged(position);
        partCardAdapter.notifyItemChanged(patrolIndex);
        forecastView.setForecast(routePositions.get(patrolIndex));
        EventBus.getDefault().post(new PatrolWrap(patrolIndex));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
