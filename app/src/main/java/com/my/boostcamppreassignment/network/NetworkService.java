package com.my.boostcamppreassignment.network;

import com.my.boostcamppreassignment.data.MovieSearchData;
import com.my.boostcamppreassignment.data.response.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("v1/search/movie.json")
    Call<SearchResponse> SEARCH_DATA_CALL(
            @Query("query") String MovieName,
            @Query("display") int disaply,
            @Query("start") int start
    );
}
