package pl.op.danex11.hulajwro.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import pl.op.danex11.hulajwro.Screens.FahrenScreen;

import static pl.op.danex11.hulajwro.SpielFahre.PPM;

public class Follower extends Sprite {
    public World world;
    public Body b2body;
    private Rectangle rectFeet = new Rectangle();
    private TextureRegion spriteStand;

    //for Animation
    public enum StateF {STANDING_F, RUNNING_F}

    public StateF currentState;
    public StateF previousState;
    // private Animation Stand;
    private Animation spriteRun;
    private float stateTimer;
    private boolean runningRight;

    public Follower(World world, FahrenScreen screen) {
        //super(screen.getAtlas().findRegion("birdbrown"));
        super(screen.getAtlas().findRegion("bird2blue"));
        this.world = world;

        //in pixels region data
        String regionName = "bird2blue";
        int framesNo = 3;
        int widthOfFrame = getRegionWidth() / framesNo;
        int heightOfFrame = getRegionHeight();

        //todo
        //scale for pixelperfect resolution

        currentState = StateF.STANDING_F;
        previousState = StateF.STANDING_F;
        stateTimer = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < framesNo; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion(regionName), i * widthOfFrame, 0, widthOfFrame, heightOfFrame));

        spriteRun = new Animation(0.1f, frames);
        frames.clear();

        spriteStand = new TextureRegion(screen.getAtlas().findRegion(regionName), 0, 0, widthOfFrame, heightOfFrame);
        //size of sprite on the screen
//        setBounds(0, 0, 80 / PPM, 80 / PPM);
        int scaling = 2;
        setBounds(0, 0, scaling*widthOfFrame / PPM, scaling*heightOfFrame / PPM);

        setRegion(spriteStand);

    }


    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case RUNNING_F:
                region = (TextureRegion) spriteRun.getKeyFrame(stateTimer, true);
                break;
            case STANDING_F:
                // break;
            default:
                region = spriteStand;
                break;
        }
        //flip left or right for standing still
        /*
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
         */
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public StateF getState() {
        //if ((b2body.getLinearVelocity().x > 10 && b2body.getLinearVelocity().x <-10) || b2body.getLinearVelocity().y > 10)
        if (true) return StateF.RUNNING_F;
        else
            return StateF.STANDING_F;
    }



    public void defineFollower() {
    }

    public void update(float deltatime, float posX, float posY) {
        setPosition(posX, posY);
        setRegion(getFrame(deltatime));



    }

}
