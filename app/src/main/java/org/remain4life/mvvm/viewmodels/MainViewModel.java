package org.remain4life.mvvm.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import org.remain4life.mvvm.R;
import org.remain4life.mvvm.viewmodels.base.BaseViewModel;
import org.remain4life.mvvm.views.FavouritesFragment;
import org.remain4life.mvvm.views.InfoFragment;
import org.remain4life.mvvm.views.base.IMainNavigator;
import org.remain4life.mvvm.views.PhotosFragment;

public class MainViewModel extends BaseViewModel<IMainNavigator> implements BottomNavigationView.OnNavigationItemSelectedListener {
    public MainViewModel(@NonNull Context context, IMainNavigator navigator) {
        super(context, navigator);
        navigator.setTitle(context.getString(R.string.title_photos));
        navigator.setContentFragment(new PhotosFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_favourites:
                navigator.setContentFragment(new FavouritesFragment());
                navigator.setTitle(context.getString(R.string.title_favourites));
                return true;
            case R.id.navigation_photos:
                navigator.setContentFragment(new PhotosFragment());
                navigator.setTitle(context.getString(R.string.title_photos));
                return true;
            case R.id.navigation_info:
                navigator.setContentFragment(new InfoFragment());
                navigator.setTitle(context.getString(R.string.title_info));
                return true;
        }
        return false;
    }
}
