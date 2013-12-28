package com.pulsence.common.libgdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class MouseHelper {
    private OrthographicCamera camera;
    
    public MouseHelper(OrthographicCamera camera){
        this.camera = camera;
    }
    
    public Vector3 getWorldMousePosition(){
        Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(pos);
        return pos;
    }
}
