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

import java.util.List;

public class ClipAdapter extends RecyclerView.Adapter<ClipAdapter.ClipHolder> {

    private List<ClipboardBean> dataList;
    private OnFloatWindowClickListener itemListener;

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
        holder.binding.setVariable(BR.bean, dataList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onClipItemClick(dataList.get(position));
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
