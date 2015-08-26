package com.example.recipeApp;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FragmentShoppingList extends ListFragment {

    private AdapterShoppingList simpleAdpt;
    //private Map<String, String> shoppingData;
    List<RecipeObject.Ingredient> shoppingListdata;
    private FragmentShoppingList fragmentShoppingList;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shoppingListdata = ActivityMain.readShoppingList(getActivity().getApplicationContext());
        if (shoppingListdata == null) {
            shoppingListdata = new ArrayList<>();
        }

        new LongOperation().execute("");
        fragmentShoppingList = this;
        simpleAdpt = new AdapterShoppingList(getActivity(), com.example.recipeApp.R.layout.shoppinglist_item_view, shoppingListdata, fragmentShoppingList);
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

            if (shoppingListdata != null) {
                Log.v("Rob_Debug", "It is not null, from ShoppingListFragment");
//                for (int i = 0; i < shoppingListdata.size(); i++) {
//                    //shoppingListdata.add(new RecipeObject().new Ingredient(shoppingListdata.get(i).getName(), "", "", "", "", ""));
//                    //Log.d("RobDebug","key is: "+key);
//                    Log.d("RobDebug", "Recipes Checked: " + shoppingListdata.get(i).getName());
//                }

            } else {
                Log.v("Rob_Debug", "It is null, from ShoppingListFragment");
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
