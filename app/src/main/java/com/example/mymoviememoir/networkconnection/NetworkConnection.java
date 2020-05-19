package com.example.mymoviememoir.networkconnection;

import android.util.Log;

import com.example.mymoviememoir.entities.Credential;
import com.example.mymoviememoir.entities.Person;
import com.google.gson.Gson;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NetworkConnection {
    private OkHttpClient client = null;
    private String results;
    private Person person = null;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public NetworkConnection(){
        client= new OkHttpClient();
    }

    private static final String MOVIE_API_KEY = "87c3db1c6af7a7af863738f4711bc40f";
    private static final String GEO_API_KEY = "AIzaSyAYyX5Shw-E8RY_Qo-TjYHvfQ8obbiqmzE";
    private static final String BASE_MOVIEAPI_URL = "https://api.themoviedb.org/3/search/movie?";
    private static final String BASE_URL =
            "http://192.168.18.8:8080/MyMovieMemoir1.0/webresources/";

    public String getAllCredentials(){
        final String methodPath = "moviememoir.credential";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public String getTopFiveMovies(int id){
        final String methodPath = "moviememoir.memoir/findFiveMoviesWithHighestRatingsInRecentYear/" + id;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public String getPersonById(int id){
        final String methodPath = "moviememoir.person/" + id;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public String getAllCinemas(){
        final String methodPath = "moviememoir.cinema";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

   /* public String getLocationCoordinate(String address){
        final String methodPath = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + GEO_API_KEY;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }*/

    public String getMoviesFromApi(String query){
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_MOVIEAPI_URL + "api_key=" + MOVIE_API_KEY + "&query=" + query);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public Person addPerson(String[] personData){
        person = new Person(Integer.parseInt(personData[0]), personData[1], personData[2], personData[3], personData[4], personData[5], personData[6], Integer.parseInt(personData[7]));
        return person;
    }

    public String addCredential(String[] credentialData){
        Credential credential = null;
        credential = new Credential(Integer.parseInt(credentialData[0]), person, credentialData[1], credentialData[2], credentialData[3]);
        Gson gson = new Gson();
        String credentialJson = gson.toJson(credential);
        final String methodPath = "moviememoir.credential";
        RequestBody body = RequestBody.create(credentialJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("json " , credentialJson);
        return results;
    }
}
