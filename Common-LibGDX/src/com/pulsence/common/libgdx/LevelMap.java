package com.pulsence.common.libgdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class LevelMap {
	
	private TiledMap map;
	
	private static final String LEVEL_DIFFICULTY_PROPERTY = "level-difficulty";
	public int levelDifficulty;
	
    private OrthogonalTiledMapRenderer mapRenderer;
    
    private static final String TILE_PROPERTY_TRUE_VALUE = "true";
    
    private TiledMapTileLayer floorLayer;
    private static final String COLLIDABLE_TILE_PROPERTY = "collide";
    
    private TiledMapTileLayer interactionLayer;
    private float priorX;
    private float priorY;
    private TiledMapTile priorTile;
    private static final String INTERACTABLE_TILE_PROPERTY = "can_interact";
    
    private TiledMapTileLayer spawnLayer;
    private static final String PLAYER_SPAWN_PROPERTY = "player-spawn";
    
    private Vector2 playerSpawn;
    
    private OrthographicCamera camera;
	
	public LevelMap(TiledMap map, OrthographicCamera camera, float unitScale){
		this(map, camera, unitScale, "Floor", "Interactable", "Spawners");
	}
	
	public LevelMap(TiledMap map, OrthographicCamera camera, float unitScale,
					String floorLayerId, String interactebleLayerId, 
					String spawnLayerId){
		this.map = map;
		
		Object prop = getMapProperty(LEVEL_DIFFICULTY_PROPERTY);
		if(prop != null){
			levelDifficulty = Integer.parseInt((String) prop);
		} else {
			levelDifficulty = 1;
		}
		
		
        floorLayer = (TiledMapTileLayer) map.getLayers().get(floorLayerId);
        interactionLayer = (TiledMapTileLayer) map.getLayers().get(interactebleLayerId);
        
        spawnLayer = (TiledMapTileLayer)map.getLayers().get(spawnLayerId);
        
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
        mapRenderer.setView(camera);
        
        this.camera = camera;
	}
	
	/**
	 * If the camera has been updated, this method needs to be called to update
	 * the map when it is rendered.
	 */
	public void syncCamera(){
		mapRenderer.setView(camera);
	}
	
	/**
	 * Checks to see if the block at x, y has the {@value #COLLIDABLE_TILE_PROPERTY}
	 * property set to {@value #TILE_PROPERTY_TRUE_VALUE}.
	 * @param x
	 * @param y
	 * @return True if there is a collision.
	 */
	public boolean checkCollision(float x, float y){
		Object prop = floorLayer.getCell((int)x, (int)y).getTile().
								  getProperties().get(COLLIDABLE_TILE_PROPERTY);
		return prop != null && prop.equals(TILE_PROPERTY_TRUE_VALUE);
	}
	
	public Vector2 getPlayerSpawnLocation(){
		if(playerSpawn != null)
			return playerSpawn;
		
		playerSpawn = new Vector2();
        for(int x = 0; x < spawnLayer.getWidth(); x++){
            for(int y = 0; y < spawnLayer.getHeight(); y++){
                Cell cell = spawnLayer.getCell(x, y);
                if(cell == null)
                    continue;
                Object prop = cell.getTile().getProperties().get(PLAYER_SPAWN_PROPERTY);
                if(prop != null && prop.equals(TILE_PROPERTY_TRUE_VALUE)){
                	playerSpawn.x = x;
                	playerSpawn.y = y;
                    break;
                }
            }
        }
        return playerSpawn;
	}
	
	public boolean canInteract(float x, float y){
		TiledMapTile tile = getIteractionTile(x, y);
		if(tile != null){
			Object prop = tile.getProperties().get(INTERACTABLE_TILE_PROPERTY);
			return prop != null && prop.equals(TILE_PROPERTY_TRUE_VALUE);
		}
		return false;
	}
	
	public Object getInteractPropertyValue(float x, float y, String property){
		TiledMapTile tile = getIteractionTile(x, y);
		
		if(tile == null)
			return null;
		return tile.getProperties().get(property);
	}
	
	/**
	 * Helper method to get tile from the interaction layer. It caches the last
	 * request so if there is another request for the same tile right after, it
	 * does not have to go find it in the layer. This method should be used to
	 * get tiles from the interaction layer 
	 * @param x
	 * @param y
	 * @return
	 */
	private TiledMapTile getIteractionTile(float x, float y){
		if(x != priorX || y != priorY){
			Cell cell = interactionLayer.getCell((int)x, (int)y);
			priorX = x;
			priorY = y;
			if(cell != null)
				priorTile = cell.getTile();
			else
				priorTile = null;
		}
		return priorTile;
	}
	
	public Object getMapProperty(String property){
		return map.getProperties().get(property);
	}
	
	public void draw(){
		mapRenderer.render();
	}
	
	public void dispose(){
		mapRenderer.dispose();
	}
}
