package pl.op.danex11.hulajwro.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Solids extends InteractiveTiledObject {

    public Solids(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
       // bounds.setWidth(bounds.getWidth()*4);
     }
}
