package org.remain4life.mvvm.views;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import org.remain4life.mvvm.R;
import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.databinding.ActivityMainBinding;
import org.remain4life.mvvm.viewmodels.MainViewModel;
import org.remain4life.mvvm.views.base.BaseActivity;
import org.remain4life.mvvm.views.base.IMainNavigator;

import java.util.Objects;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel>
        implements IMainNavigator {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    public MainViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new MainViewModel(this, this);
    }

    @Override
    public int getVariable() {
        return BR.mainViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setContentFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void setTitle(String title) {
        Objects.requireNonNull(
                getSupportActionBar()
        ).setTitle(getString(R.string.app_name) + " " + title);
    }
}
