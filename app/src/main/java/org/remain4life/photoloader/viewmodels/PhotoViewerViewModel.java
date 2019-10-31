package org.remain4life.photoloader.viewmodels;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.remain4life.photoloader.BR;
import org.remain4life.photoloader.BuildConfig;
import org.remain4life.photoloader.R;
import org.remain4life.photoloader.model.PhotoItem;
import org.remain4life.photoloader.model.PhotoRepository;
import org.remain4life.photoloader.viewmodels.base.BaseViewModel;
import org.remain4life.photoloader.views.PhotoViewerActivity;
import org.remain4life.photoloader.views.base.IPhotoViewerNavigator;

import java.io.File;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;
import static org.remain4life.photoloader.helpers.Constants.APP_TAG;

public class PhotoViewerViewModel extends BaseViewModel<IPhotoViewerNavigator> {

    private PhotoItem photo;
    private PhotoRepository photoRepo;

    public PhotoViewerViewModel(@NonNull Context context, IPhotoViewerNavigator navigator) {
        super(context, navigator);
        setPhoto(navigator.getPhotoItemFromIntent());
        photoRepo = PhotoRepository.getInstance();
    }

    @Bindable
    public PhotoItem getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoItem photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }

    /**
     * Adds or removes photo from favourites
     */
    public void addToFavourites() {
        boolean newFlag = !photo.isFavourite();
        photo.setFavourite(newFlag);
        photoRepo.cacheFavourite(photo);
    }

    /**
     * Called on download clicked
     */
    public void onDownload() {
        // check permission to save file
        if (!navigator.isWriteEnabled()) {
            //Toast.makeText(context, context.getString(R.string.no_write_permissions), Toast.LENGTH_LONG).show();
            navigator.requestWritePermission();
        } else {
            downloadPicture();
        }
    }

    /**
     * Downloads the opened picture using DownloadManager service
     */
    private void downloadPicture() {
        // file name for local storage
        String filename = String.format(context.getString(R.string.download_name_pattern),
                photo.getAuthor().getNoSpacesName(),
                new Date().getTime() / 1000L);

        // notification download title title
        String title = String.format(context.getString(R.string.download_title), filename);

        String beginMessage = String.format(context.getString(R.string.download_began), filename);
        Toast.makeText(context, beginMessage, Toast.LENGTH_LONG).show();

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        // picture download link
        Uri downloadUri = photo.getLinks().getDownload();

        // request settings
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);
        request.setTitle(title);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, filename);
        // download ID for callback
        long downloadId = downloadManager.enqueue(request);

        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    // file is downloaded
                    String completedMessage = String.format(context.getString(R.string.download_completed), filename);
                    Toast.makeText(context, completedMessage, Toast.LENGTH_SHORT).show();

                    File fileFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    String fileLocation = fileFolder + "/" + filename;

                    if (BuildConfig.DEBUG) {
                        Log.d(APP_TAG, "File location: " + fileLocation);
                    }

                    navigator.showSavedFileDialog(fileLocation);
                }
            }
        };
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PhotoViewerActivity.REQUEST_PERMISSION_WRITE) {
            // permissions granted
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(context, context.getString(R.string.permissions_granted), Toast.LENGTH_LONG).show();
                downloadPicture();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
