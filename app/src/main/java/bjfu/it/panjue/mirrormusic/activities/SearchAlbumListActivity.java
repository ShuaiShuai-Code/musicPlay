package bjfu.it.panjue.mirrormusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bjfu.it.panjue.mirrormusic.BaseActivity.BaseActivity;
import bjfu.it.panjue.mirrormusic.BaseActivity.BaseSearchActivity;
import bjfu.it.panjue.mirrormusic.R;
import bjfu.it.panjue.mirrormusic.adapters.MusicListAdapter;
import bjfu.it.panjue.mirrormusic.helps.RealmHelp;
import bjfu.it.panjue.mirrormusic.models.AlbumModel;
import bjfu.it.panjue.mirrormusic.models.MusicModel;

public class SearchAlbumListActivity extends BaseSearchActivity {

    public static final String ALBUM_ID = "album_id";

    private RecyclerView mRvList;
    private MusicListAdapter mAdapter;
    private RealmHelp mRealmHelp;
    private AlbumModel mAlbumModel;
    private EditText edit_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        initData();
        initView();
    }

    private void initData() {
        mRealmHelp = new RealmHelp();
        mAlbumModel = mRealmHelp.getAlbum("1");
    }

    private void initView() {
        initNavBar(true, "搜索结果", false);

        mRvList = fd(R.id.rv_list);
        edit_search = fd(R.id.edit_search);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        String ketWorld=getIntent().getStringExtra("keyWord");
        edit_search.setText(ketWorld);

        mAdapter = new MusicListAdapter(this, null, search(ketWorld,mAlbumModel.getList()));
        mRvList.setAdapter(mAdapter);

        findViewById(R.id.txt_Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SearchAlbumListActivity.this, "搜索", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static ArrayList<MusicModel> search(String name, List<MusicModel> list) {
        ArrayList<MusicModel> results = new ArrayList();
        Pattern pattern = Pattern.compile(name,Pattern.CASE_INSENSITIVE);
        Pattern patternName = Pattern.compile(name,Pattern.CASE_INSENSITIVE);
//      如果要求大小写不敏感，改成：
//      Pattern pattern = Pattern.compile(name,Pattern.CASE_INSENSITIVE);
        for (int i = 0; i < list.size(); i++) {
            Matcher matcher = pattern.matcher((list.get(i)).getName());
            Matcher matcherName = patternName.matcher((list.get(i)).getAuthor());
            //匹配查询
            //matcher.matches()
            if (matcher.find()||matcherName.find()) {
                results.add(list.get(i));
            }
        }
        return results;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmHelp.close();
    }
}
