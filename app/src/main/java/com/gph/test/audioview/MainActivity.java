package com.gph.test.audioview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gph
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private List<AudioInfoBean> mList;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        mList = initData();
        adapter = new TestAdapter(this, mList);
        recycler.setAdapter(adapter);
    }

    private List<AudioInfoBean> initData() {
        List<AudioInfoBean> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(new AudioInfoBean("http://up.mcyt.net/down/36453.mp3", "04:26"));
            list.add(new AudioInfoBean("http://sc1.111ttt.cn/2016/1/11/27/204271343347.mp3", "05:10"));
            list.add(new AudioInfoBean("http://sc1.111ttt.cn/2016/5/12/10/205100046334.mp3", "03:15"));
        }

        return list;
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        mList.add(0, new AudioInfoBean("http://sc1.111ttt.cn/2016/1/12/04/205041601432.mp3", "04:45"));
        adapter.notifyDataSetChanged();
    }
}
