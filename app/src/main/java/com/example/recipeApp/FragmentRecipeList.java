package com.example.recipeApp;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.recipeApp.AdapterRecipe.AdapterInterface;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentRecipeList extends ListFragment implements AdapterInterface {

    ListView lv;

    List<RecipeObject.RecipeInfo> searchrecipeData = new ArrayList<>();
    private static Map<String, String> favorites = new HashMap<>();
    Button addToFavorites;

    private AdapterRecipe simpleAdpt;
    private ArrayList<String> tags;
    private RecipeFragmentListener listener;
    private ListView contactListView;
    private String recieveSearchString = "";
    private String recipeSend = "";
    private static ProgressDialog progressDialog;
    private Runnable viewParts;
    private AdapterInterface buttonListener;
    private String checkBoxFavorites;
    private TextView favoriteCounter;
    private int favoritecounter = 1;
    private FragmentRecipeList fragmentRecipeList;

    public interface RecipeFragmentListener {
        void onRecipeSelected(long rowID, String recipeSend);

        void onAddRecipe();

        void recipeFragmentFavoriteListener(Map<String, String> favoriteRecipes);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RecipeFragmentListener) activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup parent = (ViewGroup) inflater.inflate(com.example.recipeApp.R.layout.activity_recipe, container, false);
        parent.addView(v, 0);
        return parent;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        buttonListener = this;
        super.onViewCreated(view, savedInstanceState);
        fragmentRecipeList = this;

        Bundle arguments = getArguments();

        if (arguments != null) {
            recieveSearchString = arguments.getString("searchStringkey");
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        setRetainInstance(true);
        setHasOptionsMenu(true);
        setEmptyText("");

        new LongOperation().execute("");
        progressDialog.show();

        contactListView = getListView();
        contactListView.setOnItemClickListener(viewRecipeListener);
        contactListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        tags = new ArrayList<String>();

        simpleAdpt = new AdapterRecipe(getActivity(), com.example.recipeApp.R.layout.recipe_list_item_view, searchrecipeData, fragmentRecipeList);

        setListAdapter(simpleAdpt);

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            simpleAdpt = new AdapterRecipe(getActivity(), com.example.recipeApp.R.layout.recipe_list_item_view, searchrecipeData, fragmentRecipeList);
            setListAdapter(simpleAdpt);
        }
    };

    OnItemClickListener viewRecipeListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            recipeSend = searchrecipeData.get(position).getRecipeID();
            listener.onRecipeSelected(id, recipeSend);
            Log.d("RobDebug", "The Contact ID Chosen is: " + id);
        }
    };

    private void initList() {
        searchrecipeData = searchRecipeXML(recieveSearchString);
        Log.d("RobDebug", "RecipeDataSize is: " + searchrecipeData.size());
        for (int i = 0; i < searchrecipeData.size(); i++) {

            tags.add(searchrecipeData.get(i).getTitle());
            Log.d("RobDebug", "RecipeDataToString: " + searchrecipeData.get(i).getTitle());

        }
    }

    private static List<RecipeObject.RecipeInfo> searchRecipeXML(String searchString) {
        String AUTH = "dvx7CAs3YEQSGk2AchkN9u2v4Kpher6I";

        List<RecipeObject.RecipeInfo> data = new ArrayList<>();

        String[] searchParam = {"title_kw=", "any_kw="};
        String[] sortString = {"quality", "title", "dateasc", "datedesc", "orderinqueue"};
        String pg = "1";
        String rpp = "50";

        HttpURLConnection urlConnection;

        try {
            URL searchURL = new URL("http://api.bigoven.com/recipes?" + searchParam[0] + searchString + "&pg=" + pg + "&rpp=" + rpp + "&sort=" + sortString[0] + "&api_key=" + AUTH);

            urlConnection = (HttpURLConnection) searchURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "text/xml");

            if (Build.VERSION.SDK_INT > 13) {
                urlConnection.setRequestProperty("Connection", "close");
            }

            urlConnection.connect();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            byte buffer[] = new byte[4096];
            int count;
            String xmlData = "";
            while ((count = in.read(buffer)) != -1) {
                xmlData += new String(buffer, 0, count);

            }
            Log.d("RobDebug", "XmlDATA: " + xmlData);

            data = getRecipeList(xmlData);

            urlConnection.disconnect();
            in.close();

        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return data;
    }

    private class LongOperation extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            initList();

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            simpleAdpt.notifyDataSetChanged();
            viewParts = new Runnable() {
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };

            Thread thread = new Thread(null, viewParts, "MagentoBackground");
            thread.start();

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }
    }

    public static List<RecipeObject.RecipeInfo> getRecipeList(String inputString) {
        XmlPullParserFactory factory;
        List<RecipeObject.RecipeInfo> recipe = new ArrayList<>();
        try {
            factory = XmlPullParserFactory.newInstance();

            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(inputString));
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("RecipeInfo")) {

                        recipe.add(readRecipe(xpp));
                    }
                }

                eventType = xpp.next();
            }

        } catch (XmlPullParserException | IOException e) {

            e.printStackTrace();
        }

        for (int i = 0; i < recipe.size(); i++) {
            Log.d("RobDebug", "Title is: " + i + ": " + recipe.get(i).getTitle());
        }

        return recipe;

    }

    private static RecipeObject.RecipeInfo readRecipe(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "RecipeInfo");

        RecipeObject.RecipeInfo recipe = null;
        String title = null;
        String cuisine = null;
        String category = null;
        String subcategory = null;
        String webURL = null;
        String imageURL120 = null;
        String recipeID = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            if (tagName.equals("Title")) {
                title = readText(parser, "Title");
            } else if (tagName.equals("Cuisine")) {
                cuisine = readText(parser, "Cuisine");
            } else if (tagName.equals("Category")) {
                category = readText(parser, "Category");
            } else if (tagName.equals("Subcategory")) {
                subcategory = readText(parser, "Subcategory");
            } else if (tagName.equals("WebURL")) {
                webURL = readText(parser, "WebURL");
            } else if (tagName.equals("ImageURL120")) {
                imageURL120 = readText(parser, "ImageURL120");
            } else if (tagName.equals("RecipeID")) {
                recipeID = readText(parser, "RecipeID");
            } else {
                skip(parser);
            }
        }

        recipe = new RecipeObject().new RecipeInfo(recipeID, title, cuisine, category, subcategory, webURL, imageURL120);
        return recipe;
    }

    public static List<RecipeObject.Ingredient> getIngredientList(String inputString) {
        XmlPullParserFactory factory;
        List<RecipeObject.Ingredient> ingredients = new ArrayList<>();
        try {
            factory = XmlPullParserFactory.newInstance();

            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(inputString));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equals("Ingredient")) {

                        ingredients.add(readIngrdient(xpp));
                    }
                }

                eventType = xpp.next();
            }

        } catch (XmlPullParserException | IOException e) {

            e.printStackTrace();
        }

        for (int i = 0; i < ingredients.size(); i++) {

            Log.d("RobDebug", "Name is: " + i + ": " + ingredients.get(i).getName());

        }

        return ingredients;

    }

    private static RecipeObject.Ingredient readIngrdient(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Ingredient");

        RecipeObject.Ingredient ingredient = null;
        String name = null;
        String quantity = null;
        String displayQuantity = null;
        String unit = null;
        String metricQuantity = null;
        String metricUnit = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            if (tagName.equals("Name")) {
                name = readText(parser, "Name");
            } else if (tagName.equals("Quantity")) {
                quantity = readText(parser, "Quantity");
            } else if (tagName.equals("DisplayQuantity")) {
                displayQuantity = readText(parser, "DisplayQuantity");
            } else if (tagName.equals("Unit")) {
                unit = readText(parser, "Unit");
            } else if (tagName.equals("MetricQuantity")) {
                metricQuantity = readText(parser, "MetricQuantity");
            } else if (tagName.equals("MetricUnit")) {
                metricUnit = readText(parser, "MetricUnit");
            } else {
                skip(parser);
            }
        }

        ingredient = new RecipeObject().new Ingredient(name, quantity, displayQuantity, unit, metricQuantity, metricUnit);
        return ingredient;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static String readText(XmlPullParser parser, String text) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, text);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, text);

        return result;
    }

    @Override
    public void checkBoxChecked(String recipeID, String recipeTitle) {
        checkBoxFavorites = recipeID;
        favorites.put(recipeID, recipeTitle);
        listener.recipeFragmentFavoriteListener(favorites);
        //Log.d("RobDebug", "Recipe: "+recipeTitle+ " with ID: "+recipeID+" added");
    }


    @Override
    public void checkBoxUnChecked(String recipeID, String recipeTitle) {
        checkBoxFavorites = recipeID;
        favorites.remove(recipeID);
        listener.recipeFragmentFavoriteListener(favorites);
        //Log.d("RobDebug", "Recipe: "+recipeTitle+ " with ID: "+recipeID+" Removed");

    }


}
