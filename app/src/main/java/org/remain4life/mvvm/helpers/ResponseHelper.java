package org.remain4life.mvvm.helpers;

import com.google.gson.JsonObject;

import org.json.JSONException;

public class ResponseHelper {

    /**
     * Creates StatusResponse object and checks it's status
     *
     * @param jsonObject JSON data from server
     * @param okParser   Function interface to get needed object from JSON
     * @param <T>        class of needed object
     * @return needed <T> object from JSON if statusResponse is Ok
     * @throws ResponseException in other cases
     */
    public static <T> T responseDataOrThrow(JsonObject jsonObject, StatusResponse.Parser<T> okParser) throws ResponseException, JSONException {
        final StatusResponse<T> statusResponse = new StatusResponse<>(jsonObject, okParser);
        if (statusResponse.status == StatusResponse.Status.Ok) {
            return okParser.apply(jsonObject);
        } else {
            throw new ResponseException(statusResponse);
        }
    }

    /**
     * Checks only response status
     *
     * @param jsonObject JSON response from server
     * @throws ResponseException if response is not correct
     */
    public static boolean okResponseOrThrow(JsonObject jsonObject) throws ResponseException {
        final StatusResponse statusResponse = Application.GSON.fromJson(jsonObject, StatusResponse.class);;

        if (statusResponse.status != StatusResponse.Status.Ok) {
            throw new ResponseException(statusResponse);
        }

        return true;
    }
}
