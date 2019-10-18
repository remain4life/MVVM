package org.remain4life.mvvm.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.R;
import org.remain4life.mvvm.databinding.ActivityPhotoViewerBinding;
import org.remain4life.mvvm.model.PhotoItem;
import org.remain4life.mvvm.viewmodels.PhotoViewerViewModel;
import org.remain4life.mvvm.views.base.BaseActivity;
import org.remain4life.mvvm.views.base.IPhotoViewerNavigator;

import java.util.Objects;

public class PhotoViewerActivity extends BaseActivity<ActivityPhotoViewerBinding, PhotoViewerViewModel>
        implements IPhotoViewerNavigator {

    public static final String EXTRA_PHOTO_ITEM = "extra_photo_item";

    /**
     * Creates intent to start this activity with parcelable PhotoItem
     *
     * @param context activity to start from
     * @param photoItem PhotoItem for adding ti Intent
     * @return Intent with PhotoItem
     */
    public static Intent createIntent(Context context, PhotoItem photoItem){
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
        return new PhotoViewerViewModel(this, this);
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
}
