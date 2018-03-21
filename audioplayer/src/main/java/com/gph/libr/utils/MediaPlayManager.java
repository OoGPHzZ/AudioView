package com.gph.libr.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;

/**
 *
 * @author guopenghui-961057759@qq.com
 * @date 2018/2/27 0027
 */

public class MediaPlayManager implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, Runnable, MediaPlayer.OnBufferingUpdateListener {
    private static final long DELAY_MILLIS = 100;
    private MediaPlayer mMediaPlayer;
    private int totalTime;
    private Handler mHandler;

    private OnPlayerStateListener mOnPlayerStateListener;
    private OnErrorListener mOnErrorListener;
    private OnCompletionListener mOnCompletionListener;
    private OnBufferingListener mOnBufferingListener;
    private OnPlayerProgressListener mOnPlayerProgressListener;

    private static MediaPlayManager instance;

    private MediaPlayManager() {
    }

    public static MediaPlayManager getInstance() {
        if (null == instance) {
            instance = new MediaPlayManager();
        }
        return instance;
    }

    /**
     * 开始定时任务
     */
    private void startPoolTimer() {
        if (null == mHandler) {
            mHandler = new Handler();
        }
        stopPoolTimer();
        mHandler.postDelayed(this, DELAY_MILLIS);
    }

    /**
     * 停止定时任务
     */
    private void stopPoolTimer() {
        if (null != mHandler) {
            mHandler.removeCallbacks(this);
        }
    }

    /**
     * 初始化播放器
     *
     * @param url
     */
    public void setData(String url) {
        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
        }
        if (null != mOnBufferingListener) {
            mOnBufferingListener.onBufferStart(mMediaPlayer);
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(EncodeUtils.toUrlEncode(url));
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void seekTo(int progress) {
        if(null != mMediaPlayer){
            mMediaPlayer.seekTo(progress);
        }
    }

    /**
     * 播放
     */
    public void play() {
        if (null != mMediaPlayer && !mMediaPlayer.isPlaying()) {
            startPoolTimer();
            mMediaPlayer.start();
            if (null != mOnPlayerStateListener) {
                mOnPlayerStateListener.onMediaPlayerState(true);
            }
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            if (null != mOnPlayerStateListener) {
                mOnPlayerStateListener.onMediaPlayerState(false);
            }
        }
        stopPoolTimer();
    }

    /**
     * 停止及释放
     */
    public void stop() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        stopPoolTimer();
        if (null != mOnPlayerStateListener) {
            mOnPlayerStateListener.onMediaPlayerState(false);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (null != mOnErrorListener) {
            mOnErrorListener.onError(mp, what, extra);
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        totalTime = mp.getDuration();
        if (null != mOnBufferingListener) {
            mOnBufferingListener.onBufferCompletion(mp);
        }
        if (null != mOnPlayerStateListener) {
            mOnPlayerStateListener.onPrepared();
        }
        if (null != mOnPlayerProgressListener) {
            mOnPlayerProgressListener.onProgress(0);
            mOnPlayerProgressListener.onDuration(totalTime);
        }
        play();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        stop();
        if (null != mOnPlayerProgressListener) {
            mOnPlayerProgressListener.onProgress(totalTime);
        }
        if (null != mOnCompletionListener) {
            mOnCompletionListener.onCompletion(mp);
        }
        if (null != mOnPlayerStateListener) {
            mOnPlayerStateListener.onMediaPlayerState(false);
        }
    }

    @Override
    public void run() {
        int current = 0;
        if (null != mMediaPlayer) {
            current = mMediaPlayer.getCurrentPosition();
        }
        if (null != mOnPlayerProgressListener) {
            mOnPlayerProgressListener.onProgress(current);
        }
        mHandler.postDelayed(this, DELAY_MILLIS);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (null != mOnBufferingListener) {
            mOnBufferingListener.onBufferingUpdate(mp, percent);
        }
    }

    public interface OnErrorListener {
        /**
         * 播放错误
         */
        void onError(MediaPlayer mp, int what, int extra);
    }

    public interface OnCompletionListener {
        /**
         * 播放完成
         */
        void onCompletion(MediaPlayer mp);
    }

    public interface OnBufferingListener {
        /**
         * 缓冲开始
         */
        void onBufferStart(MediaPlayer mp);

        /**
         * 缓冲中
         *
         * @param percent 缓冲百分比
         */
        void onBufferingUpdate(MediaPlayer mp, int percent);

        /**
         * 缓冲完成
         */
        void onBufferCompletion(MediaPlayer mp);
    }

    public interface OnPlayerProgressListener {
        /**
         * 总时间
         */
        void onDuration(int duration);

        /**
         * 当前播放进度
         */
        void onProgress(int progress);
    }

    public interface OnPlayerStateListener {
        /**
         * 播放状态
         *
         * @param play 播放 / 未播放
         */
        void onMediaPlayerState(boolean play);

        /**
         * view对mp的操作全部放在prepared后
         */
        void onPrepared();
    }

    public void setErrorListener(OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public void setCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public void setBufferingListener(OnBufferingListener listener) {
        this.mOnBufferingListener = listener;
    }

    /**
     * 动态设置view对应的seekBar
     */
    public void setPlayerProgressListener(OnPlayerProgressListener listener) {
        this.mOnPlayerProgressListener = listener;
    }

    /**
     * 动态设置view对应的状态
     */
    public void setPlayerStateListener(OnPlayerStateListener listener) {
        this.mOnPlayerStateListener = listener;
    }
}
