package com.yangj.dahemodule.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseCommonFragment;
import com.tepia.base.utils.AppManager;
import com.tepia.guangdong_module.amainguangdong.LoginOfGDActivity;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.xunchaview.fragment.TabMainFragmentFactory;
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
            loginOutClick();
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

    private void loginOutClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
        builder.setMessage(com.example.guangdong_module.R.string.exit_message);
        builder.setCancelable(true);
        builder.setPositiveButton(com.example.guangdong_module.R.string.sure, (dialog, which) -> {
            UserManager.getInstance().clearCacheAndStopPush();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            AppManager.getInstance().finishAll();
        });
        builder.setNegativeButton(com.example.guangdong_module.R.string.cancel, (dialog, which) -> {

        });
        builder.create().show();
    }
}
