package danieldiv.pseudogames.hulajwro.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import danieldiv.pseudogames.hulajwro.Screens.FahrenScreen;

import static danieldiv.pseudogames.hulajwro.SpielFahre.PPM;

public class Follower extends Sprite {
    public World world;
    public Body b2body;
    private Rectangle rectFeet = new Rectangle();
    private TextureRegion standstill;

    public Follower(World world, FahrenScreen screen) {
        //super(screen.getAtlas().findRegion("birdbrown"));
        this.world = world;

        standstill = new TextureRegion(screen.getAtlas().findRegion("birdbrown"));
        //size of sprite on the screen
        setBounds(0, 0, 80 / PPM, 80 / PPM);
        setRegion(standstill);

    }

    public void defineFollower() {
    }

    public void update(float deltatime, float posX, float posY) {
        setPosition(posX,posY);

    }

}
