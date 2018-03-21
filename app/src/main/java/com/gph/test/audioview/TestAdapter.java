package com.gph.test.audioview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gph.libr.AudioView;

import java.util.List;

/**
 * Created by guopenghui-961057759@qq.com on 2018/3/20 0020.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private Context mContext;
    private List<AudioInfoBean> mList;

    public TestAdapter(Context context, List<AudioInfoBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_layout, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        AudioInfoBean bean = mList.get(position);
        holder.audioView.init(bean.getUrl(), bean.getTotalTime());
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        AudioView audioView;

        public TestViewHolder(View itemView) {
            super(itemView);
            audioView = itemView.findViewById(R.id.audio_view);
        }
    }
}
