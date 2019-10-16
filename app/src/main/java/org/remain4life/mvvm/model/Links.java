package org.remain4life.mvvm.model;

import android.databinding.BaseObservable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.remain4life.mvvm.helpers.PhotosQuery;

public class Links extends BaseObservable implements Parcelable {

    @SerializedName(PhotosQuery.PQ_LINKS_SELF)
    private Uri self;
    @SerializedName(PhotosQuery.PQ_LINKS_DOWNLOAD)
    private Uri download;
    @SerializedName(PhotosQuery.PQ_LINKS_HTML)
    private Uri html;

    public Links(Uri self, Uri download, Uri html) {
        this.self = self;
        this.download = download;
        this.html = html;
    }


    protected Links(Parcel in) {
        self = in.readParcelable(Uri.class.getClassLoader());
        download = in.readParcelable(Uri.class.getClassLoader());
        html = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(self, flags);
        dest.writeParcelable(download, flags);
        dest.writeParcelable(html, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Links> CREATOR = new Creator<Links>() {
        @Override
        public Links createFromParcel(Parcel in) {
            return new Links(in);
        }

        @Override
        public Links[] newArray(int size) {
            return new Links[size];
        }
    };
}
