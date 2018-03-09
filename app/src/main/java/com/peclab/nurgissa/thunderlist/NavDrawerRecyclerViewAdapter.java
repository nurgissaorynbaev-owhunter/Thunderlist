package com.peclab.nurgissa.thunderlist;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class NavDrawerRecyclerViewAdapter extends RecyclerView.Adapter<NavDrawerRecyclerViewAdapter.ViewHolder> {
    private MainActivity activity;
    private NavDrawerPresenter presenter;

    public NavDrawerRecyclerViewAdapter(MainActivity activity, NavDrawerPresenter presenter) {
        this.activity = activity;
        this.presenter = presenter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements NavDrawerContract.AdapterView{
        private ImageView imageView;
        private TextView textViewTitle;
        private TextView textViewCount;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_nav_image);
            textViewTitle = itemView.findViewById(R.id.item_nav_title_text);
            textViewCount = itemView.findViewById(R.id.item_nav_count);
        }

        @Override
        public void setItemNav(String title, int imageView, int imageColor, int itemCount) {
            this.textViewTitle.setText(title);
            this.imageView.setImageResource(imageView);
            this.imageView.setColorFilter(ContextCompat.getColor(activity, imageColor));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        presenter.bindAdapterViewToData(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getCategoryItemCount();
    }
}
