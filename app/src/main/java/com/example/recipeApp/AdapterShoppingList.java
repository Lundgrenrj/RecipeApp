package com.example.recipeApp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.recipeApp.RecipeObject.Ingredient;

import java.util.List;

public class AdapterShoppingList extends ArrayAdapter<Ingredient> {


    private List<Ingredient> searchrecipeData;
    private final Activity activityContext;
    private int layoutResourceId;
    private ViewHolder viewHolder;
    private FragmentShoppingList fragmentShoppingList;

    public AdapterShoppingList(Activity activityContext, int layoutResourceId, List<Ingredient> searchrecipeData, FragmentShoppingList fragmentShoppingList) {
        super(activityContext, layoutResourceId, searchrecipeData);
        this.activityContext = activityContext;
        this.layoutResourceId = layoutResourceId;
        this.searchrecipeData = searchrecipeData;
        this.fragmentShoppingList = fragmentShoppingList;

    }

    static class ViewHolder {
        protected TextView recipeItemTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            LayoutInflater inflator = activityContext.getLayoutInflater();
            rowView = inflator.inflate(com.example.recipeApp.R.layout.shoppinglist_item_view, null);
            viewHolder = new ViewHolder();

            viewHolder.recipeItemTextView = (TextView) rowView.findViewById(com.example.recipeApp.R.id.shoppingListItemTextView);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
            //rowView = convertView;
            //((ViewHolder) rowView.getTag()).recipeItemCheckBox.setTag(list.get(position));
        }

        final Ingredient element = searchrecipeData.get(position);

        viewHolder.recipeItemTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("RobDebug", "You cliecked on this, ID is: " + element.getName());


            }

        });

        viewHolder.recipeItemTextView.setText(searchrecipeData.get(position).getName());
        return rowView;
    }

}