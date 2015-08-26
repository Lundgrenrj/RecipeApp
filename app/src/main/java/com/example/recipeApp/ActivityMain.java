package com.example.recipeApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.recipeApp.RecipeObject.Ingredient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class ActivityMain extends Activity {

    private Button searchButton;
    private EditText searchDialog;
    private Map<String, String> favorites;
    private List<String> shoppingList;
    private Button shoppingListButton;
    private Button favoritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        favorites = ActivityMain.readFavorites(this);
        if (favorites != null) {
            Log.v("Rob_Debug", "favorites is not null");
        } else {
            Log.v("Rob_Debug", "favorites is null");
        }

        super.onCreate(savedInstanceState);
        setContentView(com.example.recipeApp.R.layout.activity_main);

        searchDialog = (EditText) findViewById(com.example.recipeApp.R.id.searchDialogText);
        shoppingListButton = (Button) findViewById(com.example.recipeApp.R.id.gotoShoppingListButton);
        shoppingListButton.setOnClickListener(addToShoppingListListener);
        favoritesButton = (Button) findViewById(com.example.recipeApp.R.id.goToFavoritesButton);
        favoritesButton.setOnClickListener(addToFavoriteListener);
        searchButton = (Button) findViewById(com.example.recipeApp.R.id.addToShoppingList);
        searchButton.setOnClickListener(searchButtonListener);

        searchDialog.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Toast.makeText(ActivityMain.this, "YOU CLICKED ENTER KEY", Toast.LENGTH_LONG).show();
                    if (searchDialog.getText().length() > 0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchDialog.getWindowToken(), 0);
                        Log.v("Rob_Debug", "You clicked on the button");
                        Intent i = new Intent(ActivityMain.this, ActivityRecipe.class);
                        String searchString = searchDialog.getText().toString();
                        i.putExtra("searchStringkey", searchString.replaceAll("\\s+", "&title_kw="));

                        startActivity(i);
                    }
                    return true;

                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(com.example.recipeApp.R.menu.recipe, menu);
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

    public OnClickListener searchButtonListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (searchDialog.getText().length() > 0) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchDialog.getWindowToken(), 0);
                Log.v("Rob_Debug", "You clicked on the button");
                Intent i = new Intent(ActivityMain.this, ActivityRecipe.class);
                String searchString = searchDialog.getText().toString();

                i.putExtra("searchStringkey", searchString);

                startActivity(i);
            }

        }

    };


    public static SharedPreferences getSharedPreferences(Context ctxt) {
        return ctxt.getSharedPreferences("FILE", 0);
    }

    public static Map<String, String> readFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("favoriteMap", MODE_PRIVATE);
        String value = prefs.getString("favoriteMap", null);

        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        Map<String, String> favorites = gson.fromJson(value, new TypeToken<Map<String, String>>() {
        }.getType());
        return favorites;
    }


    public static void writeFavorites(Context context, Map<String, String> favoriteMap) {
        Gson gson = new Gson();
        String value = gson.toJson(favoriteMap);
        SharedPreferences prefs = context.getSharedPreferences("favoriteMap", MODE_PRIVATE);
        Editor e = prefs.edit();
        e.putString("favoriteMap", value);
        e.commit();
    }

    public static List<Ingredient> readShoppingList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("shoppingList", MODE_PRIVATE);
        String value = prefs.getString("shoppingList", null);

        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        List<Ingredient> shoppingList = gson.fromJson(value, new TypeToken<List<Ingredient>>() {
        }.getType());
        return shoppingList;
    }


    public static void writeShoppingList(Context context, List<Ingredient> shoppingList) {
        Gson gson = new Gson();
        String value = gson.toJson(shoppingList);
        SharedPreferences prefs = context.getSharedPreferences("shoppingList", MODE_PRIVATE);
        Editor e = prefs.edit();
        e.putString("shoppingList", value);
        e.commit();
    }


    public OnClickListener addToFavoriteListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Log.v("Rob_Debug", "You clicked on the addToFavoriteButton");
            Intent i = new Intent(ActivityMain.this, ActivityFavorites.class);
            startActivity(i);
        }

    };


    public OnClickListener addToShoppingListListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Log.v("Rob_Debug", "You clicked on the addToShoppingListButton");
            Intent i = new Intent(ActivityMain.this, ActivityShoppingList.class);
            startActivity(i);
        }

    };

}
