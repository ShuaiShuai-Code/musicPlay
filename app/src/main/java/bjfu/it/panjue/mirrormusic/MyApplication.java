package bjfu.it.panjue.mirrormusic;

import android.app.Application;
import com.blankj.utilcode.util.Utils;

import bjfu.it.panjue.mirrormusic.helps.RealmHelp;
import io.realm.Realm;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        Realm.init(this);

        RealmHelp.migration();
    }
}
