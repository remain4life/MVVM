package org.remain4life.mvvm.helpers;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.remain4life.mvvm.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class PhotosQuery {

    private interface InternalApi {
        @GET("/photos/")
        Single<JsonArray> getPhotos(
                @Query(PQ_CLIENT_ID) String clientId,
                @QueryMap Map<String, Object> params
        );

        @FormUrlEncoded
        @POST("/")
        Single<JsonObject> post(
                @Query("needed_action") String neededAction,
                @FieldMap Map<String, Object> params
        );

        @FormUrlEncoded
        @PUT("/")
        Single<JsonObject> put(
                @Query("needed_action") String neededAction,
                @FieldMap Map<String, Object> params
        );

        @DELETE("/")
        Single<JsonObject> delete(
                @Query("needed_action") String neededAction,
                @QueryMap Map<String, Object> params
        );

        @Multipart
        @POST("/")
        Single<JsonObject> multipart(
                @Query("needed_action") String neededAction,
                @PartMap Map<String, Object> params
        );
    }

    private static final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(@NonNull ResponseBody value) throws IOException {
            final JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                return adapter.read(jsonReader);
            }
            finally {
                value.close();
            }
        }
    }

    private final static class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private static final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(@NonNull T value) throws IOException {
            if (value instanceof RequestBody) {
                return (RequestBody)value;
            }
            String result = adapter.toJson(value);
            final int length = result.length();
            if (length > 1 && result.charAt(0) == '"' && result.charAt(length - 1) == '"') {
                result = result.substring(1, length - 1);
            }
            return RequestBody.create(MEDIA_TYPE, result);
        }
    }

    private String address;
    private InternalApi internalApi;

    private PhotosQuery() {
        setAddress("https://api.unsplash.com");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;

        /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);*/

        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .cookieJar(
                        new PersistentCookieJar(
                                new SetCookieCache(),
                                new SharedPrefsCookiePersistor(Application.getApplication())
                        )
                )
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                //.addInterceptor(logging)
                ;
        final Gson gson = new Gson();
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                        new Converter.Factory() {
                            @Override
                            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                                final TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
                                return new GsonResponseBodyConverter<>(gson, adapter);
                            }

                            @Override
                            public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                                                  Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
                                final TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
                                return new GsonRequestBodyConverter<>(gson, adapter);
                            }
                        }
                )
                .addConverterFactory(new Converter.Factory() {
                    @Override
                    public Converter<?, String> stringConverter(final Type type, final Annotation[] annotations, final Retrofit retrofit) {
                        if (isQueryOrFieldMap(annotations) && type != String.class && type != Number.class) {
                            return value -> {
                                if (value instanceof String) {
                                    return (String)value;
                                }
                                String result = gson.toJson(value, type);
                                final int length = result.length();
                                if (length > 1 && result.charAt(0) == '"' && result.charAt(length - 1) == '"') {
                                    result = result.substring(1, length - 1);
                                }
                                return result;
                            };
                        }
                        else {
                            return super.stringConverter(type, annotations, retrofit);
                        }
                    }


                    private boolean isQueryOrFieldMap(final Annotation[] annotations) {
                        for ( final Annotation annotation : annotations ) {
                            if (annotation instanceof QueryMap || annotation instanceof FieldMap) {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .client(okHttpClientBuilder.build())
                .baseUrl(address);
        internalApi = retrofitBuilder.build().create(InternalApi.class);
    }

    private static volatile PhotosQuery instance;

    public static PhotosQuery getInstance() {
        PhotosQuery localInstance = instance;
        if (localInstance == null) {
            synchronized (PhotosQuery.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new PhotosQuery();
                }
            }
        }
        return localInstance;
    }

    private static byte[] getBytesFromFileUri(Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = Application.getApplication().getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                return ByteStreams.toByteArray(inputStream);
            }
        } catch (IOException ignored) {
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void addParam(Map<String, Object> params, String param, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Uri) {
            final Uri uri = (Uri)value;
            //noinspection ConstantConditions
            params.put(
                    String.format(Locale.getDefault(), "%1$s\"; filename=\"%2$s", param, uri.getLastPathSegment()),
                    RequestBody.create(
                            MediaType.parse("image/png"),
                            getBytesFromFileUri(uri)
                    )
            );
        }
        else {
            params.put(
                    param,
                    value
            );
        }
    }

    public static final String PQ_STATUS = "status";
    public static final String PQ_ID = "id";
    public static final String PQ_DESCRIPTION = "description";
    public static final String PQ_ALT_DESCRIPTION = "alt_description";
    public static final String PQ_URLS = "urls";
    public static final String PQ_URLS_THUMB = "thumb";
    public static final String PQ_URLS_REGULAR = "regular";
    public static final String PQ_LINKS = "links";
    public static final String PQ_LINKS_SELF = "self";
    public static final String PQ_LINKS_DOWNLOAD = "download";
    public static final String PQ_LINKS_HTML = "html";
    public static final String PQ_USER = "user";
    public static final String PQ_USER_NICK = "username";
    public static final String PQ_USER_NAME = "name";
    public static final String PQ_USER_LOCATION = "location";

    private static final String PQ_CLIENT_ID = "client_id";
    private static final String PQ_PAGE = "page";
    private static final String PQ_PER_PAGE = "per_page";


    /**
     * Loads photos data from Unsplash with needed parameters
     *
     * @param page needed page to load (first by default)
     * @param perPage number of photos per page (10 by default, 30 max according to API docs)
     * @return Single representation of array of Json objects with photo data
     */
    public static Single<JsonArray> loadPhotos(Integer page, Integer perPage) {
        return Single.fromCallable(
                () -> {
                    final Map<String, Object> params = new HashMap<>(2);
                    addParam(params, PQ_PAGE, page);
                    addParam(params, PQ_PER_PAGE, perPage);
                    return params;
                }
        )
                .flatMap(
                        params -> getInstance().internalApi.getPhotos(
                                Application.getApplication().getString(R.string.access_key),
                                params
                        )
                );
    }


}
