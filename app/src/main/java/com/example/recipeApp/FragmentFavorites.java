package com.example.recipeApp;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentFavorites extends ListFragment {

    private AdapterFavorites simpleAdpt;
    private Map<String, String> favoriteRecipeData;
    List<RecipeObject.RecipeInfo> searchrecipeData = new ArrayList<>();
    private FragmentFavorites fragmentFavorites;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoriteRecipeData = ActivityMain.readFavorites(getActivity().getApplicationContext());
        new LongOperation().execute("");
        fragmentFavorites = this;
        simpleAdpt = new AdapterFavorites(getActivity(), com.example.recipeApp.R.layout.favorites_list_item_view, searchrecipeData, fragmentFavorites);
        setListAdapter(simpleAdpt);

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    private class LongOperation extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            if (favoriteRecipeData != null) {
                Log.v("Rob_Debug", "It is not null, from FavoriteFragment");
                for (Map.Entry<String, String> entry : favoriteRecipeData.entrySet()) {
                    searchrecipeData.add(new RecipeObject().new RecipeInfo(entry.getKey(), entry.getValue(), "", "", "", "", ""));
                    String key = entry.getKey();
                    String value = entry.getValue();
                    //Log.d("RobDebug","key is: "+key);
                    Log.d("RobDebug", "Recipes Checked: " + value);
                }

            } else {
                Log.v("Rob_Debug", "It is null, from FavoriteFragment");
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
    }


}
