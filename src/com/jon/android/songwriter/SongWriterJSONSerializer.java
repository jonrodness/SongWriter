package com.jon.android.songwriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class SongWriterJSONSerializer {

    private Context mContext;
    private String mFilename;

    public SongWriterJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }
    
    public ArrayList<Song> loadSongs() throws IOException, JSONException {
        ArrayList<Song> songs = new ArrayList<Song>();
        BufferedReader reader = null;
        try {
            // Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                .nextValue();
            // Build the array of songs from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                songs.add(new Song(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // Ignore this one; it happens when starting fresh
        } finally {
            if (reader != null)
                reader.close();
        }
        return songs;
    }

    public void saveSongs(ArrayList<Song> songs)
            throws JSONException, IOException {
        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Song s : songs){
            array.put(s.toJSON());
        }

        // Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext
                .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
