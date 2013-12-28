package com.pulsence.common.libgdx.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Loads a text file containing Key-Value pairs into a Hash Map. All keys are
 * stored using lower case values. Keys and Values have white space striped
 * from their ends.
 * @author pulsence
 *
 */
public class KVPFile {
    public HashMap<String, String> fileData;
    
    public KVPFile(String filePath) throws IOException{
        fileData = new HashMap<String, String>();
        FileHandle file = Gdx.files.internal(filePath);
        BufferedReader reader = new BufferedReader(file.reader());
        String line;
        while((line = reader.readLine()) != null){
            if(line.startsWith("#")) continue;
            String[] parts = line.split(":", 2);
            if(parts.length != 2)
                throw new IOException("File was malformated, can not load");
            fileData.put(parts[0].toLowerCase().trim(), parts[1].trim());
        }
    }
}
