package org.remain4life.mvvm.adapters;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableList;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.remain4life.mvvm.BR;
import org.remain4life.mvvm.databinding.PhotosItemBinding;
import org.remain4life.mvvm.helpers.AdapterOnListChangedCallback;
import org.remain4life.mvvm.model.PhotoItem;

import java.util.List;

public class PhotosRecyclerViewAdapter
        extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotosViewHolder>
        implements IListAdapter<PhotoItem>{

    private List<PhotoItem> data;
    private final ObservableList.OnListChangedCallback<ObservableList<PhotoItem>> onListChangedCallback = new AdapterOnListChangedCallback<>(this);

    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(viewGroup.getContext());
        PhotosItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, i, viewGroup, false);
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
            photosItemBinding.setVariable(BR.photosViewModel, this);
            photosItemBinding.setPhotosItem(item);
        }

        public void onImage() {
            // on image click
        }

        @Bindable
        public PhotoItem getItem() {
            return item;
        }

        @CallSuper
        public void setItem(PhotoItem item) {
            this.item = item;
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
        if (this.data == data) {
            return;
        }

        if (this.data instanceof ObservableList) {
            ((ObservableList<PhotoItem>)this.data).removeOnListChangedCallback(onListChangedCallback);
        }
        this.data = data;
        if (this.data instanceof ObservableList) {
            ((ObservableList<PhotoItem>)this.data).addOnListChangedCallback(onListChangedCallback);
        }
        notifyDataSetChanged();
    }
}
