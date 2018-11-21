package com.pws.pateast.api;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.pws.pateast.MyApplication;
import com.pws.pateast.api.interceptor.AuthorisedInterceptor;
import com.pws.pateast.api.interceptor.ConnectivityInterceptor;
import com.pws.pateast.api.interceptor.LoginInterceptor;
import com.pws.pateast.api.model.AccessToken;
import com.pws.pateast.di.component.ApplicationComponent;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Preference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * Created by intel on 06-Apr-17.
 */

public class ServiceBuilder {
    public static final int DATA_LIMIT = 20;
    public static String DEVICE_TOKEN = null;
    public static String DEVICE_TYPE = "a";

    //Live
    /*public static final String API_BASE_URL = "https://www.pateast.co:3016/";
    public static final String SOCKET_BASE_URL = "https://www.pateast.co:3016/";
    public static final String IMAGE_URL = "https://www.pateast.co:3016/";*/

    //Developer
    public static final String API_BASE_URL = "http://www.pateast.co:4095/";
    public static final String SOCKET_BASE_URL = "http://www.pateast.co:4095/";
    public static final String IMAGE_URL = "http://www.pateast.co:4095/";

    //Abhishek
    /*public static final String API_BASE_URL = "http://192.168.100.160:8989/";
    public static final String SOCKET_BASE_URL = "http://192.168.100.160:8989/";
    public static final String IMAGE_URL = "http://192.168.100.160/abhishek/patest-api/";*/

    //Gaurav
   /* public static final String API_BASE_URL = "http://192.168.100.218:6016/";
    public static final String SOCKET_BASE_URL = "http://192.168.100.218:6016/";
    public static final String IMAGE_URL = "http://192.168.100.218:6016/";*/

    //Pankaj
    /*public static final String API_BASE_URL = "http://192.168.100.71:6015/";
    public static final String SOCKET_BASE_URL = "http://192.168.100.71:6015/";
    public static final String IMAGE_URL = "http://192.168.100.71/pankaj/patest-service/";*/

    //Sudesh
    /*public static final String SOCKET_BASE_URL = "http://192.168.100.215:6040/";
    public static final String API_BASE_URL = "http://192.168.100.215:6040/";
    public static final String IMAGE_URL = "http://192.168.100.215:6040/";*/

    //Live Staging
    /*public static final String API_BASE_URL = "http://www.pateast.co:6041/";
    public static final String SOCKET_BASE_URL = "http://www.pateast.co:6041/";
    public static final String IMAGE_URL = "http://www.pateast.co:6041/";*/

    //Constant
    public static final String TOS_URL_ENGLISH = "https://www.pateast.co/en/terms-condition-app";
    public static final String PRIVACY_URL_ENGLISH = "https://www.pateast.co/en/privacy-policy-app";

    public static final String TOS_URL_ARABIC = "https://www.pateast.co/ar/terms-condition-app";
    public static final String PRIVACY_URL_ARABIC = "https://www.pateast.co/ar/privacy-policy-app";


    public static final String CLIENT_ID = "demo";
    public static final String CLIENT_SECRET = "demo";

    private ApplicationComponent applicationComponent;
    private MyApplication myApplication;
    @Inject
    public OkHttpClient.Builder httpClient;
    @Inject
    public Retrofit.Builder builder;
    @Inject
    public Preference preference;

    private AccessToken accessToken;

    public <S> S createLogin(Class<S> serviceClass) {
        httpClient.interceptors().clear();
        httpClient.interceptors().add(new LoginInterceptor(applicationComponent, myApplication.getApplicationContext()));

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public <S> S createToken(Class<S> serviceClass) {
        httpClient.interceptors().clear();
        httpClient.interceptors().add(new ConnectivityInterceptor(applicationComponent, myApplication.getApplicationContext()));
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass) {
        httpClient.interceptors().clear();
        httpClient.interceptors().add(new AuthorisedInterceptor(applicationComponent, myApplication.getApplicationContext()));

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public void setMyApplication(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    public static class Builder {
        private ApplicationComponent applicationComponent;
        private MyApplication myApplication;

        public Builder(MyApplication myApplication) {
            this.myApplication = myApplication;
            applicationComponent = myApplication.getApplicationComponent();
        }

        public ServiceBuilder create() {
            ServiceBuilder serviceBuilder = new ServiceBuilder();
            serviceBuilder.setApplicationComponent(applicationComponent);
            serviceBuilder.setMyApplication(myApplication);
            applicationComponent.inject(serviceBuilder);
            return serviceBuilder;
        }
    }


    // Insert the header. // Header insert in retrofit request builder
    public static Request.Builder requestBuild(Request request, String auth) {
        return request.newBuilder()
                .header("Authorization", auth)
                .method(request.method(), request.body());
    }

    // Insert the header. // Header insert in retrofit request builder
    public static Request.Builder requestBuild(Request request) {
        return request.newBuilder()
                .method(request.method(), request.body());
    }

    public static String basic() {
        return Credentials.basic(CLIENT_ID, CLIENT_SECRET);
    }

    public HashMap<String, String> getParams(String model, String action) {
        HashMap<String, String> params = getParams();
        params.put("model", model);
        params.put("action", action);
        return params;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("langId", preference.getLanguageID());
        params.put("lang", preference.getLanguage());
        return params;
    }

    public HashMap<String, RequestBody> getRequestBodyParams() {
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("langId", prepareStringPart(preference.getLanguageID()));
        params.put("lang", prepareStringPart(preference.getLanguage()));
        return params;
    }

   /* @NonNull
    public MultipartBody.Part prepareFilePart(Context context, String partName, Uri fileUri) {
        // create RequestBody instance from file
        File file = FileUtils.getFile(context, fileUri);
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(getMimeType(context, fileUri)),
                file
        );
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }*/

    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(getMimeType(file)),
                file
        );
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(Context context, String partName, Uri fileUri) {
        // create RequestBody instance from file

        File file = FileUtils.getFile(context, fileUri);
        InputStream inputStream = null;
        byte[] imageBytes = new byte[0];
        try {
            inputStream = context.getContentResolver().openInputStream(fileUri);
            imageBytes = FileUtils.getBytes(inputStream, inputStream.available());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse(getMimeType(file)), imageBytes);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    /**
     * get mime type
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getMimeType(Context context, Uri uri) {
        String extension;
        //Check uri format to avoid null
        if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }

        return extension;
    }

    public static String getMimeType(File file) {
        String extension = null;
        //Check uri format to avoid null
        if (file != null)
            extension = MimeTypeMap.getFileExtensionFromUrl(file.getPath());

        return extension;
    }

    @NonNull
    public RequestBody prepareStringPart(String params) {
        return RequestBody.create(
                MediaType.parse("text/plain"),
                params
        );
    }
}
