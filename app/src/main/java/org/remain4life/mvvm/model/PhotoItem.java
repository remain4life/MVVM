package org.remain4life.mvvm.model;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.remain4life.mvvm.helpers.PhotosQuery;

public class PhotoItem extends BaseObservable implements Parcelable {
    @SerializedName(PhotosQuery.PQ_ID)
    private int id;
    @SerializedName(PhotosQuery.PQ_DESCRIPTION)
    private int description;
    @SerializedName(PhotosQuery.PQ_ALT_DESCRIPTION)
    private int altDescription;
    @SerializedName(PhotosQuery.PQ_IS_LIKED)
    private boolean isLiked;

    @SerializedName(PhotosQuery.PQ_URLS)
    private PhotoUrls photoUrls;

    @SerializedName(PhotosQuery.PQ_LINKS)
    private Links links;

    @SerializedName(PhotosQuery.PQ_USER)
    private User author;

    public PhotoItem(int id, int description, int altDescription, boolean isLiked, PhotoUrls photoUrls, Links links, User author) {
        this.id = id;
        this.description = description;
        this.altDescription = altDescription;
        this.isLiked = isLiked;
        this.photoUrls = photoUrls;
        this.links = links;
        this.author = author;
    }


    protected PhotoItem(Parcel in) {
        id = in.readInt();
        description = in.readInt();
        altDescription = in.readInt();
        isLiked = in.readByte() != 0;
        photoUrls = in.readParcelable(PhotoUrls.class.getClassLoader());
        links = in.readParcelable(Links.class.getClassLoader());
        author = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(description);
        dest.writeInt(altDescription);
        dest.writeByte((byte) (isLiked ? 1 : 0));
        dest.writeParcelable(photoUrls, flags);
        dest.writeParcelable(links, flags);
        dest.writeParcelable(author, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        @Override
        public PhotoItem createFromParcel(Parcel in) {
            return new PhotoItem(in);
        }

        @Override
        public PhotoItem[] newArray(int size) {
            return new PhotoItem[size];
        }
    };
}
