package bjfu.it.panjue.mirrormusic.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.xw.repo.BubbleSeekBar;

import bjfu.it.panjue.mirrormusic.R;
import bjfu.it.panjue.mirrormusic.helps.MediaPlayerHelp;
import bjfu.it.panjue.mirrormusic.models.MusicModel;
import bjfu.it.panjue.mirrormusic.services.MusicService;
import bjfu.it.panjue.mirrormusic.utils.MusicStopListener;

public class PlayMusicView extends FrameLayout {

    private Context mContext;

    private Intent mServiceIntent;
    private MusicModel mMusicModel;
    public MusicService.MusicBind mMusicBind;
    private boolean isPlaying, isBindService;
    private View mView;
    private FrameLayout mFlPlayMusic;
    private ImageView mIvIcon, mIvNeedle, mIvPlay;
    private MusicStopListener musicStopListener;

    private Animation mPlayMusicAnim, mPlayNeedleAnim, mStopNeedleAnim;

    public PlayMusicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlayMusicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        //MediaPlayer

        mContext = context;

        mView = LayoutInflater.from(mContext).inflate(R.layout.play_music, this, false);

        mFlPlayMusic = mView.findViewById(R.id.fl_play_music);
        mFlPlayMusic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trigger();
            }
        });
        mIvIcon = mView.findViewById(R.id.iv_icon);
        mIvNeedle = mView.findViewById(R.id.iv_needle);
        mIvPlay = mView.findViewById(R.id.iv_play);


        /*1.定义所需要执行的动画
          (1)光盘转动的动画
          (2)指针指向光盘的动画
          (3)指针离开光盘的动画
          2.startAnimation
         */
        mPlayMusicAnim = AnimationUtils.loadAnimation(mContext, R.anim.play_misic_anim);
        mPlayNeedleAnim = AnimationUtils.loadAnimation(mContext, R.anim.play_needle_anim);
        mStopNeedleAnim = AnimationUtils.loadAnimation(mContext, R.anim.stop_needle_anim);

        addView(mView);


//可以实现拖动进度条，让歌曲在进度条位置播放，调用seekTo（方法）
//        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
//            @Override
//            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
//
//            }
//
//            @Override
//            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
//                changePlayerProgress(progressFloat);
//
//            }
//
//            @Override
//            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
//
//            }
//        });
    }

    //切换播放状态
    private void trigger() {
        if (isPlaying) {
            stopMusic();
        } else {
            playMusic();
        }
    }

    //播放音乐
    public void playMusic() {
        isPlaying = true;
        if (null != musicStopListener)
            musicStopListener.getState(isPlaying);
        mIvPlay.setVisibility(View.GONE);
        mFlPlayMusic.startAnimation(mPlayMusicAnim);
        mIvNeedle.startAnimation(mPlayNeedleAnim);

        //启动服务
        startMusicService();
    }

    //停止音乐
    public void stopMusic() {
        isPlaying = false;
        if (null != musicStopListener)
            musicStopListener.getState(isPlaying);
        mIvPlay.setVisibility(View.VISIBLE);
        mFlPlayMusic.clearAnimation();
        mIvNeedle.startAnimation(mStopNeedleAnim);

        mMusicBind.stopMusic();
    }

    public boolean getPlaying() {
        return isPlaying;
    }

    public void setListenerStop(MusicStopListener musicStopListener) {
        this.musicStopListener = musicStopListener;
    }

    //设置光盘中显示的音乐封面图片
    public void setMusicIcon() {

        Glide.with(mContext)
                .load(mMusicModel.getPoster())
                .into(mIvIcon);
    }

    //设置音乐播放模型
    public void setMusic(MusicModel musicModel) {
        this.mMusicModel = musicModel;
        if (null != mMusicBind)
            mMusicBind.setMusic(mMusicModel);
        setMusicIcon();
    }


    //启动音乐服务
    private void startMusicService() {
        //启动service
        if (mServiceIntent == null) {
            mServiceIntent = new Intent(mContext, MusicService.class);
            mContext.startService(mServiceIntent);
        } else {
            mMusicBind.playMusic();
        }

        //当前未绑定，绑定服务，同时修改绑定状态
        if (!isBindService) {
            isBindService = true;
            mContext.bindService(mServiceIntent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    //销毁方法，需要在 activity 被销毁的时候调用
    public void destroy() {
        //如果已绑定服务，则解除绑定，同时修改绑定状态
        if (isBindService) {
            isBindService = false;
            mContext.unbindService(conn);
        }

    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBind = (MusicService.MusicBind) service;
            mMusicBind.setMusic(mMusicModel);
            mMusicBind.playMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    //获取时间
    public int getDuration() {
        if (null == mMusicBind)
            return 0;
        return mMusicBind.getDuration();
    }

    //获取当前位置
    public int getCurrentPosition() {
        if (null == mMusicBind)
            return 0;
        return mMusicBind.getCurrentPosition();
    }

    //播放位置
    public void seekTo(int position) {
        if (null != mMusicBind)
            mMusicBind.seekTo(position);
    }

}
