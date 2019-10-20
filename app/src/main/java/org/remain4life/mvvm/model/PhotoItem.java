package org.remain4life.mvvm.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import org.remain4life.mvvm.BR;

import com.google.gson.annotations.SerializedName;

import org.remain4life.mvvm.helpers.PhotosQuery;

public class PhotoItem extends BaseObservable implements Parcelable {
    @SerializedName(PhotosQuery.PQ_ID)
    private String id;
    @SerializedName(PhotosQuery.PQ_DESCRIPTION)
    private String description;
    @SerializedName(PhotosQuery.PQ_ALT_DESCRIPTION)
    private String altDescription;

    // local property
    private boolean isFavourite;

    @SerializedName(PhotosQuery.PQ_URLS)
    private PhotoUrls photoUrls;

    @SerializedName(PhotosQuery.PQ_LINKS)
    private Links links;

    @SerializedName(PhotosQuery.PQ_USER)
    private User author;

    public PhotoItem(String id, String description, String altDescription, boolean isLiked, PhotoUrls photoUrls, Links links, User author) {
        this.id = id;
        this.description = description;
        this.altDescription = altDescription;
        this.isFavourite = isLiked;
        this.photoUrls = photoUrls;
        this.links = links;
        this.author = author;
    }


    protected PhotoItem(Parcel in) {
        id = in.readString();
        description = in.readString();
        altDescription = in.readString();
        isFavourite = in.readByte() != 0;
        photoUrls = in.readParcelable(PhotoUrls.class.getClassLoader());
        links = in.readParcelable(Links.class.getClassLoader());
        author = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(altDescription);
        dest.writeByte((byte) (isFavourite ? 1 : 0));
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

    @Bindable
    public PhotoUrls getPhotoUrls() {
        return photoUrls;
    }

    @Bindable
    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
        notifyPropertyChanged(BR.favourite);
    }

    @NonNull
    @Override
    public String toString() {
        return "PhotoItem{" +
                "thumb='" + photoUrls.getThumb() + '\'' +
                ", description='" + description + '\'' +
                ", altDescription='" + altDescription + '\'' +
                ", isFavourite='" + isFavourite + '\'' +
                '}';
    }

    public Spanned getPhotoSignature() {
        String html = "Photo by <a href=\"" + author.getUserLinks().getHtml() + "\">" + author.getName() + "</a> from " +
                "<a href=\"https://unsplash.com\">Unsplash</a>";

        return Html.fromHtml(html);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAltDescription() {
        return altDescription;
    }

    public Links getLinks() {
        return links;
    }

    public User getAuthor() {
        return author;
    }
}
