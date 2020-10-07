package danieldiv.pseudogames.hulajwro.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import danieldiv.pseudogames.hulajwro.Screens.FahrenScreen;

import static danieldiv.pseudogames.hulajwro.SpielFahre.PPM;

public class PlrSprite extends Sprite {
    public World world;
    public Body b2body;

    //for Animation
    public enum State {STANDING, RUNNING}

    public State currentState;
    public State previousState;
    private TextureRegion plrStandstill;
    // private Animation plrStand;
    private Animation plrRun;
    private float stateTimer;
    private boolean runningRight;

    //private Rectangle rectFeet;
    private Rectangle rectFeet = new Rectangle();

    public PlrSprite(World world, FahrenScreen screen) {
        super(screen.getAtlas().findRegion("jelen"));
        this.world = world;

        //
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //in pixels region data
        String regionName = "jelen";
        int framesNo = 5;
        int widthOfFrame = getRegionWidth() / framesNo;
        int heightOfFrame = getRegionHeight();

        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion(regionName), i * widthOfFrame, 0, widthOfFrame, heightOfFrame));

        plrRun = new Animation(0.1f, frames);
        frames.clear();

        //get texture for stand pose - it should be done by "jelen" txture name
        plrStandstill = new TextureRegion(screen.getAtlas().findRegion(regionName), 0, 0, widthOfFrame, heightOfFrame);
        definePlr();

        //size of sprite on the screen
        setBounds(0, 0, 80 / PPM, 80 / PPM);
        setRegion(plrStandstill);
    }


    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case RUNNING:
                region = (TextureRegion) plrRun.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
                // break;
            default:
                region = plrStandstill;
                break;
        }
        //flip left or right for standing still
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        //TODO study this
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }


    public State getState() {
        if ((b2body.getLinearVelocity().x > 10 || b2body.getLinearVelocity().x < -10) || b2body.getLinearVelocity().y > 10)
            return State.RUNNING;
        else
            return State.STANDING;
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
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - rectFeet.getHeight());
        setRegion(getFrame(dt));
    }
}
