package com.gph.libr;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gph.libr.utils.AudioUiUtils;
import com.gph.libr.utils.MediaPlayManager;

/**
 * @author guopenghui-961057759@qq.com
 * @date 2018/2/27 0027
 */

public class AudioView extends FrameLayout implements View.OnClickListener, MediaPlayManager.OnBufferingListener, MediaPlayManager.OnCompletionListener, MediaPlayManager.OnErrorListener, MediaPlayManager.OnPlayerProgressListener, MediaPlayManager.OnPlayerStateListener, SeekBar.OnSeekBarChangeListener {
    private Activity mActivity;

    private AppCompatSeekBar mSeekBar;
    private TextView mCurrent;
    private TextView mDuration;
    private ImageView mPlay;
    private RelativeLayout mBuffer;
    private TextView mPercent;
    private RelativeLayout mError;
    private String mUrl;
    private MediaPlayManager mManager;
    private int mSeekBarProgress;
    private OnPlayCompletion onPlayCompletion;
    private OnReleaseOtherPlayer onReleaseOtherPlayer;
    /**
     * 是否允许拖动进度条播放
     */
    private boolean mEnableSeekTo = true;
    private boolean isPrepared;

    public AudioView(@NonNull Context context) {
        this(context, null);
    }

    public AudioView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        initView();
    }

    private void initView() {
        inflate(mActivity, R.layout.view_audio_layout, this);
        mCurrent = findViewById(R.id.current);
        mSeekBar = findViewById(R.id.seek_bar);
        mDuration = findViewById(R.id.duration);
        mBuffer = findViewById(R.id.buffer);
        mError = findViewById(R.id.error);
        mPercent = findViewById(R.id.percent);
        mPlay = findViewById(R.id.play);
        mPlay.setOnClickListener(this);
        mError.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);

        mManager = MediaPlayManager.getInstance();
    }

    /**
     * 初始化配置
     *
     * @param resourceUrl 媒体资源路径
     * @param totalTime   资源总时长
     */
    public void init(String resourceUrl, String totalTime) {
        this.mUrl = resourceUrl;
        AudioUiUtils.getInstance().addViewToList(this);
        mDuration.setText(totalTime);
    }

    /**
     * 重置UI的播放状态为初始状态
     */
    public void resetDefaultUi() {
        mManager.setBufferingListener(null);
        mManager.setCompletionListener(null);
        mManager.setErrorListener(null);
        mManager.setPlayerProgressListener(null);
        mManager.setPlayerStateListener(null);
        mPlay.setSelected(false);
        mSeekBar.setProgress(0);
        mCurrent.setText("00:00");
        isPrepared = false;
    }

    /**
     * 设置是否可以拖动播放，默认可以
     *
     * @param enableSeekTo
     */
    public void setEnableSeekTo(boolean enableSeekTo) {
        mEnableSeekTo = enableSeekTo;
    }

    /**
     * 监听播放完成事件
     *
     * @param onPlayCompletion
     */
    public void setOnPlayCompletion(OnPlayCompletion onPlayCompletion) {
        this.onPlayCompletion = onPlayCompletion;
    }

    /**
     * 监听释放其他正在播放的软件
     *
     * @param onReleaseOtherPlayer
     */
    public void setOnReleaseOtherPlayer(OnReleaseOtherPlayer onReleaseOtherPlayer) {
        this.onReleaseOtherPlayer = onReleaseOtherPlayer;
    }

    /**
     * 释放播放器以及初始化所有UI
     */
    public void releaseAll() {
        MediaPlayManager.getInstance().stop();
        AudioUiUtils.getInstance().resetAllView();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.play) {
            if (mManager.isPlaying()) {
                if (!mPlay.isSelected()) {
                    mManager.pause();
                    AudioUiUtils.getInstance().resetOtherView(this);
                    prepared();
                } else {
                    mManager.pause();
                }
            } else {
                if (null != onReleaseOtherPlayer) {
                    onReleaseOtherPlayer.onRelease();
                }
                if (isPrepared) {
                    mManager.play();
                } else {
                    AudioUiUtils.getInstance().resetOtherView(this);
                    prepared();
                }
            }
        }else if(i == R.id.error){
            resetDefaultUi();
            mError.setVisibility(GONE);
            prepared();
        }
    }

    private void prepared() {
        mManager.setBufferingListener(this);
        mManager.setCompletionListener(this);
        mManager.setErrorListener(this);
        mManager.setPlayerProgressListener(this);
        mManager.setPlayerStateListener(this);
        isPrepared = true;
        mManager.setData(mUrl);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mManager) {
            mManager.stop();
        }
    }

    @Override
    public void onBufferStart(MediaPlayer mp) {
        mBuffer.setVisibility(VISIBLE);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mPercent.setText(percent + "%");
    }

    @Override
    public void onBufferCompletion(MediaPlayer mp) {
        mBuffer.setVisibility(GONE);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (null != onPlayCompletion) {
            onPlayCompletion.onPlayCompletion(mp);
        }
    }

    @Override
    public void onError(MediaPlayer mp, int what, int extra) {
        mBuffer.setVisibility(GONE);
        mError.setVisibility(VISIBLE);
    }

    @Override
    public void onDuration(int duration) {
        mDuration.setText(long2String(duration));
        mSeekBar.setMax(duration);
    }

    @Override
    public void onProgress(int progress) {
        mCurrent.setText(long2String(progress));
        mSeekBar.setProgress(progress);
    }

    @Override
    public void onMediaPlayerState(boolean play) {
        mPlay.setSelected(play);
    }

    @Override
    public void onPrepared() {
        mSeekBar.setEnabled(true);
        mPlay.setEnabled(true);
        mError.setVisibility(GONE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mSeekBarProgress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mEnableSeekTo) {
            mManager.seekTo(mSeekBarProgress);
            mCurrent.setText(long2String(mSeekBarProgress));
        }
    }

    public interface OnPlayCompletion {
        void onPlayCompletion(MediaPlayer mp);
    }

    public interface OnReleaseOtherPlayer {
        void onRelease();
    }

    private String long2String(long time) {
        if (time <= 0) {
            return "00:00";
        }
        int sec = (int) time / 1000;
        int min = sec / 60;
        sec = sec % 60;
        if (min < 10) {
            if (sec < 10) {
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }
    }
}
