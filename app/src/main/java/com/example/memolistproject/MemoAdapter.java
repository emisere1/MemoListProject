package com.example.memolistproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private Context parentContext;
    private ArrayList<Memos> memoData;
    private View.OnClickListener mOnItemClickListener;

    public MemoAdapter(ArrayList<Memos> arrayList, Context context) {
        memoData = arrayList;
        parentContext = context;
    }

    public static class MemoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSubject;
        public TextView textViewDate;
        public TextView textViewDescription;
        public RadioButton radioButtonHigh;
        public RadioButton radioButtonMedium;
        public RadioButton radioButtonLow;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textSubject);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewDescription = itemView.findViewById(R.id.textDescription);
            radioButtonHigh = itemView.findViewById(R.id.radioHigh);
            radioButtonMedium = itemView.findViewById(R.id.radioMedium);
            radioButtonLow = itemView.findViewById(R.id.radioLow);
        }
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item, parent, false);
        return new MemoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        Memos memo = memoData.get(position);
        holder.textViewSubject.setText(memo.getMemoSubject());
        holder.textViewDescription.setText(memo.getMemoDescription());

        Calendar memoDate = memo.getMemoDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = sdf.format(memoDate.getTime());
        holder.textViewDate.setText(dateString);

        int priority = memo.getMemoPriority();

        holder.itemView.setBackgroundColor(getPriorityColor(priority));
        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(mOnItemClickListener);
    }

    private void updatePriority(MemoViewHolder holder, int priority) {
        int position = holder.getAdapterPosition();
        Memos memo = memoData.get(position);
        memo.setMemoPriority(priority);
        notifyItemChanged(position);
    }

    private int getPriorityColor(int priority) {
        switch (priority) {
            case Memos.PRIORITY_HIGH:
                return Color.RED;
            case Memos.PRIORITY_MEDIUM:
                return Color.YELLOW;
            case Memos.PRIORITY_LOW:
                return Color.GREEN;
            default:
                return Color.WHITE;
        }
    }

    public void removeItem(int position) {
        Memos memo = memoData.get(position);
        MemoQueryActivity db = new MemoQueryActivity(parentContext);
        try {
            db.open();
            boolean didDelete = db.deleteMemo(memo.getMemoID());
            db.close();
            if (didDelete){
                memoData.remove(position);
                notifyItemRemoved(position);
            }
            else{
                Toast.makeText(parentContext, "Delete failed", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete failed", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return memoData.size();
    }
}


