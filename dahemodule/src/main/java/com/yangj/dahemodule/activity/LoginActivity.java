package com.yangj.dahemodule.activity;

import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.tepia.base.AppRoutePath;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.LogUtil;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.HttpManager;
import com.yangj.dahemodule.model.UserBean;
import com.yangj.dahemodule.model.UserDataBean;
import com.yangj.dahemodule.model.user.RolesBean;
import com.yangj.dahemodule.model.user.SysUser;
import com.yangj.dahemodule.model.user.SysUserBean;
import com.yangj.dahemodule.model.user.SysUserDataBean;
import com.yangj.dahemodule.util.AesUtils;

import java.util.List;

/**
 * Author:xch
 * Date:2019/8/29
 * Description:
 */
@Route(path = AppRoutePath.app_dahe_login)
public class LoginActivity extends BaseActivity {
    private EditText phoneEt;
    private EditText passwordEt;
    private Button loginBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        phoneEt = findViewById(R.id.et_phone);
        passwordEt = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);

        phoneEt.setText("dhxc");
        passwordEt.setText("123456");
        loginBtn.setOnClickListener(v -> {
            String phone = phoneEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();
            try {
                HttpManager.getInstance().login(phone, AesUtils.encrypt(password))
                        .subscribe(new LoadingSubject<UserDataBean>(true, "正在登录...") {
                            @Override
                            protected void _onNext(UserDataBean userLoginResponse) {
                                if (userLoginResponse == null) return;
                                UserBean userBean = userLoginResponse.getData();
                                if (userBean == null) return;
                                HttpManager.getInstance().saveUser(JSON.toJSONString(userBean));
                                UserManager.getInstance().saveToken(userBean.getAccess_token());
                                UserManager.getInstance().saveUserCode(phone);
                                loadUserInfo();
                            }

                            @Override
                            protected void _onError(String message) {
                                LogUtil.e("userLoginResponse--->" + message);
                                finish();
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                                finish();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadUserInfo() {
        HttpManager.getInstance().getUserInfo()
                .subscribe(new LoadingSubject<SysUserDataBean>() {

                    @Override
                    protected void _onNext(SysUserDataBean sysUserDataBean) {
                        SysUserBean sysUserBean = sysUserDataBean.getData();
                        if (sysUserBean == null) return;
                        SysUser sysUser = sysUserBean.getSysUser();
                        if (sysUser != null) {
                            HttpManager.getInstance().saveSysUser(JSON.toJSONString(sysUser));
                        }
                        List<RolesBean> roles = sysUserBean.getRoles();
                        if (!CollectionsUtil.isEmpty(roles)) {
                            HttpManager.getInstance().saveRolesBean(JSON.toJSONString(roles.get(0)));
                        }
                        ARouter.getInstance().build(AppRoutePath.app_dahe_main).navigation();
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initRequestData() {

    }
}
