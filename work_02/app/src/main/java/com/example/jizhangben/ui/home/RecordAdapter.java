package com.example.jizhangben.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jizhangben.R;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<Map<String, Object>> records;

    public RecordAdapter(List<Map<String, Object>> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> record = records.get(position);

        String type = (String) record.get("type");
        String category = (String) record.get("category");
        Double amount = (Double) record.get("amount");
        String date = (String) record.get("date");
        String remark = (String) record.get("remark");

        // 类目
        holder.tvCategory.setText(category != null ? category : "未分类");

        // 备注（没有则显示日期）
        if (remark != null && !remark.isEmpty()) {
            holder.tvRemark.setText(remark);
            holder.tvRemark.setVisibility(View.VISIBLE);
        } else {
            holder.tvRemark.setVisibility(View.GONE);
        }

        // 金额颜色：收入绿色，支出红色
        if ("income".equals(type)) {
            holder.tvAmount.setTextColor(Color.parseColor("#4CAF50"));
            holder.tvAmount.setText(String.format("+¥%.2f", amount));
            holder.ivIcon.setImageResource(android.R.drawable.ic_input_add);
        } else {
            holder.tvAmount.setTextColor(Color.parseColor("#E53935"));
            holder.tvAmount.setText(String.format("-¥%.2f", amount));
            holder.ivIcon.setImageResource(android.R.drawable.ic_delete);
        }
        holder.ivIcon.setColorFilter(holder.tvAmount.getCurrentTextColor());

        // 日期（只显示月-日）
        if (date != null && date.length() >= 10) {
            holder.tvDate.setText(date.substring(5)); // MM-dd
        } else {
            holder.tvDate.setText(date);
        }

        // 长按删除
        holder.itemView.setOnLongClickListener(v -> {
            if (onRecordLongClick != null) {
                onRecordLongClick.onLongClick(position,
                        ((Number) record.get("id")).longValue());
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvCategory, tvRemark, tvAmount, tvDate;

        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_category_icon);
            tvCategory = itemView.findViewById(R.id.tv_item_category);
            tvRemark = itemView.findViewById(R.id.tv_item_remark);
            tvAmount = itemView.findViewById(R.id.tv_item_amount);
            tvDate = itemView.findViewById(R.id.tv_item_date);
        }
    }

    // 长按回调接口
    private OnRecordLongClick onRecordLongClick;

    public interface OnRecordLongClick {
        void onLongClick(int position, Long recordId);
    }

    public void setOnRecordLongClick(OnRecordLongClick listener) {
        this.onRecordLongClick = listener;
    }
}
