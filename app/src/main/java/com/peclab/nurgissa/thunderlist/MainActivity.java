package com.peclab.nurgissa.thunderlist;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements TaskListFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskListFragment fragment = new TaskListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onItemClick(String value) {
        TaskDetailFragment detailFragment = new TaskDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TaskDetailFragment.EXTRA_VALUE, value);

        detailFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, detailFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
