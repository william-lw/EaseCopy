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
import com.murui.easecopy.bean.ClipboardDBModel;
import com.murui.easecopy.databinding.MainCopyItemBinding;
import com.murui.easecopy.view.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ClipContentAdapter extends RecyclerView.Adapter<ClipContentAdapter.ClipHolder> {

    private final String TAG = "ClipContentAdapter";
    private List<ClipboardBean> dataList;
    private OnClipBoardItemClickListener itemListener;
    private Context mContext;
    private HashSet<ClipboardBean> selectSet = new HashSet<ClipboardBean>();

    public void setDateList(List<ClipboardBean> strList) {
        this.dataList = strList;
        notifyDataSetChanged();
    }

    public void addDateToLast(ClipboardBean bean){
        if (dataList == null){
            dataList = new ArrayList<>();
        }
        int size = dataList.size();
        dataList.add(size, bean);
        notifyItemInserted(dataList.size() -1);
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
        holder.binding.executePendingBindings();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onClipItemClick(clipboardBean);
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

        holder.mainDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipeLayout.smoothToCloseMenu();
                if (itemListener != null) {
                    itemListener.onDeleteButtonClick(clipboardBean);
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
