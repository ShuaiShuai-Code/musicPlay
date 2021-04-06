package bjfu.it.panjue.mirrormusic.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.List;

import bjfu.it.panjue.mirrormusic.BaseActivity.BaseActivity;
import bjfu.it.panjue.mirrormusic.R;
import bjfu.it.panjue.mirrormusic.helps.RealmHelp;
import bjfu.it.panjue.mirrormusic.models.AlbumModel;
import bjfu.it.panjue.mirrormusic.models.MusicModel;
import bjfu.it.panjue.mirrormusic.utils.MusicStopListener;
import bjfu.it.panjue.mirrormusic.utils.StringUtils;
import bjfu.it.panjue.mirrormusic.views.PlayMusicView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlayMusicActivity extends BaseActivity implements MusicStopListener {

    public static final String MUSIC_ID = "musicId";

    private ImageView mIvBg;
    private TextView mTvName, mTvAuthor;
    private PlayMusicView mPlayMusicView;
    private String mMusicId;
    private RealmHelp mRealmHelp;
    private MusicModel mMusicModel;
    private BubbleSeekBar my_seekBar;
    private TextView tv_time;
    private List<MusicModel> dataSource;
    private AlbumModel albumModel;
    private int new_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        //隐藏StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initData();
        initView();
    }

    private void initData() {
        dataSource = new ArrayList<>();
        mMusicId = getIntent().getStringExtra(MUSIC_ID);
        mRealmHelp = new RealmHelp();
        mMusicModel = mRealmHelp.getMusic(mMusicId);
        albumModel = mRealmHelp.getAlbum("1");
        dataSource = albumModel.getList();

        for (int i = 0; i < dataSource.size(); i++) {
            if (dataSource.get(i).getName().equals(mMusicModel.getName())) {
                new_position = i;
            }
        }
    }

    //下一页
    private void pageNext(int position) {
        new_position = position;
        mMusicId = dataSource.get(position).getMusicId();
        mRealmHelp = new RealmHelp();
        mMusicModel = dataSource.get(position);
        Glide.with(this)
                .load(mMusicModel.getPoster())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 10)))
                .into(mIvBg);
        mTvName.setText(mMusicModel.getName());
        mTvAuthor.setText(mMusicModel.getAuthor());
        mPlayMusicView.stopMusic();
        mPlayMusicView.setMusic(mMusicModel);
        mPlayMusicView.playMusic();
        mPlayMusicView.seekTo(0);
        handler.sendEmptyMessage(1);
    }

    //上一页
    private void pagePrevi(int position) {
        new_position = position;
        mMusicId = dataSource.get(position).getMusicId();
        mRealmHelp = new RealmHelp();
        mMusicModel = dataSource.get(position);
        ;
        Glide.with(this)
                .load(mMusicModel.getPoster())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 10)))
                .into(mIvBg);
        mTvName.setText(mMusicModel.getName());
        mTvAuthor.setText(mMusicModel.getAuthor());
        mPlayMusicView.stopMusic();
        mPlayMusicView.setMusic(mMusicModel);
        mPlayMusicView.playMusic();
        mPlayMusicView.seekTo(0);
        handler.sendEmptyMessage(1);
    }

    private void initView() {

        mIvBg = fd(R.id.iv_bg);
        mTvName = fd(R.id.tv_name);
        mTvAuthor = fd(R.id.tv_author);
        my_seekBar = fd(R.id.my_seekBar);
        //可以实现拖动进度条，让歌曲在进度条位置播放，调用seekTo（方法）
        my_seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
//                Log.i("时长+++++Position",mPlayMusicView.getCurrentPosition()+"");
//                Log.i("时长+++++Duration",mPlayMusicView.getDuration()+"");
                if (mPlayMusicView.getCurrentPosition()>=mPlayMusicView.getDuration()) {
                    if (new_position >= dataSource.size() - 1) {
                        Toast.makeText(PlayMusicActivity.this, "目前是列表最后一曲", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    pageNext(new_position + 1);
                }
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                //
                int time = (int) (mPlayMusicView.getDuration() * progressFloat / 1000);
                //my_seekBar.setProgress((float) (player.position() * 1000 / player.duration()));
                //tv_time.setText(StringUtils.getTime((int) player.position() * 1000));
                mPlayMusicView.seekTo(time);

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });
        tv_time = fd(R.id.tv_time);

        //glide-transformations
        Glide.with(this)
                .load(mMusicModel.getPoster())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 10)))
                .into(mIvBg);
        mTvName.setText(mMusicModel.getName());
        mTvAuthor.setText(mMusicModel.getAuthor());

        mPlayMusicView = fd(R.id.play_music_view);
        //mPlayMusicView.setMusicIcon(mMusicModel.getPoster());
        mPlayMusicView.setMusic(mMusicModel);
        mPlayMusicView.playMusic();
        mPlayMusicView.setListenerStop(this);
        handler.sendEmptyMessage(1);
        //下一曲
        findViewById(R.id.tv_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_position >= dataSource.size() - 1) {
                    Toast.makeText(PlayMusicActivity.this, "目前是列表最后一曲", Toast.LENGTH_SHORT).show();
                    return;
                }
                pageNext(new_position + 1);
            }
        });
        //上一曲
        findViewById(R.id.tv_previ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_position == 0) {
                    Toast.makeText(PlayMusicActivity.this, "目前是列表第一首", Toast.LENGTH_SHORT).show();
                    return;
                }
                pagePrevi(new_position - 1);
            }
        });


    }

    //后退按钮点击事件
    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayMusicView.destroy();
        mRealmHelp.close();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mPlayMusicView.getPlaying() && msg.what == 1) {

                int duration = mPlayMusicView.getDuration();

                if (duration <= 0) {
                    handler.sendEmptyMessage(1);
                    return;
                }
                int current = mPlayMusicView.getCurrentPosition();

                if (current <= 0) {
                    handler.sendEmptyMessage(1);
                    return;
                }
                float process = current / (float) duration;

                float realProgress = process * 1000;

                my_seekBar.setProgress(realProgress);
                handler.sendEmptyMessageDelayed(1, 100);
                tv_time.setText(StringUtils.getTime(current) + "/" + StringUtils.getTime(duration));
            }
        }
    };

    @Override
    public void getState(boolean isPlaying) {
        if (isPlaying)
            handler.sendEmptyMessage(1);
    }
}
