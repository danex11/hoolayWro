package pl.op.danex11.hulajwro.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;


public class Coin extends InteractiveTiledObject {
    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);

    }
}
