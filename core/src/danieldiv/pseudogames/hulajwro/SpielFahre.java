package danieldiv.pseudogames.hulajwro;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import danieldiv.pseudogames.hulajwro.Screens.FahrenScreen;

public class SpielFahre extends com.badlogic.gdx.Game {
    public static final int VIRTUAL_WIDTH = 1280 ;
    public static final int VIRTUAL_HEIGHT = 720;
    public static final float PPM = 64;
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new FahrenScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
