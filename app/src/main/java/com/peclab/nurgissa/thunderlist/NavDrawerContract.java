package com.peclab.nurgissa.thunderlist;


public interface NavDrawerContract {

    interface View {
        void createNewItemCategory(int position);
        void notifyItemCategoryAdded(int position);
    }

    interface AdapterView {
        void setItemNav(String title, int imageView, int imageColor, int itemCount);
    }
}
