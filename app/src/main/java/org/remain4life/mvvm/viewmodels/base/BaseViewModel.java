package org.remain4life.mvvm.viewmodels.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.remain4life.mvvm.R;
import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.helpers.ResponseException;
import org.remain4life.mvvm.helpers.StatusResponse;
import org.remain4life.mvvm.views.base.INavigator;

import io.reactivex.disposables.Disposable;

import static org.remain4life.mvvm.helpers.Constants.ERROR_TAG;

public abstract class BaseViewModel<N extends INavigator> extends BaseObservable {
    protected Context context;
    protected N navigator;
    private Disposable disposable;
    private Dialog dialog;
    private boolean isLoading;

    public BaseViewModel(@NonNull Context context, N navigator) {
        this.context = context;
        this.navigator = navigator;
    }

    @Bindable
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    public void onError(String error) {
        showDialog(R.string.error, error);
    }

    public void onError(Throwable throwable) {

        String message;
        if (throwable instanceof ResponseException) {
            final StatusResponse statusResponse = ((ResponseException)throwable).statusResponse;
            message = "status code " + statusResponse.status + ", " + statusResponse.errorMessage;
        } else {
            message = throwable.getMessage();
        }

        Log.e(ERROR_TAG, "Error: " + message);
        onError(message);
    }

    public void setDialog(Dialog dialog) {
        dismissDialog();
        this.dialog = dialog;
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void showDialog(int title,String message) {
        setDialog(new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show());
    }

    public void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    public void setDisposable(Disposable disposable) {
        dispose();
        this.disposable = disposable;
    }

    public void onResume() {
    }

    public void onStart() {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroy() {
        context = null;
        dispose();
        dismissDialog();
    }

    public void onDestroyView() {
    }
}
