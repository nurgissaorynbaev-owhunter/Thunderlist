package com.peclab.nurgissa.thunderlist;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder> {
    private TasksListPresenter presenter;

    public TasksRecyclerViewAdapter(TasksListPresenter presenter) {
        this.presenter = presenter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements TasksListContract.AdapterView, View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        private TextView tvTitle;
        private CheckBox chbStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.text_view_title);
            this.chbStatus = itemView.findViewById(R.id.checkbox_status);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            chbStatus.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            presenter.checkStatusChanged(isChecked, getAdapterPosition());
        }

        @Override
        public void setTitle(String value) {
            tvTitle.setText(value);
        }

        @Override
        public void notifyDataChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void setChecked(boolean value) {
            chbStatus.setChecked(value);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_view_task, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        presenter.bindAdapterViewToValue(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

}
