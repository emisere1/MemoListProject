
package com.example.memolistproject;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class MemoAdapter extends RecyclerView.Adapter {

    private Context parentContext;
    private ArrayList<Memos> memoData;
    private View.OnClickListener mOnItemClickListener;

    public MemoAdapter(ArrayList<Memos> arrayList, Context Context) {
        memoData = arrayList;
        parentContext = Context;
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder {
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
            setupPriorityButtons();

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getSubjectTextView() {
            return textViewSubject;
        }

        public TextView getDateTextView() {
            return textViewDate;
        }

        public TextView getDescriptionTextView() {
            return textViewDescription;
        }


        private void setupPriorityButtons() {
            radioButtonHigh.setOnClickListener(v -> selectPriority(Memos.PRIORITY_HIGH));
            radioButtonMedium.setOnClickListener(v -> selectPriority(Memos.PRIORITY_MEDIUM));
            radioButtonLow.setOnClickListener(v -> selectPriority(Memos.PRIORITY_LOW));
        }
        private void selectPriority(int priority) {
            radioButtonHigh.setChecked(false);
            radioButtonMedium.setChecked(false);
            radioButtonLow.setChecked(false);

            if (priority == Memos.PRIORITY_HIGH) {
                radioButtonHigh.setChecked(true);
            } else if (priority == Memos.PRIORITY_MEDIUM) {
                radioButtonMedium.setChecked(true);
            } else if (priority == Memos.PRIORITY_LOW) {
                radioButtonLow.setChecked(true);
            }
        }

    }



    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_item, parent, false);
        return new MemoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MemoViewHolder mvh = (MemoViewHolder) holder;
        mvh.getSubjectTextView().setText(memoData.get(position).getMemoSubject());
        mvh.getDescriptionTextView().setText(memoData.get(position).getMemoDescription());
        mvh.getDateTextView().setText(memoData.get(position).getMemoDate());
        int pri = memoData.get(position).getMemoPriority();
        if (pri == 1) {
            mvh.itemView.setBackgroundColor(Color.RED);
        } else if (pri == 2) {
            mvh.itemView.setBackgroundColor(Color.YELLOW);
        } else {
            mvh.itemView.setBackgroundColor(Color.GREEN);
        }
        mvh.itemView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
            //        deleteItem(position);
                }
                return false;
            }
        });

    }

//    public void deleteItem(int position) {
//        Memos memo = memoData.get(position);
//        MemoDataSource ds = new MemoDataSource(parentContext);
//        try {
//            ds.open();
//            boolean didDelete = ds.deleteMemo(memo.getMemoID());
//            ds.close();
//            if (didDelete) {
//                memoData.remove(position);
//                notifyDataSetChanged();
//            } else {
//                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public int getItemCount() {
        return memoData.size();
    }


}



