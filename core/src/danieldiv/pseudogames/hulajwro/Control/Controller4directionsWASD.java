package danieldiv.pseudogames.hulajwro.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Controller4directionsWASD {

    int dirX, dirY;
    Body body;

    public void Controller4directionsWASD() {
        dirX = 0;
        dirY = 0;
        int speed = 30;
        int PPM = 100;

        if (Gdx.input.isKeyPressed(Input.Keys.S)) dirY = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dirY = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dirX = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dirX = 1;

        body.setLinearVelocity(dirX * speed, dirY * speed);
        body.applyLinearImpulse(new Vector2((dirX * speed) / PPM, (dirY * speed) / PPM), new Vector2(0, 0), true);

        //0.4f means 0.4frames
        /*
        if (Gdx.input.isKeyJustPressed(Input.Keys.W))
            plr.b2body.applyLinearImpulse(new Vector2(0, 4f), plr.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.D) && plr.b2body.getLinearVelocity().x <= 2)
            plr.b2body.applyLinearImpulse(new Vector2(0.4f, 0), plr.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.A) && plr.b2body.getLinearVelocity().x >= -2)
            plr.b2body.applyLinearImpulse(new Vector2(-0.4f, 0), plr.b2body.getWorldCenter(), true);
         */

    }
}
