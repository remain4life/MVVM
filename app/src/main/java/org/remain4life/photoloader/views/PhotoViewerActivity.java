package org.remain4life.photoloader.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.remain4life.photoloader.BR;
import org.remain4life.photoloader.R;
import org.remain4life.photoloader.databinding.ActivityPhotoViewerBinding;
import org.remain4life.photoloader.model.PhotoItem;
import org.remain4life.photoloader.viewmodels.PhotoViewerViewModel;
import org.remain4life.photoloader.views.base.BaseActivity;
import org.remain4life.photoloader.views.base.IPhotoViewerNavigator;

import java.util.Objects;

public class PhotoViewerActivity extends BaseActivity<ActivityPhotoViewerBinding, PhotoViewerViewModel>
        implements IPhotoViewerNavigator {

    public static final String EXTRA_PHOTO_ITEM = "extra_photo_item";

    public static final int REQUEST_PERMISSION_WRITE = 1;

    /**
     * Creates intent to start this activity with parcelable PhotoItem
     *
     * @param context   activity to start from
     * @param photoItem PhotoItem for adding ti Intent
     * @return Intent with PhotoItem
     */
    public static Intent createIntent(Context context, PhotoItem photoItem) {
        Intent intent = new Intent(context, PhotoViewerActivity.class);
        intent.putExtra(EXTRA_PHOTO_ITEM, photoItem);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_photo_viewer));
    }

    @Override
    public PhotoViewerViewModel onCreateViewModel(@Nullable Bundle savedInstanceState) {
        return new PhotoViewerViewModel(getApplicationContext(), this);
    }

    @Override
    public int getVariable() {
        return BR.photoViewerViewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_viewer;
    }

    /**
     * Loads PhotoItem for ViewModel
     */
    @Override
    public PhotoItem getPhotoItemFromIntent() {
        return getIntent().getParcelableExtra(EXTRA_PHOTO_ITEM);
    }

    public void setTitle(String title) {
        Objects.requireNonNull(
                getSupportActionBar()
        ).setTitle(getString(R.string.app_name) + " " + title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.backToMain) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSavedFileDialog(String fileAddress) {
        // pass null as the parent view because its going in the dialog layout
        @SuppressLint("InflateParams") final View viewWithEditText = getLayoutInflater().inflate(R.layout.download_complete_dialog, null);
        // get EditText from inflated view
        final EditText fileAddressEditText = viewWithEditText.findViewById(R.id.dialog_file_address);
        fileAddressEditText.setText(fileAddress);

        String title = getString(R.string.dialog_download_success);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_done)
                .setTitle(title)
                .setView(viewWithEditText)
                .setPositiveButton(android.R.string.ok, null)
                .create();

        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public boolean isWriteEnabled() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestWritePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);

    }
}
