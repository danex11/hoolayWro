package pl.op.danex11.hulajwro.Control;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

public class InputsHandling extends ApplicationAdapter implements InputProcessor {

    boolean goGoGo;
    float dragX, dragY;
    public Vector3 touchScreenPosGdx = new Vector3(0,0,0);

/*
    public InputsHandling() {
        //-----------------Controls
        //this.goGoGo = goGoGo;
        // this.touchScreenPosGdx = touchScreenPosGdx;
        Gdx.app.log("tagGdx", "InputHandling_touchScreenPosGdx " + touchScreenPosGdx);
        Gdx.input.setInputProcessor(this);
    }


 */








    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        goGoGo = false;
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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
