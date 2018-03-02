package com.peclab.nurgissa.thunderlist;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TaskDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private TaskDetailPresenter presenter;
    private TaskDetailFragment fragment;

    public TaskDetailRecyclerViewAdapter(TaskDetailFragment fragment, TaskDetailPresenter presenter) {
        this.presenter = presenter;
        this.fragment = fragment;
    }

    public interface Listener {
        void onNoteItemClick(View view);
    }

    public class BasicViewHolder extends RecyclerView.ViewHolder implements TaskDetailContract.BasicAdapterView, View.OnClickListener {
        private ImageView image;
        private TextView text;

        public BasicViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.basic_task_imageView);
            text = itemView.findViewById(R.id.basic_task_textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void feelView(int image, String text) {
            this.image.setImageResource(image);
            this.text.setText(text);
        }

        @Override
        public void onClick(View v) {
            switch (getAdapterPosition()) {
                case 2:
                    fragment.onNoteItemClick(v);
                    break;
            }
        }
    }

    public class SubtaskViewHolder extends RecyclerView.ViewHolder implements TaskDetailContract.SubtaskAdapterView {
        private CheckBox checkBox;
        private TextView text;

        public SubtaskViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.sub_task_checkBox);
            text = itemView.findViewById(R.id.sub_task_textView);
        }

        @Override
        public void feelView(String value) {
            text.setText(value);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder viewHolder;

        if (viewType <= 3) {
            view = inflater.inflate(R.layout.basic_task_detail, parent, false);
            viewHolder = new BasicViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.sub_task_detail, parent, false);
            viewHolder = new SubtaskViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() <= 3) {
            presenter.bindBasicViewHolderToData((BasicViewHolder) holder, position);
        } else {
            presenter.bindSubtaskViewHolderToData((SubtaskViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getDetailTaskCount();
    }
}
