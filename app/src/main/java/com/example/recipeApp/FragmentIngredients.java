package com.example.recipeApp;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.recipeApp.AdapterIngredients.AdapterInterfaceIngredients;
import com.example.recipeApp.RecipeObject.Ingredient;

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

public class FragmentIngredients extends ListFragment implements AdapterInterfaceIngredients {

    public interface ShoppingListFragmentListener {
        void onRecipeSelected(long rowID, String recipeSend);

        void onAddRecipe();

        void shoppingListListener(List<Ingredient> shoppingList);
    }

    private long rowID = -1;
    private List<Map<String, String>> listViewList = new ArrayList<>();
    private List<Ingredient> ingredientData = new ArrayList<>();

    private static List<Ingredient> shoppingList = new ArrayList<>();
    private ShoppingListFragmentListener listener;

    private static ProgressDialog progressDialog;
    private Context context;
    private ListView ingredientsListView;
    private String recipeIDString = "";
    private AdapterIngredients simpleAdpt;
    private Runnable viewParts;
    private String ingredientSend;
    private FragmentIngredients fragmentIngredients;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ShoppingListFragmentListener) activity;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        fragmentIngredients = this;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        if (savedInstanceState != null) {
            rowID = savedInstanceState.getLong(ActivityRecipe.ROW_ID);
            Log.d("RobDebug", "1_ROW_ID from details is: " + Long.toString(rowID));
        } else {

            Bundle arguments = getArguments();
            if (arguments != null) {
                rowID = arguments.getLong(ActivityRecipe.ROW_ID);
            }
            recipeIDString = arguments.getString("recipeID");
            Log.d("RobDebug", "2_ROW_ID from details is: " + Long.toString(rowID));
        }

        setHasOptionsMenu(true);

        new LongOperation().execute("");
        progressDialog.show();

        ingredientsListView = getListView();
        ingredientsListView.setOnItemClickListener(viewIngredientListener);
        ingredientsListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        simpleAdpt = new AdapterIngredients(getActivity(), com.example.recipeApp.R.layout.ingredient_list_item_view, ingredientData, fragmentIngredients);

        setListAdapter(simpleAdpt);

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            simpleAdpt = new AdapterIngredients(getActivity(), com.example.recipeApp.R.layout.ingredient_list_item_view, ingredientData, fragmentIngredients);

            setListAdapter(simpleAdpt);
        }
    };

    OnItemClickListener viewIngredientListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ingredientSend = ingredientData.get(position).getName();
            listener.onRecipeSelected(id, ingredientSend);
            Log.d("RobDebug", "The Contact ID Chosen is: " + id);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        new LoadContactTask().execute(rowID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ActivityRecipe.ROW_ID, rowID);
    }

    private class LoadContactTask extends AsyncTask<Long, Object, String> {

        @Override
        protected String doInBackground(Long... params) {
            Log.d("RobDebug", "params0 is: " + params[0]);
            return "";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
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

    private void initList() {
        ingredientData = getIngredientsXML(recipeIDString);
        Log.d("RobDebug", "RecipeDataSize is: " + ingredientData.size());
        for (int i = 0; i < ingredientData.size(); i++) {
            listViewList.add(createPlanet("planet", ingredientData.get(i).getName()));
            Log.d("RobDebug", "RecipeDataToString: " + ingredientData.get(i).getName());

        }
    }

    private HashMap<String, String> createPlanet(String key, String name) {
        HashMap<String, String> planet = new HashMap<>();
        planet.put(key, name);

        return planet;
    }

    private static List<Ingredient> getIngredientsXML(String recipeIDString) {
        String AUTH = "dvx7CAs3YEQSGk2AchkN9u2v4Kpher6I";

        List<Ingredient> data = new ArrayList<>();

        HttpURLConnection urlConnection;

        try {

            URL recipeURL = new URL("http://api.bigoven.com/recipe/" + recipeIDString + "?api_key=" + AUTH);

            urlConnection = (HttpURLConnection) recipeURL.openConnection();
            urlConnection.setRequestMethod("GET");

            urlConnection.setRequestProperty("Accept", "text/xml");
            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            byte buffer[] = new byte[4096];
            int count;
            String xmlData = "";
            while ((count = in.read(buffer)) != -1) {
                xmlData += new String(buffer, 0, count);

            }
            Log.d("RobDebug", "XmlDATA: " + xmlData);

            data = getIngredientList(xmlData);

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

    public static List<Ingredient> getIngredientList(String inputString) {
        XmlPullParserFactory factory;
        List<Ingredient> ingredients = new ArrayList<>();
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

    private static Ingredient readIngrdient(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Ingredient");

        Ingredient ingredient = null;
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
    public void checkBoxChecked(String ingredientName) {
        //checkBoxFavorites = recipeID;
        shoppingList.add(new RecipeObject().new Ingredient(ingredientName, "", "", "", "", ""));
        listener.shoppingListListener(shoppingList);
        Log.d("RobDebug", "Ingredient: " + ingredientName + " added");
    }


    @Override
    public void checkBoxUnChecked(String ingredientName) {
        //checkBoxFavorites = recipeID;
        for (int i = 0; i < shoppingList.size(); i++) {
            if (shoppingList.get(i).getName().equals(ingredientName)) {
                shoppingList.remove(i);
            }
        }
        listener.shoppingListListener(shoppingList);
        //Log.d("RobDebug", "Recipe: "+recipeTitle+ " with ID: "+recipeID+" Removed");

    }

}
