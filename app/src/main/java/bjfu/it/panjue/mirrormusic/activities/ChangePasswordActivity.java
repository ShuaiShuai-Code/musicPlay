package bjfu.it.panjue.mirrormusic.activities;

import android.os.Bundle;
import android.view.View;

import bjfu.it.panjue.mirrormusic.BaseActivity.BaseActivity;
import bjfu.it.panjue.mirrormusic.R;
import bjfu.it.panjue.mirrormusic.utils.UserUtils;
import bjfu.it.panjue.mirrormusic.views.InputView;

public class ChangePasswordActivity extends BaseActivity {

    private InputView mOldPasseord, mPassword, mPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
    }

    private void initView(){
        initNavBar(true, "修改密码", false);

        mOldPasseord = fd(R.id.input_old_password);
        mPassword = fd(R.id.input_password);
        mPasswordConfirm = fd(R.id.input_password_confirm);
    }

    public void onChangePasswordClick(View v){
        String oldPassword = mOldPasseord.getInputStr();
        String password = mPassword.getInputStr();
        String passwordConfirm = mPasswordConfirm.getInputStr();

        boolean result = UserUtils.changePassword(this, oldPassword, password, passwordConfirm);
        if(!result){
            return;
        }

        UserUtils.logout(this);
    }
}
