package danieldiv.pseudogames.hulajwro.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import danieldiv.pseudogames.hulajwro.GamePlay;
import danieldiv.pseudogames.hulajwro.Scenes.Hud;

public class PlayScreen implements Screen {

    //game sets
    private GamePlay game;
    private OrthographicCamera gamecam;
    private Viewport gameViewPort;
    Viewport mapViewPort;
    //Hud
    private Hud hud;
    //Map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Box2DDebugRenderer b2drenderer;
    World world;

    public PlayScreen(GamePlay game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        //scale View to height, than add black bars at sides to meintain aspect ratio
        gameViewPort = new FitViewport(GamePlay.VIRTUAL_WIDTH, GamePlay.VIRTUAL_HEIGHT, gamecam);
        //scale View to height, than add black bars at sides to meintain aspect ratio
        mapViewPort = new FitViewport(GamePlay.VIRTUAL_WIDTH, GamePlay.VIRTUAL_HEIGHT, gamecam);
       // mapViewPort = new ScreenViewport(gamecam);
        //this game.batch gets variable batch from GamePlay
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tilemaps/csvprawydol.tmx");
        //this centers around zero,zero
        mapRenderer = new OrthogonalTiledMapRenderer(map);


        // mapRenderer.setView(gameViewPort);
        //move zerozero to left down corner
        gamecam.position.set(gameViewPort.getWorldWidth() / 2, gameViewPort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, 0), true);
        b2drenderer = new Box2DDebugRenderer();

        //body
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //TileObjectLayer 1-solids
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //TileObjectLayer 2-evilground
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


    }

    @Override
    public void show() {
    }

    public void handleInput(float deltatime) {
        if (Gdx.input.isTouched()) gamecam.position.x += 100 * deltatime;
    }

    public void update(float deltatime) {
        handleInput(deltatime);

        gamecam.update();


/*
        if (goGoGo) {
            plrBodyScreenPosV2 = new Vector2(plrBody.getPosition().x, plrBody.getPosition().y);
            ///deb movementVector = new Vector2(controller8directions.moveVector(hpx, camera, touchScreenPosGdx, plrBodyScreenPosV2, plrSprite));
            ///deb Vector2 movementVectorScaled = new Vector2(movementVector.x, movementVector.y);
            ///deb  this.plrBody.applyLinearImpulse(movementVectorScaled, plrBodyScreenPosV2, true);

        } else if (!goGoGo) {

            //todo if velocity !=0 apply counterforce
            Vector2 velocityPlr = new Vector2(plrBody.getLinearVelocity());
            if (velocityPlr.x > 0) {
                movementVector = new Vector2(-movementVector.x, -movementVector.y);
            } else {
                movementVector = new Vector2(0, 0);
            }
            this.plrBody.applyLinearImpulse(movementVector, plrBodyScreenPosV2, true);
            //movementVector = new Vector2(-movementVector.x / 2, -movementVector.y / 2);

             */

    }


    @Override
    public void render(float delta) {
        update(delta);

        //background -- clearing is neccessary
        Gdx.gl.glClearColor((float) 0.2, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.setView(gamecam);
        mapRenderer.render();
        //b2d render
        b2drenderer.render(world, gamecam.combined);

        //to only render what is visible
        game.batch.setProjectionMatrix(gamecam.combined);
        //
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewPort.update(width, height);
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
    }
}
