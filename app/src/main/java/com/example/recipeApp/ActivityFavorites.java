package com.example.recipeApp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;

public class ActivityFavorites extends Activity {

    private Map<String, String> favorites;
    private FragmentFavorites favoritesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.recipeApp.R.layout.activity_favorites);

        favorites = ActivityMain.readFavorites(getApplicationContext());
        if (favorites != null) {
            Log.v("Rob_Debug", "It is not null, from FavoriteActivity");
        } else {
            Log.v("Rob_Debug", "It is null, from FavoriteActivity");
        }


        if (findViewById(com.example.recipeApp.R.id.favoritesLayout2) != null) {
            favoritesListFragment = new FragmentFavorites();

            Bundle arguments = new Bundle();
            favoritesListFragment.setArguments(arguments);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(com.example.recipeApp.R.id.favoritesLayout2, favoritesListFragment);
            transaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.recipeApp.R.menu.favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == com.example.recipeApp.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
