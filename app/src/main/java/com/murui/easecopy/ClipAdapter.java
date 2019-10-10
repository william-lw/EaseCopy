package com.murui.easecopy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.murui.easecopy.bean.ClipboardBean;
import com.murui.easecopy.databinding.ClipContentItemBinding;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ClipAdapter extends RecyclerView.Adapter<ClipAdapter.ClipHolder> {

    private List<ClipboardBean> dataList;
    private OnFloatWindowClickListener itemListener;
    private HashSet<ClipboardBean> selectSet = new HashSet<ClipboardBean>();

    public void setDateList(List<ClipboardBean> strList) {
        this.dataList = strList;
        notifyDataSetChanged();
    }

    public void setOnClipTextItemClickListener(OnFloatWindowClickListener listener) {
        this.itemListener = listener;
    }

    @NonNull
    @Override
    public ClipHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClipContentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.clip_content_item, parent, false);
        ClipHolder clipHolder = new ClipHolder(binding);
        return clipHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClipHolder holder, final int position) {
        final ClipboardBean clipboardBean = dataList.get(position);
        holder.binding.setVariable(BR.bean, dataList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onClipItemClick(dataList.get(position));
                }
                if (!selectSet.isEmpty()) {
                    for (int i = 0; i < selectSet.size(); i++) {
                        Iterator<ClipboardBean> iterator = selectSet.iterator();
                        while (iterator.hasNext()) {
                            ClipboardBean next = iterator.next();
                            next.setItemState(false);
                            iterator.remove();
                        }
                    }
                }
                clipboardBean.setItemState(true);
                selectSet.add(clipboardBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class ClipHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ClipHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
