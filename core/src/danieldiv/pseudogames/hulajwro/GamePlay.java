package danieldiv.pseudogames.hulajwro;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import danieldiv.pseudogames.hulajwro.Screens.PlayScreen;

public class GamePlay extends com.badlogic.gdx.Game {
    public static final int VIRTUAL_WIDTH = 1280 ;
    public static final int VIRTUAL_HEIGHT = 720;
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
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
