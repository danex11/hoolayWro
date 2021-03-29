package pl.op.danex11.hulajwro.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.op.danex11.hulajwro.sprites.Follower;
import pl.op.danex11.hulajwro.Control.Controller8directionsConstVect;
import pl.op.danex11.hulajwro.Scenes.Hud;
import pl.op.danex11.hulajwro.SpielFahre;
import pl.op.danex11.hulajwro.Tools.B2WorldBuilder;
import pl.op.danex11.hulajwro.Tools.FixBleedingTiles;
import pl.op.danex11.hulajwro.Tools.FixedList;
import pl.op.danex11.hulajwro.sprites.PlrSprite;


//TODO remove spring counterforce for touching close to sprite - this is how it comes to be:
//app is catching touchPos > sprite is moving to this touchPos > touchPos is not changing, sprite oscillates around it with impulses

//TODO MInimizze  deadzone  for not moving on y axis

//todo bugfix flickering on turns on obsticle on low velocity
//todo bugfix Sprite position on first touch down is wrong

//todo

/**
 * is using methods of InputAdapter which allows to only use desired methods of gdx.InputProcessor
 *
 * @author Daniel Szablewski
 * @version 0.3
 * @since 2020-10-
 */
public class FahrenScreen extends InputAdapter implements Screen {

    //reference to our game, used to set Screens
    private SpielFahre spiel;
    private TextureAtlas atlas;

    //Prefs
    Preferences prefs = Gdx.app.getPreferences("game preferences");


    //basic playscreen variables
    private OrthographicCamera spielcam;
    private Viewport spielViewPort;
    Viewport mapViewPort;
    //Hud
    private Hud hud;
    //Map
    private TmxMapLoader mapLoader;
    private TiledMap map, mapFixed;
    private OrthogonalTiledMapRenderer mapRenderer;
    private MapLayers mapLayers;
    private TiledMapTileLayer overlayLayer;


    //Box2D variables
    private Box2DDebugRenderer b2drenderer;
    World world;
    private float PPM = SpielFahre.PPM;

    private PlrSprite plr;
    private Follower follower;

    private boolean goGoGo = false;
    private boolean swipeGoGo = false;
    float dragX, dragY;
    float touchX, touchY;
    Vector3 touchScreenPosGdx = new Vector3(0, 0, 0);

    //tail
    private FixedList<Vector2> inputPoints;
    private Vector2 lastPoint = new Vector2();


    //https://github.com/libgdx/libgdx/wiki/Event-handling#inputmultiplexer
    InputAdapter inputHandling = new InputAdapter() {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            swipeGoGo = true;

            return false;
        }

        /**
         *
         * @param screenX
         * @param screenY
         * @param pointer
         * @return
         */
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            swipeGoGo = false;
            goGoGo = true;
            dragX = Gdx.input.getX();
            dragY = Gdx.input.getY();
            touchScreenPosGdx = new Vector3(dragX, dragY, 0);
            //  Gdx.app.log("tagGdxT", "touchDrag_ScreenPosGdx " + dragX + " " + dragY);
            spielcam.unproject(touchScreenPosGdx);
            //  Gdx.app.log("tagGdxT", "touchDrag_ScreenPosGdx " + dragX + " " + dragY);
            //  Gdx.app.log("tagGdxT", "touchDrag_WorldPos " + touchScreenPosGdx);

