package com.nanodegree.android.stevenson.popularmovies.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nanodegree.android.stevenson.popularmovies.data.network.helpers.ApiKeyInterceptor;
import com.nanodegree.android.stevenson.popularmovies.data.network.helpers.MoviesDeserializer;
import com.nanodegree.android.stevenson.popularmovies.data.network.helpers.NetworkConnectionInterceptor;
import com.nanodegree.android.stevenson.popularmovies.data.network.helpers.ReviewsDeserializer;
import com.nanodegree.android.stevenson.popularmovies.data.network.helpers.TrailersDeserializer;
import com.nanodegree.android.stevenson.popularmovies.model.Movie;
import com.nanodegree.android.stevenson.popularmovies.model.Review;
import com.nanodegree.android.stevenson.popularmovies.model.Trailer;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static final ApiKeyInterceptor apiKeyInterceptor = new ApiKeyInterceptor();

    private static final NetworkConnectionInterceptor networkConnectionInterceptor = new NetworkConnectionInterceptor();

    private static final OkHttpClient.Builder okHttp =
            new OkHttpClient.Builder()
                    .addInterceptor(networkConnectionInterceptor)
                    .addInterceptor(apiKeyInterceptor);

    private static final Gson gson =
            new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Movie>>() {}.getType(), new MoviesDeserializer())
                .registerTypeAdapter(new TypeToken<List<Trailer>>() {}.getType(), new TrailersDeserializer())
                .registerTypeAdapter(new TypeToken<List<Review>>() {}.getType(), new ReviewsDeserializer())
                .create();

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttp.build());

    private static final Retrofit retrofit = builder.build();

    public static <T> T getService(Class<T> serviceType) {
        return retrofit.create(serviceType);
    }
}
