package com.vaagdevi.newsneventsadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Workshops extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<WorkshopsRegdatabase> list;
    WorkshopsMyAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    FloatingActionButton Edit_Workshops;
   /* AdView mAdView;
    private InterstitialAd mInterstitialAd;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshops);

        recyclerView = (RecyclerView) findViewById(R.id.workshopsRV);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.refresh_workshops);
        Edit_Workshops = (FloatingActionButton) findViewById(R.id.edit_workshops);

        Edit_Workshops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Workshops.this,EditWorkshops.class));
            }
        });

       /* // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-2546283744340576~1317058396");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        prepareAD();*/

        list = new ArrayList<WorkshopsRegdatabase>();
        reference = FirebaseDatabase.getInstance().getReference().child("Workshops");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    WorkshopsRegdatabase workshopsRegdatabase = dataSnapshot1.getValue(WorkshopsRegdatabase.class);
                    list.add(workshopsRegdatabase);
                }
                adapter = new WorkshopsMyAdapter(Workshops.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Workshops.this, "Opsss!!!.... Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

        refreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1500);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkConnection();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), Workshops.class));
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }, 1500);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

        });

    }

    public void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (null == activeNetwork) {
            Toast.makeText(Workshops.this, "No Internet Connection!",
                    Toast.LENGTH_LONG).show();
        }
    }

   /* public void prepareAD(){
        mInterstitialAd = new InterstitialAd(this);
        //Test AD Unit : ca-app-pub-3940256099942544/1033173712
        mInterstitialAd.setAdUnitId("ca-app-pub-2546283744340576/2313078313");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {

        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();

            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                }
            });
        }else {
            super.onBackPressed();
        }
    }*/
}
