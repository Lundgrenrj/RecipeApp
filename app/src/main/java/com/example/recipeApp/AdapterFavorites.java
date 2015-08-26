package com.example.recipeApp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.recipeApp.RecipeObject.RecipeInfo;

import java.util.List;

public class AdapterFavorites extends ArrayAdapter<RecipeInfo> {


    private List<RecipeInfo> searchrecipeData;
    private final Activity activityContext;
    private int layoutResourceId;
    private ViewHolder viewHolder;
    private FragmentFavorites fragmentFavorites;

    public AdapterFavorites(Activity activityContext, int layoutResourceId, List<RecipeInfo> searchrecipeData, FragmentFavorites fragmentRecipeList) {
        super(activityContext, layoutResourceId, searchrecipeData);
        this.activityContext = activityContext;
        this.layoutResourceId = layoutResourceId;
        this.searchrecipeData = searchrecipeData;
        this.fragmentFavorites = fragmentRecipeList;

    }

    static class ViewHolder {
        protected TextView recipeItemTextView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            LayoutInflater inflator = activityContext.getLayoutInflater();
            rowView = inflator.inflate(com.example.recipeApp.R.layout.favorites_list_item_view, null);
            viewHolder = new ViewHolder();

            viewHolder.recipeItemTextView = (TextView) rowView.findViewById(com.example.recipeApp.R.id.favoritesItemTextView);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
            //rowView = convertView;
            //((ViewHolder) rowView.getTag()).recipeItemCheckBox.setTag(list.get(position));
        }

        final RecipeInfo element = searchrecipeData.get(position);

        viewHolder.recipeItemTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("RobDebug", "You cliecked on this, ID is: " + element.getRecipeID());


            }

        });

        viewHolder.recipeItemTextView.setText(searchrecipeData.get(position).getTitle());
        return rowView;
    }

}