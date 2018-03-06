package com.gph.test.audioview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gph.libr.AudioView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.audio_view)
    AudioView audioView;
    @BindView(R.id.audio_view2)
    AudioView audioView2;
    @BindView(R.id.audio_view3)
    AudioView audioView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        audioView.init("http://win.web.ri01.sycdn.kuwo.cn/dd7676ca986a5051f37216ebcb638182/5a9d2b1f/resource/n1/37/85/2338221246.mp3", "04:40");
        audioView2.init("http://sc1.111ttt.cn/2016/1/11/27/204271343347.mp3", "04:40");
        audioView3.init("http://sc1.111ttt.cn/2016/5/12/10/205100046334.mp3", "04:40");
        // VideoPlayerManager.getInstance().releaseVideoPlayer();
    }
}
