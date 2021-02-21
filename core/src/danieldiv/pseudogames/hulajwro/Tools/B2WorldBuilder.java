package danieldiv.pseudogames.hulajwro.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import danieldiv.pseudogames.hulajwro.SpielFahre;
import danieldiv.pseudogames.hulajwro.sprites.Solids;

import static danieldiv.pseudogames.hulajwro.SpielFahre.PPM;

public class B2WorldBuilder {
    public B2WorldBuilder(World world, TiledMap map) {
        //body for Tiles
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //TileObjectLayer 1-solids
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //SCALING of box2d objectlayer
             rect.setWidth(rect.getWidth()*4);
            rect.setHeight(rect.getHeight()*4);
            rect.x = rect.x*4;
            rect.y = rect.y*4;
            //we are giving it our world, map and object rectangle
            //it will pass it to:  Solids>InteractiveTiledObject> there define and set in the world a body for b2d
            new Solids(world, map, rect);


        }

        //TileObjectLayer 2-evilground
        /*
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);


        }

         */


        //******************************* EDGES
        // --- edge shape
        float wpx = (SpielFahre.VIRTUAL_WIDTH / PPM);
        float hpx = (SpielFahre.VIRTUAL_HEIGHT / PPM);

        Body bodyEdgeBottom;
        Body bodyEdgeTop;

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(0, 0, 100 * wpx, 0);


        //--TOP Edge
        BodyDef bodyDefEdgeTop = new BodyDef();
        bodyDefEdgeTop.type = BodyDef.BodyType.StaticBody;
        bodyDefEdgeTop.position.set(0 - wpx / 2, (hpx));
        FixtureDef fixtureEdgeTop = new FixtureDef();
        fixtureEdgeTop.shape = edgeShape;
        bodyEdgeTop = world.createBody(bodyDefEdgeTop);
        bodyEdgeTop.createFixture(fixtureEdgeTop);

        //--- Bottom Edge
        BodyDef bodyDefEdgeBottom = new BodyDef();
        bodyDefEdgeBottom.type = BodyDef.BodyType.StaticBody;
        bodyDefEdgeBottom.position.set(0 - wpx / 2, 0);
        FixtureDef fixtureEdgeBottom = new FixtureDef();
        fixtureEdgeBottom.shape = edgeShape;
        bodyEdgeBottom = world.createBody(bodyDefEdgeBottom);
        bodyEdgeBottom.createFixture(fixtureEdgeBottom);


    }
}
