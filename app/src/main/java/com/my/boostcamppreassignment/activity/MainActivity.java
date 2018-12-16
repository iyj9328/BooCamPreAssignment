package com.my.boostcamppreassignment.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.my.boostcamppreassignment.ApplicationController;
import com.my.boostcamppreassignment.R;
import com.my.boostcamppreassignment.data.MovieDetailData;
import com.my.boostcamppreassignment.data.response.SearchResponse;
import com.my.boostcamppreassignment.network.NetworkStatus;
import com.my.boostcamppreassignment.utils.Utils;
import com.my.boostcamppreassignment.views.adapter.MovieAdapter;
import com.my.boostcamppreassignment.views.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MOVIE_LINK = "movieLink";
    private static final String TAG = "MyActivity";
    private final int PAGINATION_START_DEFAULT_NUM = 1;
    private final int DISPLAY_DEFAULT_NUM = 10;

    private EditText searchTitle;
    private Button searchButton;
    private RecyclerView movieRecyclerview;
    private List<MovieDetailData> movieList;
    private List<MovieDetailData> movieListToBeAdded;
    private MovieAdapter adapter;
    private View.OnClickListener onItemClickListener;
    private LoadingDialog loadingDialog;

    private boolean isScrolling = false;
    private boolean isEndList = false;
    private int visibleCount;
    private int totalCount;
    private int firstVisible;
    private int start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieRecyclerview = findViewById(R.id.recyclerview_movie);
        searchTitle = findViewById(R.id.search_edit);
        searchButton = findViewById(R.id.search_button);

        movieList = new ArrayList<>();
        movieListToBeAdded = new ArrayList<>();
        adapter = new MovieAdapter(getApplicationContext());
        start = PAGINATION_START_DEFAULT_NUM;
        onItemClickListener = this;
        loadingDialog = new LoadingDialog(this);

        //영화목록 사이의 Divider설정
        setMovieRecyclerviewDivider();

        movieRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        movieRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) movieRecyclerview.getLayoutManager();

                visibleCount = layoutManager.getChildCount();           //현재 화면에 보이는 Recyclerview의 Child의 갯수
                totalCount = layoutManager.getItemCount();
                firstVisible = layoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (visibleCount + firstVisible == totalCount)){     //
                    isScrolling = false;
                    if(isEndList){
                        Log.v(TAG, getString(R.string.search_nolist));
                    }else {
                        searchMovie(searchTitle.getText().toString(), false);
                    }
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //페이지네이션에 필요한 start를 DEFAULT_NUM로 할당
                start = PAGINATION_START_DEFAULT_NUM;

                if(searchTitle.getText().toString().equals("")){
                    ApplicationController.instance.makeCustomToast(getString(R.string.toast_please_input_title), Toast.LENGTH_SHORT);
                }else{
                    //키보드를 닫고 LoadingDialog를 실행
                    Utils.closeKeyboard(getApplicationContext(), searchButton);
                    loadingDialog.show();

                    //영화제목으로 해당 제목이 들어간 영화목록 검색
                    searchMovie(searchTitle.getText().toString(), true);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_NOT_CONNECTED){
            ApplicationController.instance.makeCustomToast(getString(R.string.network_notconnected), Toast.LENGTH_SHORT);
        }
    }

    private void setMovieRecyclerviewDivider() {
        DividerItemDecoration verticalItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        Drawable verticalDecoration = ContextCompat.getDrawable(this, R.drawable.vertical_divider);;
        verticalItemDecoration.setDrawable(verticalDecoration);
        movieRecyclerview.addItemDecoration(verticalItemDecoration);
    }

    private void searchMovie(String movieTitle, final boolean isInitial) {
        Call<SearchResponse> searchCall = ApplicationController.instance.networkService.SEARCH_DATA_CALL(movieTitle, DISPLAY_DEFAULT_NUM, start);
        searchCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    if(response.code() == 200 && response.body().total != 0){
                        start += DISPLAY_DEFAULT_NUM;
                        if(isInitial) {
                            movieList = response.body().items;
                            adapter.getItems(movieList);
                            adapter.setOnItemClick(onItemClickListener);
                            movieRecyclerview.setAdapter(adapter);
                            loadingDialog.dismiss();

                        }else{
                            movieListToBeAdded = response.body().items;

                            int movieListSize = movieList.size();
                            for(int i=0; i<movieListToBeAdded.size(); i++) movieList.add(movieListSize + i, movieListToBeAdded.get(i));

                            adapter = new MovieAdapter(getApplicationContext());
                            adapter.getItems(movieList);
                            movieRecyclerview.getAdapter().notifyDataSetChanged();

                            if (response.body().total == totalCount) {
                                isEndList = true;
                            }
                        }
                    }else if(response.body().total == 0){
                        loadingDialog.dismiss();
                        ApplicationController.instance
                                .makeCustomToast("\'" + searchTitle.getText().toString() + "\' "
                                        +getString(R.string.search_no_items), Toast.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.search_call_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int moviePosition = movieRecyclerview.getChildAdapterPosition(v);
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(MOVIE_LINK, movieList.get(moviePosition).link);
        startActivity(intent);
    }
}
