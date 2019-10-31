package org.remain4life.photoloader.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.remain4life.photoloader.model.Links;
import org.remain4life.photoloader.model.PhotoUrls;
import org.remain4life.photoloader.model.User;

@Entity(tableName = "photos")
public class PhotoItemEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String serverId;
    public String description;
    public String altDescription;
    public boolean isFavourite;
    // flag to prevent cleaning old favourites
    public boolean isNew;
    public PhotoUrls photoUrls;
    public Links links;
    public User author;

    // constructor for Room
    public PhotoItemEntity(int id, String serverId, String description, String altDescription, boolean isFavourite, boolean isNew, PhotoUrls photoUrls, Links links, User author) {
        this.id = id;
        this.serverId = serverId;
        this.description = description;
        this.altDescription = altDescription;
        this.isFavourite = isFavourite;
        this.isNew = isNew;
        this.photoUrls = photoUrls;
        this.links = links;
        this.author = author;
    }

    // constructor for manually entity creating without id
    @Ignore
    public PhotoItemEntity(String serverId, String description, String altDescription, boolean isFavourite, boolean isNew, PhotoUrls photoUrls, Links links, User author) {
        this.serverId = serverId;
        this.description = description;
        this.altDescription = altDescription;
        this.isFavourite = isFavourite;
        this.isNew = isNew;
        this.photoUrls = photoUrls;
        this.links = links;
        this.author = author;
    }

    @NonNull
    @Override
    public String toString() {
        return "PhotoItemEntity{" +
                "id=" + id +
                ", serverId='" + serverId + '\'' +
                ", description='" + description + '\'' +
                ", altDescription='" + altDescription + '\'' +
                ", isFavourite=" + isFavourite +
                ", isNew=" + isNew +
                ", photoUrls=" + photoUrls +
                ", links=" + links.getDownload() +
                ", author=" + author.getName() +
                '}';
    }
}
