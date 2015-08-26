package com.example.recipeApp;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private RecipeObject.Ingredient readIngredient(XmlPullParser parser) throws XmlPullParserException, IOException {
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
            if (tagName.equals("name")) {
                name = readText(parser, "name");
            } else if (tagName.equals("quantity")) {
                quantity = readText(parser, "quantity");
            } else if (tagName.equals("displayQuantity")) {
                displayQuantity = readText(parser, "displayQuantity");
            } else if (tagName.equals("unit")) {
                unit = readText(parser, "unit");
            } else if (tagName.equals("metricQuantity")) {
                metricQuantity = readText(parser, "metricQuantity");
            } else if (tagName.equals("metricUnit")) {
                metricUnit = readText(parser, "metricUnit");
            } else {
                skip(parser);
            }
        }
        ingredient = new RecipeObject().new Ingredient(name, quantity, displayQuantity, unit, metricQuantity, metricUnit);
        return ingredient;
    }

    private String readText(XmlPullParser parser, String text) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, text);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, text);

        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<RecipeObject.Ingredient> recipes = new ArrayList();
        Log.d("RobDebug", "You are in the readFeed method 1");
        parser.require(XmlPullParser.START_TAG, null, "Recipe");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Ingredient")) {
                recipes.add(readIngredient(parser));
            } else {
                skip(parser);
            }
        }
        return recipes;
    }

}