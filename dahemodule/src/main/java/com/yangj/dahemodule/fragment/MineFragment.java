package com.yangj.dahemodule.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseCommonFragment;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.activity.DangerReportListActivity;
import com.yangj.dahemodule.activity.LoginActivity;
import com.yangj.dahemodule.activity.OperatesActivity;
import com.yangj.dahemodule.activity.VersionActivity;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.user.SysUser;
import com.yangj.dahemodule.model.user.SysUserBean;
import com.yangj.dahemodule.model.user.SysUserDataBean;

/**
 * Author:xch
 * Date:2019/8/27
 * Description:我的
 */
public class MineFragment extends BaseCommonFragment {

    private TextView nameTv;
    private TextView dateTv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        nameTv = findView(R.id.tv_name);
        dateTv = findView(R.id.tv_date);
        findView(R.id.ll_one).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), OperatesActivity.class));
        });
        findView(R.id.ll_two).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DangerReportListActivity.class));
        });
        findView(R.id.ll_three).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VersionActivity.class));
        });
        findView(R.id.ll_four).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LoginActivity.class));
        });
    }

    @Override
    protected void initRequestData() {
        getUserInfo();
    }

    public void getUserInfo() {
        HttpManager.getInstance().getUserInfo()
                .subscribe(new LoadingSubject<SysUserDataBean>() {

                    @Override
                    protected void _onNext(SysUserDataBean sysUserDataBean) {
                        SysUserBean sysUserBean = sysUserDataBean.getData();
                        if (sysUserBean == null) return;
                        SysUser sysUser = sysUserBean.getSysUser();
                        nameTv.setText(sysUser.getUsername());
                        dateTv.setText("登录时间：" + sysUser.getUpdateTime());
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
