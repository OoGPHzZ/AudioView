package com.gph.libr.utils;

import com.gph.libr.AudioView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guopenghui-961057759@qq.com on 2018/3/5 0005.
 */

public class AudioUiUtils {
    private static AudioUiUtils instance;
    private List<AudioView> mList;

    private AudioUiUtils() {
        mList = new ArrayList<>();
    }

    public static AudioUiUtils getInstance() {
        if (null == instance) {
            instance = new AudioUiUtils();
        }
        return instance;
    }

    /**
     * 添加View到数组进行管理
     *
     * @param view
     */
    public void addViewToList(AudioView view) {
        mList.add(view);
    }

    /**
     * 重置view以外的UI
     *
     * @param view
     */
    public void resetOtherView(AudioView view) {
        for (AudioView audioView : mList) {
            if (audioView != view) {
                audioView.resetDefaultUi();
            }
        }
    }

    /**
     * 重置所有view
     */
    public void resetAllView() {
        for (AudioView audioView : mList) {
            audioView.resetDefaultUi();
        }
    }
}
