package com.pulsence.common.libgdx.input;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;

public class KeyMapping extends KVPFile {
    /**
     * Maps the integer value for the key to if it was pressed or not
     */
    private HashMap<Integer, Boolean> pressedKeys;
    /**
     * Maps a key name to it's integer value for the key
     */
    private HashMap<String, Integer> keyMap;
    
    public KeyMapping(String filePath) throws IOException {
        super(filePath);
        buildKeyMap();
    }
    
    private void buildKeyMap(){
        pressedKeys = new HashMap<Integer, Boolean>();
        keyMap = new HashMap<String, Integer>();
        
        Keys keysObjCopy = new Keys();
        Field keyValue;
        for(String keyName : fileData.keySet()){
            try {
                keyValue = Keys.class.getField(fileData.get(keyName));
                keyMap.put(keyName, keyValue.getInt(keysObjCopy));
                pressedKeys.put(keyMap.get(keyName), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Checks to see which keys have been pressed
     */
    public void checkKeys(){
        for(int keyValue : keyMap.values()){
            pressedKeys.put(keyValue, Gdx.input.isKeyPressed(keyValue));
        }
    }
    
    public boolean isKeyPressed(String keyName){
        return pressedKeys.get(keyMap.get(keyName));
    }

}
