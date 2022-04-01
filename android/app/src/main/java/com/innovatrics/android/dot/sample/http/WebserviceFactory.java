package com.innovatrics.android.dot.sample.http;

import android.content.Context;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebserviceFactory {

    public static Webservice create(final Context context, final String appServerUrl) {
        // Update security provider, this is needed becauce API < 20 does not support TLSv1.2 by default
        // https://developer.android.com/training/articles/security-gms-provider
        // https://github.com/square/okhttp/issues/2372
        try {
            ProviderInstaller.installIfNeeded(context);
        } catch (final GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            throw new RuntimeException("Unable to update security provider.", e);
        }

        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        final Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(byte[].class, new ByteArrayTypeAdapter())
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(appServerUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(Webservice.class);
    }

}
