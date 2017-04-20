package com.ga1.hsktextdifficultychecker;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamil on 12/21/2015.
 */
public class HskDatabase {

    private Context context;
    private Map<Character, Integer> charactersToLevel;

    public HskDatabase(Context context) {
        this.context = context;
        charactersToLevel = new HashMap<>();
        loadHskCharacters();
    }

    private void loadHskCharacters() {
        loadHsk(R.raw.hsk1, 1);
        loadHsk(R.raw.hsk2, 2);
        loadHsk(R.raw.hsk3, 3);
        loadHsk(R.raw.hsk4, 4);
        loadHsk(R.raw.hsk5, 5);
        loadHsk(R.raw.hsk6, 6);
    }

    private void loadHsk(int hskFile, int level) {
        String hskString = loadStringFromFile(hskFile);
        for (int i = 0; i < hskString.length(); i++){
            char c = hskString.charAt(i);
            charactersToLevel.put(c, level);
        }
    }

    public int getLevel(Character c) {
        if (!charactersToLevel.containsKey(c))
            return 0;
        return charactersToLevel.get(c);
    }

    private String loadStringFromFile(int fileId) {
        InputStream is = context.getResources().openRawResource(fileId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = is.read();
            while (i != -1)
            {
                byteArrayOutputStream.write(i);
                i = is.read();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }


}
