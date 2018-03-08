package com.peclab.nurgissa.thunderlist;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class TaskDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private TaskDetailPresenter presenter;
    private TaskDetailFragment fragment;

    public TaskDetailRecyclerViewAdapter(TaskDetailFragment fragment, TaskDetailPresenter presenter) {
        this.presenter = presenter;
        this.fragment = fragment;
    }

    public interface Listener {
        void onNoteItemClick(View view);

        void onAddSubtaskItemClick(View view);

        void onScheduleItemClick(View view);
    }

    public class BasicViewHolder extends RecyclerView.ViewHolder implements TaskDetailContract.BasicAdapterView, View.OnClickListener {
        private ImageView imageView;
        private TextView textView;

        public BasicViewHolder(View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.basic_detail_item_imageView);
            this.textView = itemView.findViewById(R.id.basic_detail_item_textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void feelView(int image, String text) {
            this.imageView.setImageResource(image);
            this.textView.setText(text);
        }

        @Override
        public void onClick(View v) {
            int viewType = presenter.getViewType(getAdapterPosition());

            if (viewType == DetailTaskItem.NOTE) {
                fragment.onNoteItemClick(v);

            } else if (viewType == DetailTaskItem.ADD_SUBTASK) {
                fragment.onAddSubtaskItemClick(v);

            } else if (viewType == DetailTaskItem.SCHEDULE) {
                fragment.onScheduleItemClick(v);

            }
        }
    }

    public class SubtaskViewHolder extends RecyclerView.ViewHolder implements TaskDetailContract.SubtaskAdapterView {
        private CheckBox checkBox;
        private TextView textView;

        public SubtaskViewHolder(View itemView) {
            super(itemView);

            this.checkBox = itemView.findViewById(R.id.subtask_detail_item_checkBox);
            this.textView = itemView.findViewById(R.id.subtask_detail_item_textView);
        }

        @Override
        public void feelView(String value) {
            textView.setText(value);
        }
    }

    public class TaskTitleViewHolder extends RecyclerView.ViewHolder implements TaskDetailContract.TaskTitleAdapterView {
        private ImageView imageView;
        private EditText editText;

        public TaskTitleViewHolder(View itemView) {
            super(itemView);

            this.imageView = itemView.findViewById(R.id.task_title_item_imageView);
            this.editText = itemView.findViewById(R.id.task_title_item_editText);
        }

        @Override
        public void feelView(int image, String value) {
            imageView.setImageResource(image);
            editText.setText(value);

            imageView.setColorFilter(ContextCompat.getColor(fragment.getContext(), R.color.darkGray));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getViewType(position);
    }

    //TODO move business logic from view.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        View view;

        if (viewType == DetailTaskItem.VALUE) {
            view = inflater.inflate(R.layout.task_detail_title_item, parent, false);
            viewHolder = new TaskTitleViewHolder(view);

        } else if (viewType == DetailTaskItem.ADD_SUBTASK || viewType == DetailTaskItem.NOTE || viewType == DetailTaskItem.SCHEDULE) {
            view = inflater.inflate(R.layout.basic_detail_item, parent, false);
            viewHolder = new BasicViewHolder(view);

        } else if (viewType == DetailTaskItem.SUBTASK) {
            view = inflater.inflate(R.layout.subtask_detail_item, parent, false);
            viewHolder = new SubtaskViewHolder(view);
        }

        return viewHolder;
    }

    //TODO move business logic from view.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if (viewType == DetailTaskItem.VALUE) {
            presenter.bindTaskTitleViewHolderToValue((TaskTitleViewHolder) holder, position);

        } else if (viewType == DetailTaskItem.ADD_SUBTASK || viewType == DetailTaskItem.NOTE || viewType == DetailTaskItem.SCHEDULE) {
            presenter.bindBasicViewHolderToValue((BasicViewHolder) holder, position);

        } else if (viewType == DetailTaskItem.SUBTASK) {
            presenter.bindSubtaskViewHolderToValue((SubtaskViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getDetailTaskItemCount();
    }
}
