package org.remain4life.photoloader.helpers;

import java.io.IOException;

/**
 * Class for only non valid server answers
 */
public class ResponseException extends IOException {
    public StatusResponse statusResponse;

    ResponseException(StatusResponse statusResponse) {
        super(statusResponse.errorMessage);
        this.statusResponse = statusResponse;
    }
}
