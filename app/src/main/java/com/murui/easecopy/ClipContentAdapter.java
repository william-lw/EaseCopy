package com.murui.easecopy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.murui.easecopy.bean.ClipboardBean;
import com.murui.easecopy.databinding.MainCopyItemBinding;
import com.murui.easecopy.view.SwipeMenuLayout;

import java.util.List;

public class ClipContentAdapter extends RecyclerView.Adapter<ClipContentAdapter.ClipHolder> {

    private final String TAG = "ClipContentAdapter";
    private List<ClipboardBean> dataList;
    private OnClipBoardItemClickListener itemListener;
    private Context mContext;

    public void setDateList(List<ClipboardBean> strList) {
        this.dataList = strList;
        notifyDataSetChanged();
    }

    public void setOnClipBoardItemClickListener(OnClipBoardItemClickListener listener) {
        this.itemListener = listener;
    }

    @NonNull
    @Override
    public ClipHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        MainCopyItemBinding dateBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.main_copy_item, parent, false);
        ClipHolder clipHolder = new ClipHolder(dateBinding);
        return clipHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ClipHolder holder, final int position) {
        final ClipboardBean clipboardBean = dataList.get(position);
        holder.binding.setVariable(BR.clipBean, clipboardBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onClipItemClick(clipboardBean.getClipContent());
                }
            }
        });
        holder.mainDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipeLayout.smoothToCloseMenu();
                if (itemListener != null) {
                    itemListener.onDeleteButtonClick();
                }
                dataList.remove(clipboardBean);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size() - position);
            }
        });
        holder.mainEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //手动关闭打开的菜单
                holder.swipeLayout.smoothToCloseMenu();
                if (itemListener != null) {
                    itemListener.onEditButtonClick(clipboardBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class ClipHolder extends RecyclerView.ViewHolder {

        public ViewDataBinding binding;
        //        public TextView clipTag, des, content;
        public Button mainDelete, mainEdit;
        public SwipeMenuLayout swipeLayout;

        public ClipHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            clipTag = itemView.findViewById(R.id.clip_tag);
//            des = itemView.findViewById(R.id.clip_description);
//            content = itemView.findViewById(R.id.clipboard_content);
            mainDelete = binding.getRoot().findViewById(R.id.main_delete);
            mainEdit = binding.getRoot().findViewById(R.id.main_edit);
            swipeLayout = binding.getRoot().findViewById(R.id.swipe_layout);
        }
    }
}
