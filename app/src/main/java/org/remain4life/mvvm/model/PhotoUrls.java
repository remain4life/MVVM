package org.remain4life.mvvm.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.remain4life.mvvm.helpers.PhotosQuery;

public class PhotoUrls extends BaseObservable implements Parcelable {

    @SerializedName(PhotosQuery.PQ_URLS_THUMB)
    private Uri thumb;
    @SerializedName(PhotosQuery.PQ_URLS_REGULAR)
    private Uri regular;

    public PhotoUrls(Uri thumb, Uri regular) {
        this.thumb = thumb;
        this.regular = regular;
    }


    protected PhotoUrls(Parcel in) {
        thumb = in.readParcelable(Uri.class.getClassLoader());
        regular = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(thumb, flags);
        dest.writeParcelable(regular, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoUrls> CREATOR = new Creator<PhotoUrls>() {
        @Override
        public PhotoUrls createFromParcel(Parcel in) {
            return new PhotoUrls(in);
        }

        @Override
        public PhotoUrls[] newArray(int size) {
            return new PhotoUrls[size];
        }
    };

    @Bindable
    public Uri getThumb() {
        return thumb;
    }

    @Bindable
    public Uri getRegular() {
        return regular;
    }

    @Override
    public String toString() {
        return "PhotoUrls{" +
                "thumb=" + thumb +
                ", regular=" + regular +
                '}';
    }
}
