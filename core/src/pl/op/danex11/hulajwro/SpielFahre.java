package pl.op.danex11.hulajwro;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pl.op.danex11.hulajwro.Screens.FahrenScreen;

public class SpielFahre extends com.badlogic.gdx.Game {
    public static final int VIRTUAL_WIDTH = 1280;
    public static final int VIRTUAL_HEIGHT = 720;
    public static final float PPM = 64;
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        //for when reset button reload that whole screen
        //read GameOver mario tutorial
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
