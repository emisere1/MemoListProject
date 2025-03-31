package com.example.memolistproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class MemoAdapter extends RecyclerView.Adapter {

    private Context parentContext;
    private ArrayList<Memos> memoData;
    private View.OnClickListener mOnItemClickListener;

    public MemosAdapter(ArrayList<Memos> arrayList, Context Context) {
        memoData = arrayList;
        parentContext = Context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
