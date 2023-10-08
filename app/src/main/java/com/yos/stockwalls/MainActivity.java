package com.yos.stockwalls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yos.stockwalls.Adapters.CuratedAdapter;
import com.yos.stockwalls.Listeners.CuratedResponseListener;
import com.yos.stockwalls.Listeners.OnRecyclerClickListener;
import com.yos.stockwalls.Listeners.SearchResponseListner;
import com.yos.stockwalls.Models.CuratedApiResponse;
import com.yos.stockwalls.Models.Photo;
import com.yos.stockwalls.Models.SearchApiResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRecyclerClickListener {

    RecyclerView recyclerView_home;
    CuratedAdapter adapter;
    ProgressDialog dialog;
    RequestManager manager;
    FloatingActionButton fab_next,fab_prev;
    int page;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_next =findViewById(R.id.fab_next);
        fab_prev = findViewById(R.id.fab_prev);





        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        manager = new RequestManager(this);
        manager.getCuratedWallpapers(listener,"1");

        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String next_page = String.valueOf(page+1);
                manager.getCuratedWallpapers(listener,next_page);
                dialog.show();
            }
        });

        fab_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page>1){
                    String prev_page = String.valueOf(page-1);
                    manager.getCuratedWallpapers(listener,prev_page);
                    dialog.show();
                }
            }
        });
    }
    private final CuratedResponseListener listener = new CuratedResponseListener() {
        @Override
        public void onFetch(CuratedApiResponse response, String message) {
            dialog.dismiss();
            if(response.getPhotos().isEmpty())
            {
                Toast.makeText(MainActivity.this, "No Image Found!!", Toast.LENGTH_SHORT).show();
                return;
            }
            page = response.getPage();
            showData(response.getPhotos());
        }

        @Override
        public void onError(String message) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(List<Photo> photos) {
        recyclerView_home = findViewById(R.id.recycler_home);
        recyclerView_home.setHasFixedSize(true);
        recyclerView_home.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new CuratedAdapter(MainActivity.this,photos,this);
        recyclerView_home.setAdapter(adapter);

    }

    @Override
    public void onClick(Photo photo) {
        startActivity(new Intent(MainActivity.this , WallpaperActivity.class)
                .putExtra("photo",photo));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Your Wallpaper here!");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                manager.SearchCuratedWallpapers(searchResponseListner,"1",query);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private final SearchResponseListner searchResponseListner = new SearchResponseListner() {
        @Override
        public void onFetch(SearchApiResponse response, String message) {
            dialog.dismiss();
            if(response.getPhotos().isEmpty()){
                Toast.makeText(MainActivity.this, "Wallpaper Not Found!", Toast.LENGTH_SHORT).show();
                return;
            }
            showData(response.getPhotos());

        }

        @Override
        public void onError(String message) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}