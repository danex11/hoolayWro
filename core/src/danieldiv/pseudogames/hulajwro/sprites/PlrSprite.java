package danieldiv.pseudogames.hulajwro.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.awt.SplashScreen;

import danieldiv.pseudogames.hulajwro.Screens.FahrenScreen;

import static danieldiv.pseudogames.hulajwro.SpielFahre.PPM;

public class PlrSprite extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion plrStandstill;
    //private Rectangle rectFeet;
    private Rectangle rectFeet = new Rectangle();

    public PlrSprite(World world, FahrenScreen screen) {
        super(screen.getAtlas().findRegion("jelen"));
        this.world = world;
        definePlr();
        //get texture for stand pose
        plrStandstill = new TextureRegion(getTexture(), 230, 22, 39, 32);
        //size of sprite on the screen
        setBounds(0, 0, 80 / PPM, 80 / PPM);
        setRegion(plrStandstill);
    }

    public void definePlr() {
        //def body
        BodyDef bodydef = new BodyDef();
        bodydef.position.set(32 / PPM, 128 / PPM);
        bodydef.type = BodyDef.BodyType.DynamicBody;
        bodydef.fixedRotation = true;
        //BODY in kgm   BODY in kgm BODY in kgm BODY in kgm
        //we are writing in meters and kg
        //if things are too big or too small we change the camera magnification
        //place body in world
        b2body = world.createBody(bodydef);
        //fixture def
        FixtureDef fdef = new FixtureDef();
        //circle shape?
        fdef.restitution = 0.6f;
        fdef.density = 0.4f;
        CircleShape circle = new CircleShape();
        circle.setRadius((float) 0.7);
        //rectangle shape for legs area?

        rectFeet.setWidth((float) 0.7);
        rectFeet.setHeight((float) 0.1);
        PolygonShape plrShape = new PolygonShape();
        plrShape.setAsBox(rectFeet.getWidth(), rectFeet.getHeight());

        fdef.shape = plrShape;
        b2body.createFixture(fdef);

    }

    public void updatee(float dt) {
      //  rectFeet.setWidth((float) 0.7);
       // rectFeet.setHeight((float) 0.1);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - rectFeet.getHeight() );
    }
}
