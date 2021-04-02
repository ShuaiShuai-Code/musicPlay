package bjfu.it.panjue.mirrormusic.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;

import bjfu.it.panjue.mirrormusic.R;
import bjfu.it.panjue.mirrormusic.activities.MeActivity;

public class BaseSearchActivity extends Activity {

    private ImageView mIvBack, mIvMe;
    private TextView mTvTitle;

    //findViewById
    protected <T extends View> T fd (@IdRes int id){
        return findViewById(id);
    }

    //初始化NavigationBar
    protected void initNavBar(boolean isShowBack, String title, boolean isShowMe){
        mIvBack = fd(R.id.iv_back);
        mIvMe = fd(R.id.iv_me);

        mIvBack.setVisibility(isShowBack ? View.VISIBLE : View.GONE);
        mIvMe.setVisibility(isShowMe ? View.VISIBLE : View.INVISIBLE);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mIvMe.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                startActivity(new Intent(BaseSearchActivity.this, MeActivity.class));
            }
        });

    }
}
