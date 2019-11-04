package org.remain4life.photoloader.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.remain4life.photoloader.BR;
import org.remain4life.photoloader.BuildConfig;
import org.remain4life.photoloader.R;
import org.remain4life.photoloader.databinding.ActivityMainBinding;
import org.remain4life.photoloader.helpers.CustomNumberPicker;
import org.remain4life.photoloader.model.PhotoRepository;
import org.remain4life.photoloader.viewmodels.MainViewModel;
import org.remain4life.photoloader.views.base.BaseActivity;
import org.remain4life.photoloader.views.base.IMainNavigator;

import java.util.Objects;

import static org.remain4life.photoloader.helpers.Constants.APP_TAG;
import static org.remain4life.photoloader.helpers.Constants.MAX_PHOTOS;
import static org.remain4life.photoloader.helpers.Constants.MIN_PHOTOS;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.photos_number:
                showNumberPicker();
                return true;
            case R.id.exit:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showNumberPicker() {
        // value to cache changes
        final int[] numberChosen = {0};
        // apply needed font size
        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.NumberPickerText);
        final CustomNumberPicker numberPicker = new CustomNumberPicker(cw);

        numberPicker.setMinValue(MIN_PHOTOS);
        numberPicker.setMaxValue(MAX_PHOTOS);
        if (BuildConfig.DEBUG) {
            Log.d(APP_TAG, "Selected photos number: " + PhotoRepository.getInstance().getPhotosToLoad());
        }

        // preset chosen value
        numberPicker.setValue(
                PhotoRepository.getInstance().getPhotosToLoad()
        );

        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener((numberPicker1, i, i1) -> {

            numberChosen[0] = i1;
        });

        String title = getString(R.string.dialog_choose_photos_to_load);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_dialog_photos_number)
                .setTitle(title)
                .setView(numberPicker)
                .setPositiveButton(android.R.string.ok, (dialog1, which) -> {
                    // update repository, which notifies needed photos screen
                    PhotoRepository.getInstance().setPhotosToLoad(numberChosen[0]);
                    if (BuildConfig.DEBUG) {
                        Log.d(APP_TAG, "Selected photos number: " + numberChosen[0]);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setCancelable(false);
        dialog.show();
    }
}
