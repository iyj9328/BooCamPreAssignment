package com.my.boostcamppreassignment.data.response;

import com.my.boostcamppreassignment.data.MovieDetailData;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    public String lastBuildDate;
    public int total;
    public int start;
    public int display;
    public ArrayList<MovieDetailData> items;
}