            /*
            //tail
            Vector2 v = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);

            //calc length
            float dx = v.x - lastPoint.x;
            float dy = v.y - lastPoint.y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);

            //add new point
            inputPoints.insert(v);

            lastPoint = v;
             */
            return false;
        }


        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            //swipeGoGo = true;
            goGoGo = false;
            touchX = Gdx.input.getX();
            // this dragY comes from screen height in pixels and makes no sense for desktom height is 720, for mobile 1020 or another
            // work on world coords exclusively
            //unproject to world coords before any calculations
            touchY = Gdx.input.getY();
            //makes touchpos independant of screen density or resolution
            touchScreenPosGdx = new Vector3(touchX, touchY, 0);
            //  Gdx.app.log("tagGdxT", "TTouchDown_ScreenPosGdx " + touchX + " " + touchY);
            spielcam.unproject(touchScreenPosGdx);
            //  Gdx.app.log("tagGdxT", "TTouchDown_WorldPos " + touchScreenPosGdx);


            //tail
            //clear points
            //inputPoints.clear();
            //starting point

            //todo?
            lastPoint = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
            inputPoints.insert(lastPoint);


            // your touch up code here
            goGoGo = false;
            return true; // return true to indicate the event was handled
        }
    };


    public Stage stageButt;
    public ImageButton resetButton;
    boolean isResetPressed = false;
    public Viewport viewportButt;

    public boolean isResetPressed() {
        return isResetPressed;
    }

    int maxInputPoints = 100;
    Music music_background;//= Gdx.audio.newMusic(Gdx.files.internal("S31-CityonSpeed.ogg"));;

    /**
     * @param spiel
     */
    public FahrenScreen(SpielFahre spiel) {

        //music
        //Sound music_background = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        music_background = Gdx.audio.newMusic(Gdx.files.internal("S31-CityonSpeed.ogg"));
        music_background.setLooping(true);
        music_background.setVolume(0.6f);
        music_background.play();
        //music_background.dispose();

        //tail
        this.inputPoints = new FixedList<Vector2>(maxInputPoints, Vector2.class);


        this.spiel = spiel;
        atlas = new TextureAtlas("hulajCharacters.atlas");


        spielcam = new OrthographicCamera();
        //scale View to height, than add black bars at sides to meintain aspect ratio
        spielViewPort = new FitViewport(SpielFahre.VIRTUAL_WIDTH / PPM, SpielFahre.VIRTUAL_HEIGHT / PPM, spielcam);
        //scale View to height, than add black bars at sides to maintain aspect ratio
        mapViewPort = new FitViewport(SpielFahre.VIRTUAL_WIDTH / PPM, SpielFahre.VIRTUAL_HEIGHT / PPM, spielcam);
        // mapViewPort = new ScreenViewport(gamecam);
        //this game.batch gets variable batch from GamePlay
        hud = new Hud(spiel.batch, this, spiel);


        //Prefs
        Float object = new Float((prefs.getFloat("highscore")));
        if (object != null) {
            hud.setRecordTime(prefs.getFloat("highscore"));
        }
        //for Debugging
        //prefs.putFloat("highscore", 999);
        //hud.setRecordTime(999);


        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tilemaps/1500ofsize16.tmx");

        //map texture bleeding fix
        FixBleedingTiles fixedmap = new FixBleedingTiles(map);
        mapFixed = new FixBleedingTiles(map).fixTilesPixelBleeding(map);

        //this centers around zero,zero
        //mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        mapRenderer = new OrthogonalTiledMapRenderer(mapFixed, 4 / PPM);
        // Reading map layers
        mapLayers = map.getLayers();

        //map overlay layer
        overlayLayer = (TiledMapTileLayer) mapLayers.get("overlays");

        // mapRenderer.setView(spielViewPort);
        //move zerozero to left down corner
        spielcam.position.set((spielViewPort.getWorldWidth() / 2), spielViewPort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, 0), true);
        b2drenderer = new Box2DDebugRenderer();

        new B2WorldBuilder(world, map);

        //SPRITES   SPRITES SPRITES
        plr = new PlrSprite(world, this);
        follower = new Follower(world, this);

        //**********
        //RESET Button  RESET Button    RESET Button    RESET Button    RESET Button
        Table tableButtons = new Table();
        //top position of stage
        tableButtons.left().bottom();
        //to make it the size of the stage
        tableButtons.setFillParent(true);

        Texture texture = new Texture("resetbutton64.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        resetButton = new ImageButton(drawable);
        resetButton.setSize(128, 128);
        tableButtons.add(resetButton).size(resetButton.getWidth(), resetButton.getHeight());
        viewportButt = new FitViewport(SpielFahre.VIRTUAL_WIDTH, SpielFahre.VIRTUAL_HEIGHT, new OrthographicCamera());
        stageButt = new Stage(viewportButt, spiel.batch);
        stageButt.addActor(tableButtons);

        // Gdx.input.setInputProcessor(inputHandling);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inputHandling);
        multiplexer.addProcessor(stageButt);
        Gdx.input.setInputProcessor(multiplexer);


    }


    public TextureAtlas getAtlas() {
        return atlas;
    }

    public static void fixBleeding(TextureRegion[][] region) {
        for (TextureRegion[] array : region) {
            for (TextureRegion texture : array) {
                fixBleeding(texture);
            }
        }
    }

    public static void fixBleeding(TextureRegion region) {
        float fix = 0.01f;

        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight); // Trims
        // region
    }


    @Override
    public void show() {
    }


    int dirX, dirY;
    int speed, damping;


    public void handleInputAfterFinish(float deltatime) {
        resetButton.addListener(new ClickListener() {
            // Gdx.app.log("TagGdx", "reset up");

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("TagGdx", "reset Down afterfinish");
                super.touchDown(event, x, y, pointer, button);
                //reset timer
                finished = false;
                // isResetPressed = true;
                //Prefs - highscore load
                hud.setRecordTime(prefs.getFloat("highscore"));
                isResetPressed = true;
                return true;
            }
        });
    }

    public void handleInput(float deltatime) {
        //zoom
        if (Gdx.input.isKeyPressed(Input.Keys.X) && spielcam.zoom > 0)
            spielcam.zoom -= 0.01f;
        if (Gdx.input.isKeyPressed(Input.Keys.Z))
            spielcam.zoom += 0.01f;


        //RESET *****
        resetButton.addListener(new ClickListener() {
            // Gdx.app.log("TagGdx", "reset up");
/*
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ////////////////super.touchUp(event, x, y, pointer, button);
                //reset timer
                finished = false;
                //return super.touchDown(event, x, y, pointer, button);
                Gdx.app.log("TagGdx", "reset up");
               // isResetPressed = true;
                //Prefs - highscore load
                hud.setRecordTime(prefs.getFloat("highscore"));
                //////return true;
                isResetPressed = true;
            }

         */
/*
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //reset timer
                finished = false;
                //return super.touchDown(event, x, y, pointer, button);
                Gdx.app.log("TagGdx", "reset up");
                isResetPressed = true;
                //Prefs - highscore load
                hud.setRecordTime(prefs.getFloat("highscore"));
                //return true;
            }


 */

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // super.touchUp(event, x, y, pointer, button);
                isResetPressed = true;
                Gdx.app.log("TagGdx", "reset Down");
                return true;
            }


        });
        if (isResetPressed()) {
            Gdx.app.log("TagGdx", "reset SpFh");
            music_background.stop();
            music_background.dispose();
            spiel.setScreen(new FahrenScreen((SpielFahre) spiel));


        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            spiel.setScreen(new FahrenScreen((SpielFahre) spiel));
            //stage.draw;
        }


        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            plr.b2body.setLinearVelocity(new Vector2(0, 0));

        dirX = 0;
        dirY = 0;
        //speed = 15;
        damping = 0;
        float damping_thresh = (float) 0.001;
        float damping_thresh_minus = -damping_thresh;
        float linVelX = plr.b2body.getLinearVelocity().x;
        float linVelY = plr.b2body.getLinearVelocity().y;


        float moveVectX = 0;
        float moveVectY = 0;
        Vector2 moveVectScaled = new Vector2(0, 0);
        //Gdx.app.log("tagGdx", "linearVelX " + linVelX);
        // Gdx.app.log("tagGdx", "linearVelY " + linVelY);

        //na sztywno zatrzymaj w y
        //if touchpos == pos.y {linvely =0
        float posY;
        posY = plr.b2body.getPosition().y;
        if (goGoGo || swipeGoGo) {
            int hpx = Gdx.graphics.getHeight();
            Vector2 plrBodyScreenPosV2 = new Vector2(plr.b2body.getPosition().x, plr.b2body.getPosition().y);

            // feed world POS to moveVect
            //spielcam.unproject(touchScreenPosGdx);
            Vector3 plrBodyScreenPosV3 = new Vector3(plrBodyScreenPosV2.x, plrBodyScreenPosV2.y, 0);
            spielcam.unproject(plrBodyScreenPosV3);

            Vector2 moveVect = Controller8directionsConstVect.moveVector(hpx, spielcam, touchScreenPosGdx, plrBodyScreenPosV3, plr);
            //Gdx.app.log("tagGdx", "moveVect " + moveVect);
            //limiting max x and y velocity
            if (linVelX > 20) moveVectX = 0;
            else moveVectX = moveVect.x;
            if (linVelY > 10) {
                moveVectY = 0;
                plr.b2body.setLinearVelocity(plr.b2body.getLinearVelocity().x, 11);
                // plr.b2body.applyLinearImpulse(new Vector2(0, -moveVectY), plr.b2body.getWorldCenter(), true);
            } else if (linVelY < -10) {
                moveVectY = 0;
                plr.b2body.setLinearVelocity(plr.b2body.getLinearVelocity().x, -11);
            } else moveVectY = moveVect.y;
            //zeroing y velocity
            // Vector2 pos = new Vector2(plr.b2body.getPosition());
            //posY = plr.b2body.getPosition().y;
            Gdx.app.log("tagGdx", "posY " + posY);
            //float dragYnew = dragY / PPM;
            Gdx.app.log("tagGdx", "touchScreenUnprojWorld.y " + (touchScreenPosGdx.y));
            //todo this should not be dependant of touching ofr not the screen
            //or maybe we want to allow the plr to make sprite float after  touchUp??
            //zeroing y velocity cont.
            //  Y   ZEROING
            if (posY > (touchScreenPosGdx.y) - 0.2 && posY < (touchScreenPosGdx.y) + 0.2) {
                Gdx.app.log("tagGdx", "inYzeroVelRange " + posY + " " + (touchScreenPosGdx.y));
                plr.b2body.setLinearVelocity(plr.b2body.getLinearVelocity().x, 0);
                moveVectY = 0;
            }
            ////  Y   ZEROING END
            //zeroing x velocity for moving straight up
            if (posY > (touchScreenPosGdx.y) - 0.2 && posY < (touchScreenPosGdx.y) + 0.2) {
                Gdx.app.log("touchScreenPosGdx.y", "touchScreenPosGdx.y " + posY + " " + (touchScreenPosGdx.y));
                plr.b2body.setLinearVelocity(plr.b2body.getLinearVelocity().x, 0);
                moveVectY = 0;
            }

            //applying impulses
            // fix vector values - it takes them from 0,0 origin and is slower in some directions
            moveVectScaled = new Vector2(moveVectX / (PPM * 100), moveVectY / (PPM * 5));
            Gdx.app.log("tagGdx", "moveVectScaled " + moveVectScaled);
            plr.b2body.applyLinearImpulse(moveVectScaled, plr.b2body.getWorldCenter(), true);
        } else if (!goGoGo) {
            if (linVelX > damping_thresh) {
                plr.b2body.applyLinearImpulse(new Vector2(-damping / PPM, 0), new Vector2(0, 0), true);
            }
            if (linVelX < damping_thresh_minus) {
                plr.b2body.applyLinearImpulse(new Vector2(damping / PPM, 0), new Vector2(0, 0), true);
            }
            if (linVelY > damping_thresh) {
                plr.b2body.applyLinearImpulse(new Vector2(0, -damping / PPM), new Vector2(0, 0), true);
            }
            if (linVelY < damping_thresh_minus) {
                plr.b2body.applyLinearImpulse(new Vector2(0, damping / PPM), new Vector2(0, 0), true);
            }

            //stop movement if speed is very low
            if ((linVelX < damping_thresh * 10) && (linVelX > -damping_thresh * 10) && (linVelY < damping_thresh * 10) && (linVelY > -damping_thresh * 10))
                plr.b2body.setLinearVelocity(new Vector2(0, 0));
            //if ((linVelY < damping_thresh * 10) && (linVelY > -damping_thresh * 10))
            //    plr.b2body.setLinearVelocity(new Vector2(0, 0));

        }
        //  Y   ZEROING
        if (posY > (touchScreenPosGdx.y) - 0.2 && posY < (touchScreenPosGdx.y) + 0.2) {
            Gdx.app.log("tagGdx", "inYzeroVelRange " + posY + " " + (touchScreenPosGdx.y));
            plr.b2body.setLinearVelocity(plr.b2body.getLinearVelocity().x, 0);
            moveVectY = 0;
        }

    }


    boolean birdo = false;
    Vector2 followerPosTemp = new Vector2(0, -2);
    Vector2 followerPosStarting = new Vector2(0, -2);
    int isin;
    boolean birdoactive = false;

    long startTime = 0;
    long startTimeN = 0;


    public void update(float deltatime) {


        //tail Array
        //Gdx.app.log("Tail", "inputPoints " + inputPoints);
        Gdx.app.log("TagGdxfinished ", "" + finished);


        if (!finished) handleInput(deltatime);
        if (finished) handleInputAfterFinish(deltatime);

        world.step(1 / 60f, 6, 2);

        //player
        plr.updatee(deltatime);
        //////////Gdx.app.log("Plrpos", "Plrpos " + plr.getX() + " " + plr.getY());
        ///////////Gdx.app.log("MathSin", "MathSin " + 10 * MathUtils.sinDeg(isin));

        //follower
        //tail
        //tree region
        if (plr.getX() > 32 && plr.getX() < 33 && plr.getY() > 6 && plr.getY() < 8) {
            birdo = true;
            startTime = TimeUtils.millis();
            // startTimeN = TimeUtils.nanoTime();


        }
        if (birdo) {
            birdoactive = true;
            Vector2 v = new Vector2(plr.getX(), plr.getY());
            //v interpolate
            v.x = (v.x - (plr.b2body.getPosition().x - v.x) * .1f * MathUtils.sinDeg(isin) * 4);
            v.y = (v.y - (plr.b2body.getPosition().y - v.y) * .1f * MathUtils.sinDeg(isin) * 20);
            //calc length
            float dx = v.x - lastPoint.x;
            float dy = v.y - lastPoint.y;
            //add new point
            inputPoints.insert(v);
            lastPoint = v;

            if (TimeUtils.timeSinceMillis(startTime) > 10000) {
                Gdx.app.log("(TimeUtils", "(TimeUtils.timeSince(startTime) " + (TimeUtils.timeSinceMillis(startTime)));
                followerPosTemp.y = followerPosTemp.y + (TimeUtils.timeSinceMillis(startTime) * .000005f);
                Vector2 temp = new Vector2(v.x, followerPosTemp.y);
                inputPoints.insert(temp);
                birdoactive = false;
                //birdo = false
            }

            //update by plr pos
            if (inputPoints.size > 4 && birdoactive == true) {
                followerPosTemp = inputPoints.get(4);
            }
            //if time has passed than just update x and let y fly up
            else if (inputPoints.size > 4 && birdoactive == false) {
                followerPosTemp.x = inputPoints.get(4).x;
            }

            if (isin < 361) {
                isin++;
            } else {
                isin = 0;
            }
            //interpolate
            // Gdx.app.log("followerPos.x", "followerPos.x " + followerPos.x + " ");
            followerPosStarting.x = (followerPosStarting.x - (followerPosTemp.x - followerPosStarting.x) * .1f * MathUtils.sinDeg(isin) * 4);
            followerPosStarting.y = (followerPosStarting.y - (followerPosTemp.y - followerPosStarting.y) * .1f * MathUtils.sin(isin) * 20);
            //Gdx.app.log("followerPos.x'", "followerPos.x " + followerPos.x + " ");

            ;
            follower.update(deltatime, (float) (followerPosTemp.x - 0.8), (float) (followerPosTemp.y + 0.5));


        } else {
            follower.update(deltatime, followerPosStarting.x, followerPosStarting.y);
        }
        //end birdo

        //cam tracking
        //also start position for plr on screen

        //TODO interpolate camera position
        // spielcam.position.x =plr.b2body.getPosition().x +spielViewPort.getWorldWidth()/5;
        //in every stem we are adding only .1 of distance between a camera and a plr.pos - the following is slower than normally
        //(plr.b2body.getPosition().x - spielcam.position.x-spielViewPort.getWorldWidth()/5) - difference cam to plr
        spielcam.position.x = (spielcam.position.x + (plr.b2body.getPosition().x - spielcam.position.x + spielViewPort.getWorldWidth() / 3) * .2f);
        spielcam.update();
        if (!finished) hud.update(deltatime);

        isFinished(finishpoint);
        if (finished) hud.setFinishedForHud(true);
    }

    int finishpoint = 1467;

    float thisscore = 0;
    boolean finished;

    public void isFinished(int finishpoint) {
        //stop after finishline
        if (plr.getX() > finishpoint + 10) plr.b2body.setLinearVelocity(0, 0);
        if (plr.getX() > finishpoint && plr.getX() < finishpoint + 1) {
            finished = true;
            Gdx.app.log("APPlog", "plr.getX() " + plr.getX());
            thisscore = hud.getRunTimer();
            Gdx.app.log("APPlog", "thisscore " + thisscore);
            Gdx.app.log("APPlog", "hud.getRecordTime() " + hud.getRecordTime());
            Gdx.app.log("APPlog", "prefs.getFloat() " + (new Float(prefs.getFloat("highscore"))));
            //set new highscore
            if (thisscore < prefs.getFloat("highscore") || (new Float(prefs.getFloat("highscore"))) == 0.0) {
                hud.setRecordTime(thisscore);
                hud.setNewRecordTime(thisscore);
                //Prefs highscore save
                prefs.putFloat("highscore", thisscore);
                prefs.flush();
            }
        }
    }

    public enum State {
        Running, Paused
    }

    //set to run
    State state = State.Running;


    @Override
    public void render(float delta) {
        //this @delta is than casted into @deltatime
        //delta – The time in seconds since the last render.
        switch (state) {
            case Running:
                update(delta);
                break;
            case Paused:
                break;
        }

        //background -- clearing is neccessary
        Gdx.gl.glClearColor((float) 0.2, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // for map texture bleeding
        //Set texture filtering of TiledMap
        mapRenderer.setView(spielcam);
        mapRenderer.render();
        //b2d render
        ////////////////B2Drend b2drenderer.render(world, spielcam.combined);

        //BATCH     BATCH   BATCH   BATCH
        //to only render what is visible
        spiel.batch.setProjectionMatrix(spielcam.combined);
        spiel.batch.begin();
        plr.draw(spiel.batch);
        follower.draw(spiel.batch);
        spiel.batch.end();

        //render overlays
        mapRenderer.getBatch().begin();
        mapRenderer.renderTileLayer(overlayLayer);
        mapRenderer.getBatch().end();


        spiel.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        stageButt.draw();
    }

    @Override
    public void resize(int width, int height) {
        spielViewPort.update(width, height);
        mapViewPort.update(width, height, false);
    }

    @Override
    public void pause() {
        this.state = State.Paused;
    }

    @Override
    public void resume() {
        this.state = State.Running;
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
