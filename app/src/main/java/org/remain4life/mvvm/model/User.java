package org.remain4life.mvvm.model;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.remain4life.mvvm.helpers.PhotosQuery;

public class User extends BaseObservable implements Parcelable {

    @SerializedName(PhotosQuery.PQ_USER_NICK)
    private String nickname;
    @SerializedName(PhotosQuery.PQ_USER_NAME)
    private String name;
    @SerializedName(PhotosQuery.PQ_USER_LOCATION)
    private String location;
    @SerializedName(PhotosQuery.PQ_LINKS)
    private Links userLinks;

    public User(String nickname, String name, String location, Links userLinks) {
        this.nickname = nickname;
        this.name = name;
        this.location = location;
        this.userLinks = userLinks;
    }


    protected User(Parcel in) {
        nickname = in.readString();
        name = in.readString();
        location = in.readString();
        userLinks = in.readParcelable(Links.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeParcelable(userLinks, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Links getUserLinks() {
        return userLinks;
    }
}
