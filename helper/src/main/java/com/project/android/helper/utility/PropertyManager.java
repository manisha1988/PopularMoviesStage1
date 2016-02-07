package com.project.android.helper.utility;

import android.content.Context;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by ManishaKedia on 04/07/2015.
 */
public class PropertyManager {

    private final Properties properties;

    public PropertyManager(Context context, String fileName){
        properties = new Properties();
        try {
            properties.load(context.getAssets().open(fileName + ".properties"));
        }
        catch (IOException ignored) {
        }
    }

    public String getValue(String keyName){
        if(properties != null)
            return properties.getProperty(keyName);

        return null;
    }
}