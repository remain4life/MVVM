package org.remain4life.mvvm.views.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.remain4life.mvvm.R;
import org.remain4life.mvvm.viewmodels.base.BaseViewModel;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {
    public VM viewModel;
    protected B binding;

    // abstract methods for binding
    public abstract VM onCreateViewModel(@Nullable Bundle savedInstanceState); // creates ViewModel
    public abstract @IdRes
    int getVariable(); // returns variable from data->variable tag of xml
    public abstract @LayoutRes
    int getLayoutId(); // returns xml layout id

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(savedInstanceState);
    }

    /**
     * Universal base method to bind activity to remove this code from heirs
     */
    public void bind(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? onCreateViewModel(savedInstanceState) : viewModel;
        binding.setVariable(getVariable(), viewModel);
        binding.executePendingBindings();
    }

    public B getBinding() {
        return binding;
    }

    public void setViewModel(VM viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (viewModel != null) {
            viewModel.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (viewModel != null) {
            viewModel.onResume();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (viewModel != null) {
            viewModel.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (viewModel != null) {
            viewModel.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (viewModel != null) {
            viewModel.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) {
            viewModel.onDestroy();
        }
    }

    public void onError(String error) {
        viewModel.showDialog(R.string.error, error);
    }

    public void onError(Throwable throwable) {
        viewModel.onError(throwable);
    }

    public void finishWithResult(int result, Intent intent) {
        setResult(result, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewModel != null) {
            viewModel.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (viewModel != null) {
            viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

