package com.example.recipeApp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.recipeApp.RecipeObject.Ingredient;

import java.util.List;

public class ActivityIngredients extends Activity implements FragmentIngredients.ShoppingListFragmentListener {

    FragmentIngredients ingredientListFragment;
    private Button addToShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.recipeApp.R.layout.activity_ingredients);
        addToShoppingList = (Button) findViewById(com.example.recipeApp.R.id.addToShoppingList);
        addToShoppingList.setOnClickListener(addToShoppingListListener);


        Intent i = getIntent();
        String recipeID = i.getExtras().getString("recipeID");

        if (savedInstanceState != null) {
            return;
        }

        if (findViewById(com.example.recipeApp.R.id.ingredientLayout) != null) {
            ingredientListFragment = new FragmentIngredients();

            Bundle arguments = new Bundle();
            arguments.putString("recipeID", recipeID);
            ingredientListFragment.setArguments(arguments);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(com.example.recipeApp.R.id.ingredientLayout, ingredientListFragment);
            transaction.commit();
        }

    }

    public OnClickListener addToShoppingListListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Log.v("Rob_Debug", "You clicked on the addToShoppingListButton");

            if (ActivityMain.readFavorites(getApplicationContext()) != null) {
                Log.v("Rob_Debug", "It is not null");
                Intent i = new Intent(ActivityIngredients.this, ActivityShoppingList.class);
                startActivity(i);
            } else {
                Log.v("Rob_Debug", "It is null");
                Toast.makeText(getApplicationContext(), "You must select a recipe to Add to favorites", Toast.LENGTH_LONG).show();
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.recipeApp.R.menu.activity_ingredients, menu);
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

    @Override
    public void onRecipeSelected(long rowID, String recipeSend) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAddRecipe() {
        // TODO Auto-generated method stub

    }

    @Override
    public void shoppingListListener(List<Ingredient> shoppingList) {
        Log.d("RobDebug", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!You are in the shoppingListListener method!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        ActivityMain.writeShoppingList(getApplicationContext(), shoppingList);
        for (int i = 0; i < shoppingList.size(); i++) {
            Log.d("RobDebug", "Ingredients Checked: " + shoppingList.get(i).getName());
        }

    }
}
