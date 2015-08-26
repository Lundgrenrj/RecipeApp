package com.example.recipeApp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.List;

public class AdapterRecipe extends ArrayAdapter<RecipeObject.RecipeInfo> {

    public interface AdapterInterface {
        void checkBoxChecked(String recipeID, String recipeName);

        void checkBoxUnChecked(String recipeID, String recipeName);
    }

    private final List<RecipeObject.RecipeInfo> list;
    private final Activity activityContext;
    private int layoutResourceId;
    private AdapterInterface checkBoxListener;
    private ViewHolder viewHolder;

    public AdapterRecipe(Activity activityContext, int layoutResourceId, List<RecipeObject.RecipeInfo> list, FragmentRecipeList fragmentRecipeList) {
        super(activityContext, layoutResourceId, list);
        this.activityContext = activityContext;
        this.layoutResourceId = layoutResourceId;
        this.list = list;
        checkBoxListener = fragmentRecipeList;

    }

    static class ViewHolder {
        protected TextView recipeItemTextView;
        protected CheckBox recipeItemCheckBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            LayoutInflater inflator = activityContext.getLayoutInflater();
            rowView = inflator.inflate(com.example.recipeApp.R.layout.recipe_list_item_view, null);
            viewHolder = new ViewHolder();

            viewHolder.recipeItemTextView = (TextView) rowView.findViewById(com.example.recipeApp.R.id.recipeItemTextView);
            viewHolder.recipeItemCheckBox = (CheckBox) rowView.findViewById(com.example.recipeApp.R.id.recipeItemCheckBox);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
            //rowView = convertView;
            //((ViewHolder) rowView.getTag()).recipeItemCheckBox.setTag(list.get(position));
        }

        final RecipeObject.RecipeInfo element = list.get(position);

        viewHolder.recipeItemTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("RobDebug", "You cliecked on this, ID is: " + element.getRecipeID());
                Intent i = new Intent(activityContext, ActivityIngredients.class);
                i.putExtra("recipeID", element.getRecipeID());
                arg0.getContext().startActivity(i);


            }

        });
        viewHolder.recipeItemCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RecipeObject.RecipeInfo checkElement = list.get(position);

                //final boolean isChecked = viewHolder.recipeItemCheckBox.isChecked();
                checkElement.setSelected(isChecked);

                if (checkElement.isSelected()) {
                    checkBoxListener.checkBoxChecked(list.get(position).getRecipeID(), list.get(position).getTitle());
                    //Log.d("RobDebug", "You just got Checked!!!!!!!!!!!!!!!!");
                } else {
                    checkBoxListener.checkBoxUnChecked(list.get(position).getRecipeID(), list.get(position).getTitle());
                    //Log.d("RobDebug", "You just got UN-Checked!!!!!!!!!!!!!!!!");
                }


            }
        });

        //viewHolder.recipeItemCheckBox.setTag(list.get(position));

        //ViewHolder holder = (ViewHolder) rowView.getTag();
        viewHolder.recipeItemTextView.setText(list.get(position).getTitle());
        viewHolder.recipeItemCheckBox.setChecked(list.get(position).isSelected());
        return rowView;
    }

}