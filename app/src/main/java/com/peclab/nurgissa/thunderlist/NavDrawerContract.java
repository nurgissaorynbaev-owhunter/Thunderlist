package com.peclab.nurgissa.thunderlist;


public interface NavDrawerContract {

    interface View {

    }

    interface AdapterView {
        void setItemNav(String title, int imageView, int imageColor, int itemCount);
    }
}
