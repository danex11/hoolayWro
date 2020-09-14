package danieldiv.pseudogames.hulajwro.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import danieldiv.pseudogames.hulajwro.Screens.FahrenScreen;
import danieldiv.pseudogames.hulajwro.SpielFahre;

//New camera and new viewport to keep Hud locked at given position on the screen
public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label spriteLabel;


    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

    public Hud(SpriteBatch sb, FahrenScreen screen) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        //fits to height, adds black bars to sides
        viewport = new FitViewport(SpielFahre.VIRTUAL_WIDTH, SpielFahre.VIRTUAL_HEIGHT, new OrthographicCamera());
        //stage is like a empty box waiting for Table to lay out Labels
        stage = new Stage(viewport, sb);

        Table table = new Table();
        //top position of stage
        table.top();
        //to make it the size of the stage
        table.setFillParent(true);

        // BitmapFont bitmapfont= new BitmapFont(new BitmapFont());
        //%03d - 3 digits long
        //%06d - 6 digits long
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("level1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("World level", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        spriteLabel = new Label("Game score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //expand for entire top row - for multimpe inside one row distribute them equally
        table.add(spriteLabel).expandX().padTop(1);
        table.add(worldLabel).expandX().padTop(1);
        table.add(timeLabel).expandX().padTop(1);
        //go to next row
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();


        //RESET Button  RESET Button    RESET Button    RESET Button    RESET Button

        Texture texture = new Texture("resetbutton64.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton resetButton = new ImageButton(drawable);


        stage.addActor(resetButton);



        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
