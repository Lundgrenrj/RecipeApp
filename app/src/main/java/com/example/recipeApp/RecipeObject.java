package com.example.recipeApp;

import java.util.ArrayList;
import java.util.List;

public class RecipeObject {

    class RecipeSearchResult {

        private List<Results> Results = new ArrayList<>();

    }

    class Results {
        private List<RecipeInfo> recipeInfo = new ArrayList<>();
    }

    class RecipeInfo {
        private boolean selected = false;
        private String recipeID;
        private String title;
        private String cuisine;
        private String category;
        private String subcategory;
        private String webURL;
        private String imageURL120;
        private List<Poster> posters = new ArrayList<>();
        private List<Ingredient> ingredients = new ArrayList<>();

        public RecipeInfo(String recipeID, String title, String cuisine, String category, String subcategory, String webURL, String imageURL120) {
            super();
            this.recipeID = recipeID;
            this.title = title;
            this.cuisine = cuisine;
            this.category = category;
            this.subcategory = subcategory;
            this.webURL = webURL;
            this.imageURL120 = imageURL120;
        }

        public String getRecipeID() {
            return recipeID;
        }

        public void setRecipeID(String recipeID) {
            this.recipeID = recipeID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCuisine() {
            return cuisine;
        }

        public void setCuisine(String cuisine) {
            this.cuisine = cuisine;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(String subcategory) {
            this.subcategory = subcategory;
        }

        public String getWebURL() {
            return webURL;
        }

        public void setWebURL(String webURL) {
            this.webURL = webURL;
        }

        public String getImageURL120() {
            return imageURL120;
        }

        public void setImageURL120(String imageURL120) {
            this.imageURL120 = imageURL120;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }


    }

    class Poster {
    }

    class Ingredient {
        private boolean isChecked = false;
        private String name;
        private String quantity;
        private String displayQuantity;
        private String unit;
        private String metricQuantity;
        private String metricUnit;

        public Ingredient(String name, String quantity, String displayQuantity, String unit, String metricQuantity, String metricUnit) {
            this.name = name;
            this.quantity = quantity;
            this.displayQuantity = displayQuantity;
            this.unit = unit;
            this.metricQuantity = metricQuantity;
            this.metricUnit = metricUnit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDisplayQuantity() {
            return displayQuantity;
        }

        public void setDisplayQuantity(String displayQuantity) {
            this.displayQuantity = displayQuantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getMetricQuantity() {
            return metricQuantity;
        }

        public void setMetricQuantity(String metricQuantity) {
            this.metricQuantity = metricQuantity;
        }

        public String getMetricUnit() {
            return metricUnit;
        }

        public void setMetricUnit(String metricUnit) {
            this.metricUnit = metricUnit;
        }

        public boolean isSelected() {
            return isChecked;
        }

        public void setSelected(boolean isChecked) {
            this.isChecked = isChecked;
        }


    }

}