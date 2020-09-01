package danieldiv.pseudogames.hulajwro.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import danieldiv.pseudogames.hulajwro.GamePlay;
import danieldiv.pseudogames.hulajwro.Screens.PlayScreen;

//New camera and new viewport to keep Hud locked at given position on the screen
public class Hud {
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

    public Hud(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        //fits to height, adds black bars to sides
        viewport = new FitViewport(GamePlay.VIRTUAL_WIDTH, GamePlay.VIRTUAL_HEIGHT, new OrthographicCamera());
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


        stage.addActor(table);
    }
}
