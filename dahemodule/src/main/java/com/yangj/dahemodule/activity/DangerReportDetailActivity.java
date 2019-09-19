package com.yangj.dahemodule.activity;

import android.content.Intent;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.arialyy.frame.util.TextUtil;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.utils.EmptyLayoutUtil;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.adapter.PictureAdapter;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.Report.ReportBean;
import com.yangj.dahemodule.model.Report.ReportDetailDataBean;

import java.util.Arrays;
import java.util.List;

/**
 * Author:xch
 * Date:2019/9/19
 * Description:
 */
public class DangerReportDetailActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_description;
    private TextView tv_photo_num_before;
    private RecyclerView rv_add_photo_before;

    private PictureAdapter pictureAdapter;

    private String reportId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_danger_report_detail;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            reportId = intent.getStringExtra("reportId");
        }
    }

    @Override
    public void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.tv_description);
        tv_photo_num_before = findViewById(R.id.tv_photo_num_before);
        rv_add_photo_before = findViewById(R.id.rv_add_photo_before);

        pictureAdapter = new PictureAdapter();
        rv_add_photo_before.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        rv_add_photo_before.setAdapter(pictureAdapter);
        pictureAdapter.setEmptyView(EmptyLayoutUtil.showTop("暂无图片"));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {
        loadData();
    }

    private void loadData() {
        HttpManager.getInstance().loadReportDetail(reportId)
                .subscribe(new LoadingSubject<ReportDetailDataBean>() {

                    @Override
                    protected void _onNext(ReportDetailDataBean reportDetailDataBean) {
                        if (reportDetailDataBean == null) return;
                        ReportBean reportBean = reportDetailDataBean.getData();
                        if (reportBean == null) return;
                        tv_title.setText(reportBean.getTitle());
                        tv_description.setText(reportBean.getDescription());
                        String pictures = reportBean.getPictures();
                        if (!TextUtils.isEmpty(pictures)) {
                            String[] urls = pictures.split(",");
                            pictureAdapter.setNewData(Arrays.asList(urls));
                            if (!CollectionsUtil.isEmpty(pictures)) {
                                tv_photo_num_before.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
