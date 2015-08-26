package com.example.recipeApp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.recipeApp.RecipeObject.Ingredient;

import java.util.List;

public class ActivityShoppingList extends Activity {
    private List<Ingredient> shoppingList;
    private FragmentShoppingList shoppingListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.recipeApp.R.layout.activity_activity_shopping_list);

        shoppingList = ActivityMain.readShoppingList(getApplicationContext());
        if (shoppingList != null) {
            Log.v("Rob_Debug", "It is not null, from ShopppingListActivity");
        } else {
            Log.v("Rob_Debug", "It is null, from ShopppingListActivity");
        }


        if (findViewById(com.example.recipeApp.R.id.shoppingListLayout2) != null) {
            shoppingListFragment = new FragmentShoppingList();

            Bundle arguments = new Bundle();
            shoppingListFragment.setArguments(arguments);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(com.example.recipeApp.R.id.shoppingListLayout2, shoppingListFragment);
            transaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.recipeApp.R.menu.activity_shopping_list, menu);
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
