package org.remain4life.mvvm.helpers;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import org.remain4life.mvvm.R;

import io.reactivex.functions.Function;

/**
 * Class to identify server response and get needed object from it
 */
public class StatusResponse<T> {

    /**
     * Interface to get needed object from JSON
     * @param <T> object class we need to get
     */
    public interface Parser<T> extends Function<JsonObject, T> {
        @Override
        T apply(@NonNull JsonObject jsonObject);
    }

    public enum Status {
        @SerializedName("0")
        Ok,
        @SerializedName(value = "401", alternate = { "403" })
        Unauthorized,
        @SerializedName("")
        Unknown
    }

    public final Status status;
    public final String errorMessage;
    private final int statusCode;
    private final T value;

    private static final Gson GSON = new Gson();

    StatusResponse(JsonObject jsonObject, Parser<T> okParser) {
        // get status code for info
        statusCode = jsonObject.getAsJsonPrimitive(PhotosQuery.PQ_STATUS).getAsInt();

        Status status = GSON.fromJson(jsonObject.getAsJsonPrimitive(PhotosQuery.PQ_STATUS).getAsString(), Status.class);
        if (status == null) {
            status = Status.Unknown;
        }

        this.status = status;

        // if we have success response, we can extract needed data from it
        if (status == Status.Ok) {
            value = okParser.apply(jsonObject);
            errorMessage = null;
        } else {
            // otherwise, we extract error message from response
            value = null;
            if (jsonObject.has("message")) {
                errorMessage = jsonObject.getAsJsonPrimitive("message").getAsString();
            } else {
                // we have unknown error
                errorMessage = Application.getApplication().getString(R.string.error);
            }

        }
    }

    @NonNull
    @Override
    public String toString() {
        return "StatusResponse{" +
                "status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                ", statusCode=" + statusCode +
                ", value=" + value +
                '}';
    }
}
