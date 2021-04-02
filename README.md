#封装播放器

![001.jpg](https://github.com/gethub-json/musicPlay/blob/master/001.jpg)

![002.jpg](https://github.com/gethub-json/musicPlay/blob/master/001.jpg)

```
 //1.setPath;当前需要播放的音乐
    public void setPath(String path) {
        /*1.音乐正在播放，重置音乐播放状态
          2.设置音乐播放路径
          3.准备播放
         */

        //1、音乐正在播放，重置音乐播放状态
        if (mMediaPlayer.isPlaying() || !path.equals(mPath)) {
            mMediaPlayer.reset();
        }
        mPath = path;

        //2、设置播放音乐路径
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3.准备播放
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (onMeidaPlayerHelperListener != null) {
                    onMeidaPlayerHelperListener.onPrepared(mp);
                }
            }
        });

        //监听音乐播放完成
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (onMeidaPlayerHelperListener != null) {
                    onMeidaPlayerHelperListener.onCompletion(mp);
                }
            }
        });
    }
```
#封装播放器必用方法
//返回正在播放的音乐路径

    public String getPath() {
        return mPath;
    }

    //2.start:播放音乐
    public void start() {
        if (mMediaPlayer.isPlaying()) return;
        ;
        mMediaPlayer.start();
    }

    //3.pause:暂停播放
    public void pause() {
        mMediaPlayer.pause();
    }
#封装播放器常用方法

     /**
     * @return 获取时间 毫秒
     */
    public int getDuration() {

        return mMediaPlayer.getDuration();
    }

    /**
     * @return 获取当前流媒体的播放的位置，单位是毫秒
     */
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * @return 设置当前MediaPlayer的播放位置，单位是毫秒
     */
    public void seekTo(int position) {
        mMediaPlayer.seekTo(position);
    }

