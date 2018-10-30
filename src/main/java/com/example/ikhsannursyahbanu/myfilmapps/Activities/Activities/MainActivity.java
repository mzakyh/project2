package com.example.ikhsannursyahbanu.myfilmapps.Activities.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ikhsannursyahbanu.myfilmapps.Activities.Adapters.RecyclerViewAdapter;
import com.example.ikhsannursyahbanu.myfilmapps.Activities.Model.Movie;
import com.example.ikhsannursyahbanu.myfilmapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private  final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=7c7c81a2c6f615c204eab26f0022c35a";
    private List<Movie> lstMovie;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv_judul = findViewById(R.id.movie_name);
        final TextView tv_sinopsis = findViewById(R.id.movie_sinopsis);
        final TextView tv_rating = findViewById(R.id.movie_rating);
        final ImageView img = findViewById(R.id.thumbnail);
        recyclerView = findViewById(R.id.recyclerviewId);
        lstMovie = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String responseData = response.body().string();
                    for (int i = 0; i < response.body().contentLength(); i++) {
                        try {
                            JSONObject objData = new JSONObject(responseData);
                            final JSONArray arrayMovie = objData.getJSONArray("MovieDb");
                            final JSONObject objMovie = new JSONObject(arrayMovie.get(0).toString());
                            final JSONObject objImg = new JSONObject(objData.get("genre_ids").toString());
                            final JSONObject objOverview = new JSONObject(objData.get("overview").toString());

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String urlIcon = "http://image.tmdb.org/t/p/w185/" + objImg.get("poster_path") + ".jpg";
                                        Glide.with(MainActivity.this)
                                                .load(urlIcon)
                                                .into(img);
                                        tv_judul.setText(objMovie.get("title").toString());
                                        tv_rating.setText(objMovie.get("average_rate").toString());
                                        tv_sinopsis.setText(objOverview.get("overview").toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
