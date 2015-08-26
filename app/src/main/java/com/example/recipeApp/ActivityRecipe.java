package com.example.recipeApp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

public class ActivityRecipe extends Activity implements FragmentRecipeList.RecipeFragmentListener {

    public static final String ROW_ID = "row_id";
    private FragmentRecipeList recipeListFragment;
    private Button addToFavoriteButton;
    private Map<String, String> favoriteRecipes;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(com.example.recipeApp.R.layout.activity_recipe);
        addToFavoriteButton = (Button) findViewById(com.example.recipeApp.R.id.addToShoppingList);
        addToFavoriteButton.setOnClickListener(addToFavoriteListener);

        Intent i = getIntent();
        String searchString = i.getExtras().getString("searchStringkey");

        if (savedInstanceState != null) {
            return;
        }

        if (findViewById(com.example.recipeApp.R.id.recipeLayout2) != null) {
            recipeListFragment = new FragmentRecipeList();

            Bundle arguments = new Bundle();
            arguments.putString("searchStringkey", searchString);
            recipeListFragment.setArguments(arguments);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(com.example.recipeApp.R.id.recipeLayout2, recipeListFragment);
            transaction.commit();
        }

    }

    public OnClickListener addToFavoriteListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Log.v("Rob_Debug", "You clicked on the addToFavoriteButton");

            if (ActivityMain.readFavorites(getApplicationContext()) != null) {
                Log.v("Rob_Debug", "It is not null");
                Intent i = new Intent(ActivityRecipe.this, ActivityFavorites.class);
                startActivity(i);
            } else {
                Log.v("Rob_Debug", "It is null");
                Toast.makeText(getApplicationContext(), "You must select a recipe to Add to favorites", Toast.LENGTH_LONG).show();
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(com.example.recipeApp.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == com.example.recipeApp.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecipeSelected(long rowID, String recipeIDstring) {
        if (findViewById(com.example.recipeApp.R.id.recipeLayout2) != null) {
            displayRecipe(rowID, com.example.recipeApp.R.id.recipeLayout2, recipeIDstring);
        }

    }

    private void displayRecipe(long rowID, int viewID, String recipeIDstring) {
        FragmentIngredients detailsFragment = new FragmentIngredients();

        Bundle arguments = new Bundle();
        arguments.putLong(ROW_ID, rowID);
        arguments.putString("recipe", recipeIDstring);
        detailsFragment.setArguments(arguments);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(viewID, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAddRecipe() {

    }


    @Override
    public void recipeFragmentFavoriteListener(Map<String, String> favoriteRecipes) {
        ActivityMain.writeFavorites(getApplicationContext(), favoriteRecipes);
        for (Map.Entry<String, String> entry : favoriteRecipes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //Log.d("RobDebug","key is: "+key);
            Log.d("RobDebug", "Recipes Checked: " + value);
        }
    }


}
