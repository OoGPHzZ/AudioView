package com.gph.libr.utils;

import com.gph.libr.AudioView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guopenghui-961057759@qq.com
 * @date 2018/3/5 0005
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
     * 判断list中是否存在view，不存在就添加View到数组进行管理
     *
     * @param view
     */
    public void addViewToList(AudioView view) {
        if (!mList.contains(view)) {
            mList.add(view);
        }
    }

    /**
     * 重置view以外的AudioView，并将list中其他的view进行清空
     *
     * @param view
     */
    public void resetOtherView(AudioView view) {
        if (mList.contains(view)) {
            for (AudioView audioView : mList) {
                if (audioView != view) {
                    audioView.resetDefaultUi();
                }
            }
            mList.clear();
            mList.add(view);
        } else {
            resetAllView();
            mList.add(view);
        }
    }

    /**
     * 重置当前的AudioView，并从list当中移除,并暂停播放
     *
     * @param view
     */
    public void resetView(AudioView view) {
        if (mList.contains(view)) {
            MediaPlayManager.getInstance().pause();
            view.resetDefaultUi();
            mList.remove(view);
        }
    }

    /**
     * 重置所有view，并清空list
     */
    public void resetAllView() {
        for (AudioView audioView : mList) {
            audioView.resetDefaultUi();
        }
        mList.clear();
    }

    /**
     * 重置所有view、清空list并释放mediaplayer
     */
    public void releaseAllViewPlayer() {
        MediaPlayManager.getInstance().stop();
        for (AudioView audioView : mList) {
            audioView.resetDefaultUi();
        }
        mList.clear();
    }
}
