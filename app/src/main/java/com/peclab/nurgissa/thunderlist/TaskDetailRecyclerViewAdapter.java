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
    private TaskDetailContract.Presenter presenter;
    private TaskDetailFragment fragment;

    public TaskDetailRecyclerViewAdapter(TaskDetailFragment fragment, TaskDetailContract.Presenter presenter) {
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
        public void setImage(int image) {
            imageView.setImageResource(image);
        }

        @Override
        public void setText(String text) {
            textView.setText(text);
        }

        @Override
        public void onClick(View v) {
            int viewType = presenter.getViewType(getAdapterPosition());

            if (viewType == TaskDetail.VIEW_TYPE_NOTE) {
                fragment.onNoteItemClick(v);

            } else if (viewType == TaskDetail.VIEW_TYPE_ADD_SUBTASK) {
                fragment.onAddSubtaskItemClick(v);

            } else if (viewType == TaskDetail.VIEW_TYPE_SCHEDULE) {
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
        public void setText(String text) {
            textView.setText(text);
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
        public void setImage(int image) {
            imageView.setImageResource(image);
        }

        @Override
        public void setText(String text) {
            editText.setText(text);
        }

        @Override
        public void setTextHint(String text) {
            editText.setHint(text);
            editText.setHintTextColor(ContextCompat.getColor(fragment.getContext(), R.color.light_gray));
            imageView.setColorFilter(ContextCompat.getColor(fragment.getContext(), R.color.light_gray));
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

        if (viewType == TaskDetail.VIEW_TYPE_EDIT_VALUE) {
            view = inflater.inflate(R.layout.item_detail_task_title, parent, false);
            viewHolder = new TaskTitleViewHolder(view);

        } else if (viewType == TaskDetail.VIEW_TYPE_ADD_SUBTASK || viewType == TaskDetail.VIEW_TYPE_NOTE || viewType == TaskDetail.VIEW_TYPE_SCHEDULE) {
            view = inflater.inflate(R.layout.item_detail_basic, parent, false);
            viewHolder = new BasicViewHolder(view);

        } else if (viewType == TaskDetail.VIEW_TYPE_SUBTASK) {
            view = inflater.inflate(R.layout.item_detail_subtask, parent, false);
            viewHolder = new SubtaskViewHolder(view);
        }

        return viewHolder;
    }

    //TODO move business logic from view.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if (viewType == TaskDetail.VIEW_TYPE_EDIT_VALUE) {
            presenter.bindTaskTitleViewToValue((TaskTitleViewHolder) holder, position);

        } else if (viewType == TaskDetail.VIEW_TYPE_ADD_SUBTASK || viewType == TaskDetail.VIEW_TYPE_NOTE || viewType == TaskDetail.VIEW_TYPE_SCHEDULE) {
            presenter.bindBasicViewToValue((BasicViewHolder) holder, position);

        } else if (viewType == TaskDetail.VIEW_TYPE_SUBTASK) {
            presenter.bindSubtaskViewToValue((SubtaskViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getDetailTaskItemCount();
    }
}
