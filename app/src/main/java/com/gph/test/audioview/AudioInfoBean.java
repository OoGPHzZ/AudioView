package com.gph.test.audioview;

/**
 * Created by guopenghui-961057759@qq.com on 2018/3/20 0020.
 */

public class AudioInfoBean {
    private String url;
    private String totalTime;

    public AudioInfoBean(String url, String totalTime) {
        this.url = url;
        this.totalTime = totalTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}
