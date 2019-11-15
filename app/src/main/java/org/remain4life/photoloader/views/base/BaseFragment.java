package org.remain4life.photoloader.views.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.remain4life.photoloader.R;
import org.remain4life.photoloader.viewmodels.base.BaseViewModel;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {
    public VM viewModel;
    protected B binding;

    // abstract methods for binding
    public abstract VM onCreateViewModel(@Nullable Bundle savedInstanceState); // creates ViewModel
    public abstract @IdRes
    int getVariable(); // returns variable from data->variable tag of xml
    public abstract @LayoutRes
    int getLayoutId(); // returns xml layout id

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bind(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.executePendingBindings();
    }

    /**
     * Universal base method to bind activity to remove this code from heirs
     */
    public void bind(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        this.viewModel = viewModel == null ? onCreateViewModel(savedInstanceState) : viewModel;
        binding.setVariable(getVariable(), viewModel);
        binding.executePendingBindings();
    }

    public void setViewModel(VM viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (viewModel != null) {
            viewModel.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (viewModel != null) {
            viewModel.onResume();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (viewModel != null) {
            viewModel.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (viewModel != null) {
            viewModel.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (viewModel != null) {
            viewModel.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewModel != null) {
            viewModel.onDestroyView();
        }
    }

    public void onError(String error) {
        viewModel.showDialog(R.string.error, error);
    }

    public void onError(Throwable throwable) {
        viewModel.onError(throwable);
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

    public void update(){
        binding.executePendingBindings();
    }
}
