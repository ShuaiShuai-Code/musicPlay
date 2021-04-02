package bjfu.it.panjue.mirrormusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import bjfu.it.panjue.mirrormusic.BaseActivity.BaseActivity;
import bjfu.it.panjue.mirrormusic.BaseActivity.BaseSearchActivity;
import bjfu.it.panjue.mirrormusic.R;
import bjfu.it.panjue.mirrormusic.adapters.MusicGridAdapter;
import bjfu.it.panjue.mirrormusic.adapters.MusicListAdapter;
import bjfu.it.panjue.mirrormusic.helps.RealmHelp;
import bjfu.it.panjue.mirrormusic.models.MusicSourceModel;
import bjfu.it.panjue.mirrormusic.utils.DataUtils;
import bjfu.it.panjue.mirrormusic.views.GridSpaceItemDecoration;

public class MainActivity extends BaseSearchActivity {

    private RecyclerView mRvGrid, mRvList;
    private MusicGridAdapter mGridAdapter;
    private MusicListAdapter mListAdapter;
    private RealmHelp mRealmHelp;
    private MusicSourceModel mMusicSourceModel;
    private MusicSourceModel musicModel;
    private EditText edit_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmHelp.close();
    }

    private void initData() {

        mRealmHelp = new RealmHelp();
        mRealmHelp.setMusicSource(getApplicationContext());
        mMusicSourceModel = mRealmHelp.getMusicSource();
        String musicSourceJson = DataUtils.getJsonFromAssets(MainActivity.this, "DataSource.json");
        musicModel = new Gson().fromJson(musicSourceJson, MusicSourceModel.class);
    }

    private void initView() {
        initNavBar(false, "mirrorMusic", true);
        mRvGrid = fd(R.id.rv_grid);
        mRvGrid.setLayoutManager(new GridLayoutManager(this, 3));
        mRvGrid.addItemDecoration(new GridSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.albumMarginSize), mRvGrid));
        mRvGrid.setNestedScrollingEnabled(false);
        mGridAdapter = new MusicGridAdapter(this, musicModel.getAlbum());
        mRvGrid.setAdapter(mGridAdapter);

        //1.已知列表高度的情况下，可以直接在布局中把RecycleView的高度定义上
        //2.不知列表高度的情况下，需要手动计算RecycleView的高度
        mRvList = fd(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvList.setNestedScrollingEnabled(false);
        mListAdapter = new MusicListAdapter(this, mRvList, musicModel.getHot());
        mRvList.setAdapter(mListAdapter);

        //搜索相关
        edit_search = findViewById(R.id.edit_search);
        findViewById(R.id.txt_Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWord = edit_search.getText().toString();
                if (!TextUtils.isEmpty(keyWord)) {
                    Intent intent = new Intent(MainActivity.this, SearchAlbumListActivity.class);
                    intent.putExtra("keyWord", keyWord);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "请输入关键字", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
