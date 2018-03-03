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

    public class BasicValueViewHolder extends RecyclerView.ViewHolder implements TaskDetailContract.BasicAdapterValueView {
        private ImageView image;
        private EditText editText;

        public BasicValueViewHolder(View itemView) {
            super(itemView);

            this.image = itemView.findViewById(R.id.basic_show_task_value_imageView);
            this.editText = itemView.findViewById(R.id.basic_show_task_value_editText);

            setColor();
        }

        private void setColor() {
            image.setColorFilter(ContextCompat.getColor(fragment.getContext(), R.color.darkGray));
            editText.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.darkGray));
        }

        @Override
        public void feelView(int image, String value) {
            this.image.setImageResource(image);
            this.editText.setText(value);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getViewType(position);
    }

    //TODO Move business login from view.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        View view;

        if (viewType == DetailTaskItem.VALUE) {
            view = inflater.inflate(R.layout.basic_value_item, parent, false);
            viewHolder = new BasicValueViewHolder(view);

        } else if (viewType == DetailTaskItem.ADD_SUBTASK || viewType == DetailTaskItem.NOTE || viewType == DetailTaskItem.SCHEDULE) {
            view = inflater.inflate(R.layout.basic_detail_item, parent, false);
            viewHolder = new BasicViewHolder(view);

        } else if (viewType == DetailTaskItem.SUBTASK) {
            view = inflater.inflate(R.layout.subtask_detail_item, parent, false);
            viewHolder = new SubtaskViewHolder(view);
        }
        return viewHolder;
    }

    //TODO Move business login from view.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();

        if (viewType == DetailTaskItem.VALUE) {
            presenter.bindBasicValueToValue((BasicValueViewHolder) holder, position);

        } else if (viewType == DetailTaskItem.ADD_SUBTASK || viewType == DetailTaskItem.NOTE || viewType == DetailTaskItem.SCHEDULE) {
            presenter.bindBasicViewHolderToData((BasicViewHolder) holder, position);

        } else if (viewType == DetailTaskItem.SUBTASK) {
            presenter.bindSubtaskViewHolderToData((SubtaskViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getDetailTaskItemCount();
    }
}
