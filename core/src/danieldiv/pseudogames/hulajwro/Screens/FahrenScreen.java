package danieldiv.pseudogames.hulajwro.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import danieldiv.pseudogames.hulajwro.Control.Controller8directions;
import danieldiv.pseudogames.hulajwro.Control.InputsHandling;
import danieldiv.pseudogames.hulajwro.SpielFahre;
import danieldiv.pseudogames.hulajwro.Scenes.Hud;
import danieldiv.pseudogames.hulajwro.Tools.B2WorldBuilder;
import danieldiv.pseudogames.hulajwro.sprites.PlrSprite;

public class FahrenScreen extends InputAdapter implements Screen {

    //reference to our game, used to set Screens
    private SpielFahre spiel;
    private TextureAtlas atlas;

    //basic playscreen variables
    private OrthographicCamera spielcam;
    private Viewport spielViewPort;
    Viewport mapViewPort;
    //Hud
    private Hud hud;
    //Map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //Box2D variables
    private Box2DDebugRenderer b2drenderer;
    World world;
    private float PPM = SpielFahre.PPM;

    private PlrSprite plr;

    private boolean goGoGo;
    float dragX,dragY;
    Vector3 touchScreenPosGdx = new Vector3(0, 0, 0);
    //https://github.com/libgdx/libgdx/wiki/Event-handling#inputmultiplexer
    InputAdapter inputHandling= new InputAdapter() {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            goGoGo = true;
            dragX = Gdx.input.getX();
            dragY = Gdx.graphics.getHeight() - Gdx.input.getY();
            touchScreenPosGdx = new Vector3(dragX, dragY, 0);
            Gdx.app.log("tagGdxT", "touchDown_ScreenPosGdx " + dragX + " " + dragY);
            Gdx.app.log("tagGdxT", "touchDown_WorldPos " + touchScreenPosGdx);
            return false;
        }
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            goGoGo = true;
            dragX = Gdx.input.getX();
            dragY = Gdx.graphics.getHeight() - Gdx.input.getY();
            touchScreenPosGdx = new Vector3(dragX, dragY, 0);
            Gdx.app.log("tagGdxT", "touchDrag_ScreenPosGdx " + dragX + " " + dragY);
            Gdx.app.log("tagGdxT", "touchDrag_WorldPos " + touchScreenPosGdx);
            return false;
        }
        @Override
        public boolean touchUp(int x, int y, int pointer, int button) {
            // your touch up code here
            goGoGo = false;
            return true; // return true to indicate the event was handled
        }
    };


    public FahrenScreen(SpielFahre spiel) {
        this.spiel = spiel;
        atlas = new TextureAtlas("hulajCharacters.pack");


        Gdx.input.setInputProcessor(inputHandling);

        spielcam = new OrthographicCamera();
        //scale View to height, than add black bars at sides to meintain aspect ratio
        spielViewPort = new FitViewport(SpielFahre.VIRTUAL_WIDTH / PPM, SpielFahre.VIRTUAL_HEIGHT / PPM, spielcam);
        //scale View to height, than add black bars at sides to maintain aspect ratio
        mapViewPort = new FitViewport(SpielFahre.VIRTUAL_WIDTH / PPM, SpielFahre.VIRTUAL_HEIGHT / PPM, spielcam);
        // mapViewPort = new ScreenViewport(gamecam);
        //this game.batch gets variable batch from GamePlay
        hud = new Hud(spiel.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tilemaps/csvprawydol.tmx");
        //this centers around zero,zero
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);


        // mapRenderer.setView(spielViewPort);
        //move zerozero to left down corner
        spielcam.position.set(spielViewPort.getWorldWidth() / 2, spielViewPort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, 0), true);
        b2drenderer = new Box2DDebugRenderer();

        new B2WorldBuilder(world, map);
        plr = new PlrSprite(world, this);
    }


    public TextureAtlas getAtlas() {
        return atlas;
    }


    @Override
    public void show() {
    }


    public void handleInput(float deltatime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W))
            plr.b2body.applyLinearImpulse(new Vector2(0, 4f), plr.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.D) && plr.b2body.getLinearVelocity().x <= 2)
            plr.b2body.applyLinearImpulse(new Vector2(0.4f, 0), plr.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.A) && plr.b2body.getLinearVelocity().x >= -2)
            plr.b2body.applyLinearImpulse(new Vector2(-0.4f, 0), plr.b2body.getWorldCenter(), true);


        //Controller8directions();
        if (goGoGo) {
        int hpx = Gdx.graphics.getHeight();
        Vector2 plrBodyScreenPosV2 = new Vector2(plr.b2body.getPosition().x, plr.b2body.getPosition().y);


       // Gdx.app.log("tagGdx", "FahrenScreen_touchScreenPosGdx " + touchScreenPosGdx);
        Vector2 moveVect = Controller8directions.moveVector(hpx, spielcam, touchScreenPosGdx, plrBodyScreenPosV2, plr);
         Vector2 moveVectScaled = new Vector2(moveVect.x/PPM, moveVect.y/PPM);
        plr.b2body.applyLinearImpulse(moveVectScaled, plr.b2body.getWorldCenter(), true);
        }
        //else if (!goGoGo) {
            /*
            //todo if velocity !=0 apply counterforce
            Vector2 velocityPlr = new Vector2(plrBody.getLinearVelocity());
            if (velocityPlr.x > 0) {
                movementVector = new Vector2(-movementVector.x, -movementVector.y);
            } else {
                movementVector = new Vector2(0, 0);
            }
            this.plrBody.applyLinearImpulse(movementVector, plrBodyScreenPosV2, true);
            //movementVector = new Vector2(-movementVector.x / 2, -movementVector.y / 2);
        }
             */
        // }


    }

    public void update(float deltatime) {
        handleInput(deltatime);

        world.step(1 / 60f, 6, 2);

        //player
        plr.updatee(deltatime);

        //cam tracking
        spielcam.position.x = plr.b2body.getPosition().x;

        spielcam.update();


    }


    @Override
    public void render(float delta) {
        //this @delta is than casted into @deltatime
        //delta – The time in seconds since the last render.
        update(delta);

        //background -- clearing is neccessary
        Gdx.gl.glClearColor((float) 0.2, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(spielcam);
        //mapRenderer.render();
        //b2d render
        b2drenderer.render(world, spielcam.combined);

        //BATCH     BATCH   BATCH   BATCH
        //to only render what is visible
        spiel.batch.setProjectionMatrix(spielcam.combined);
        spiel.batch.begin();
        plr.draw(spiel.batch);
        spiel.batch.end();

        spiel.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        spielViewPort.update(width, height);
        mapViewPort.update(width, height, false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        b2drenderer.dispose();
        world.dispose();
        hud.dispose();

    }


}
