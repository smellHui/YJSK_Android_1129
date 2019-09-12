package com.yangj.dahemodule.activity;

import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.tepia.base.http.LoadingSubject;
import com.tepia.base.mvp.BaseActivity;
import com.tepia.base.utils.LogUtil;
import com.yangj.dahemodule.R;
import com.yangj.dahemodule.common.UserManager;
import com.yangj.dahemodule.model.UserBean;
import com.yangj.dahemodule.model.UserDataBean;
import com.yangj.dahemodule.model.UserLoginResponse;
import com.yangj.dahemodule.util.AesUtils;

/**
 * Author:xch
 * Date:2019/8/29
 * Description:
 */
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
                UserManager.getInstance().login(phone, AesUtils.encrypt(password))
                        .subscribe(new LoadingSubject<UserDataBean>(true, "正在登录...") {
                            @Override
                            protected void _onNext(UserDataBean userLoginResponse) {
                                LogUtil.v("userLoginResponse--->" + userLoginResponse.toString());
                                if (userLoginResponse == null) return;
                                UserBean userBean = userLoginResponse.getData();
                                if (userBean == null) return;
                                UserManager.getInstance().saveUser(JSON.toJSONString(userBean));
                            }

                            @Override
                            protected void _onError(String message) {
                                LogUtil.e("userLoginResponse--->" + message);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
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
