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

import com.example.recipeApp.RecipeObject.Ingredient;

import java.util.List;

public class AdapterIngredients extends ArrayAdapter<Ingredient> {

    public interface AdapterInterfaceIngredients {
        void checkBoxChecked(String recipeID);

        void checkBoxUnChecked(String recipeID);
    }

    private final List<Ingredient> list;
    private final Activity activityContext;
    private int layoutResourceId;
    private AdapterInterfaceIngredients checkBoxListener;
    private ViewHolder viewHolder;

    public AdapterIngredients(Activity activityContext, int layoutResourceId, List<Ingredient> list, FragmentIngredients fragmentIngredientList) {
        super(activityContext, layoutResourceId, list);
        this.activityContext = activityContext;
        this.layoutResourceId = layoutResourceId;
        this.list = list;
        checkBoxListener = fragmentIngredientList;

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
            rowView = inflator.inflate(com.example.recipeApp.R.layout.ingredient_list_item_view, null);
            viewHolder = new ViewHolder();

            viewHolder.recipeItemTextView = (TextView) rowView.findViewById(com.example.recipeApp.R.id.igredientItemTextView);
            viewHolder.recipeItemCheckBox = (CheckBox) rowView.findViewById(com.example.recipeApp.R.id.ingredientItemCheckBox);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
            //rowView = convertView;
            //((ViewHolder) rowView.getTag()).recipeItemCheckBox.setTag(list.get(position));
        }

        final Ingredient element = list.get(position);

        viewHolder.recipeItemTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("RobDebug", "You cliecked on this, Name is: " + element.getName());
                Intent i = new Intent(activityContext, ActivityShoppingList.class);
                i.putExtra("ingredientName", element.getName());
                //i.putExtra("recipeID", element.getName();
                arg0.getContext().startActivity(i);


            }

        });
        viewHolder.recipeItemCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Ingredient checkElement = list.get(position);

                //final boolean isChecked = viewHolder.recipeItemCheckBox.isChecked();
                checkElement.setSelected(isChecked);

                if (checkElement.isSelected()) {
                    checkBoxListener.checkBoxChecked(list.get(position).getName());
                    //Log.d("RobDebug", "You just got Checked!!!!!!!!!!!!!!!!");
                } else {
                    checkBoxListener.checkBoxUnChecked(list.get(position).getName());
                    //Log.d("RobDebug", "You just got UN-Checked!!!!!!!!!!!!!!!!");
                }


            }
        });

        //viewHolder.recipeItemCheckBox.setTag(list.get(position));

        //ViewHolder holder = (ViewHolder) rowView.getTag();
        viewHolder.recipeItemTextView.setText(list.get(position).getName());
        viewHolder.recipeItemCheckBox.setChecked(list.get(position).isSelected());
        return rowView;
    }

}