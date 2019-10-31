package org.remain4life.mvvm.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableList;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.BuildConfig;
import org.remain4life.mvvm.databinding.PhotosItemBinding;
import org.remain4life.mvvm.helpers.AdapterOnListChangedCallback;
import org.remain4life.mvvm.helpers.Constants;
import org.remain4life.mvvm.model.PhotoItem;
import org.remain4life.mvvm.model.PhotoRepository;
import org.remain4life.mvvm.views.PhotoViewerActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static org.remain4life.mvvm.helpers.Constants.APP_TAG;
import static org.remain4life.mvvm.helpers.Constants.DB_TAG;

public class PhotosRecyclerViewAdapter
        extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotosViewHolder>
        implements IListAdapter<PhotoItem> {

    private Context context;
    private List<PhotoItem> data;
    private final ObservableList.OnListChangedCallback<ObservableList<PhotoItem>> onListChangedCallback = new AdapterOnListChangedCallback<>(this);

    public PhotosRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(viewGroup.getContext());
        PhotosItemBinding binding = PhotosItemBinding.inflate(
                layoutInflater, viewGroup, false);
        return new PhotosViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosViewHolder photosViewHolder, int i) {
        photosViewHolder.setItem(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class PhotosViewHolder extends RecyclerView.ViewHolder implements Observable {
        private final PhotosItemBinding photosItemBinding;
        private PhotoItem item;
        private final PropertyChangeRegistry mCallbacks = new PropertyChangeRegistry();

        public PhotosViewHolder(PhotosItemBinding binding) {
            super(binding.getRoot());
            photosItemBinding = binding;
            photosItemBinding.setPhotosViewHolder(this);
        }

        /**
         * Called on image clicked to open activity with regular image size
         */
        @SuppressLint("CheckResult")
        public void onImage() {
            if (BuildConfig.DEBUG) {
                Log.d(APP_TAG, "-> Clicked on image " + item.getId() + ", author " + item.getAuthor().getName());
            }

            PhotoRepository.getInstance()
                    .getPhotoFromDB(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(photoItemEntity -> {
                                if (BuildConfig.DEBUG) {
                                    Log.d(DB_TAG, "-> Photo loaded from DB: " + photoItemEntity.serverId + ", isFavourite = " + photoItemEntity.isFavourite);
                                }

                                if (photoItemEntity.isFavourite) {
                                    item.setFavourite(true);
                                } else {
                                    item.setFavourite(false);
                                }

                                context.startActivity(
                                        PhotoViewerActivity.createIntent(context, item)
                                );


                            },
                            throwable -> Log.e(Constants.ERROR_TAG, throwable.toString())
                    );


        }

        @Bindable
        public PhotoItem getItem() {
            return item;
        }

        public void setItem(PhotoItem item) {
            this.item = item;
            photosItemBinding.setPhotosItem(item);
            notifyPropertyChanged(BR.photosItem);
            notifyPropertyChanged(BR.item);

            photosItemBinding.executePendingBindings();
        }

        // observable implementation
        @Override
        public void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
            mCallbacks.add(callback);
        }

        @Override
        public void removeOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
            mCallbacks.remove(callback);
        }

        public void notifyChange() {
            mCallbacks.notifyCallbacks(this, 0, null);
        }

        public void notifyPropertyChanged(int fieldId) {
            mCallbacks.notifyCallbacks(this, fieldId, null);
        }
    }

    @Override
    public void setData(List<PhotoItem> data) {
        if (BuildConfig.DEBUG) {
            Log.d(APP_TAG, "Set data to adapter: " + data);
        }

        if (this.data == data) {
            return;
        }

        if (this.data instanceof ObservableList) {
            ((ObservableList<PhotoItem>) this.data).removeOnListChangedCallback(onListChangedCallback);
        }
        this.data = data;
        if (this.data instanceof ObservableList) {
            ((ObservableList<PhotoItem>) this.data).addOnListChangedCallback(onListChangedCallback);
        }
        notifyDataSetChanged();
    }
}
