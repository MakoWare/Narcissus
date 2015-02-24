package com.makoware.narcissus.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.makoware.narcissus.Assets;
import com.makoware.narcissus.NarcissusGame;
import com.makoware.narcissus.Universe;
import com.makoware.narcissus.systems.AnimationSystem;
import com.makoware.narcissus.systems.NarcissusSystem;
import com.makoware.narcissus.systems.CameraSystem;
import com.makoware.narcissus.systems.MovementSystem;
import com.makoware.narcissus.systems.RenderingSystem;
import com.makoware.narcissus.systems.StateSystem;

public class TestScreen extends ScreenAdapter{
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;

    NarcissusGame game;
    private Box2DDebugRenderer mDebugRender;
    OrthographicCamera guiCam;
    Vector3 touchPoint;
    Universe universe;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    int lastScore;
    String scoreString;

    Engine engine;

    private int state;

    public TestScreen (NarcissusGame game) {
        this.game = game;
        mDebugRender = new Box2DDebugRenderer();
        state = GAME_READY;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        touchPoint = new Vector3();
        engine = new Engine();

        universe = new Universe(engine);

        engine.addSystem(new NarcissusSystem(universe));
        engine.addSystem(new CameraSystem());

        engine.addSystem(new MovementSystem());
        engine.addSystem(new StateSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new RenderingSystem(game.batcher));



        universe.create();

        pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);
        resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
        quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
        lastScore = 0;
        scoreString = "SCORE: 0";

        pauseSystems();
    }

    public void update (float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        engine.update(deltaTime);

        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
            case GAME_LEVEL_END:
                updateLevelEnd();
                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void updateReady () {
        if (Gdx.input.justTouched()) {
            state = GAME_RUNNING;
            resumeSystems();
        }
    }

    private void updateRunning (float deltaTime) {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_PAUSED;
                pauseSystems();
                return;
            }
        }

        ApplicationType appType = Gdx.app.getType();

        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        float accelX = 0.0f;

        if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
            accelX = Gdx.input.getAccelerometerX();
        } else {
            if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) accelX = 5f;
            if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) accelX = -5f;
        }

        engine.getSystem(NarcissusSystem.class).setAccelX(accelX);
    }

    private void updatePaused () {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                resumeSystems();
                return;
            }

            if (quitBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                //game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }

    private void updateLevelEnd () {
        if (Gdx.input.justTouched()) {
            engine.removeAllEntities();
            universe = new Universe(engine);
            state = GAME_READY;
        }
    }

    private void updateGameOver () {
        if (Gdx.input.justTouched()) {
            //game.setScreen(new MainMenuScreen(game));
        }
    }

    public void drawUI () {
        guiCam.update();
        game.batcher.setProjectionMatrix(guiCam.combined);
        game.batcher.begin();
        switch (state) {
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
            case GAME_PAUSED:
                presentPaused();
                break;
            case GAME_LEVEL_END:
                presentLevelEnd();
                break;
            case GAME_OVER:
                presentGameOver();
                break;
        }
        game.batcher.end();
    }

    private void presentReady () {
        game.batcher.draw(Assets.ready, 160 - 192 / 2, 240 - 32 / 2, 192, 32);
    }

    private void presentRunning () {

    }

    private void presentPaused () {
        game.batcher.draw(Assets.pauseMenu, 160 - 192 / 2, 240 - 96 / 2, 192, 96);
        Assets.font.draw(game.batcher, scoreString, 16, 480 - 20);
    }

    private void presentLevelEnd () {
        String topText = "the princess is ...";
        String bottomText = "in another castle!";
        float topWidth = Assets.font.getBounds(topText).width;
        float bottomWidth = Assets.font.getBounds(bottomText).width;
        Assets.font.draw(game.batcher, topText, 160 - topWidth / 2, 480 - 40);
        Assets.font.draw(game.batcher, bottomText, 160 - bottomWidth / 2, 40);
    }

    private void presentGameOver () {
        game.batcher.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
        float scoreWidth = Assets.font.getBounds(scoreString).width;
        Assets.font.draw(game.batcher, scoreString, 160 - scoreWidth / 2, 480 - 20);
    }

    private void pauseSystems() {
        engine.getSystem(NarcissusSystem.class).setProcessing(false);
        engine.getSystem(MovementSystem.class).setProcessing(false);
        engine.getSystem(StateSystem.class).setProcessing(false);
        engine.getSystem(AnimationSystem.class).setProcessing(false);
    }

    private void resumeSystems() {
        engine.getSystem(NarcissusSystem.class).setProcessing(true);
        engine.getSystem(MovementSystem.class).setProcessing(true);
        engine.getSystem(StateSystem.class).setProcessing(true);
        engine.getSystem(AnimationSystem.class).setProcessing(true);
    }

    @Override
    public void render (float delta) {
        update(delta);
        drawUI();
    }

    @Override
    public void pause () {
        if (state == GAME_RUNNING) {
            state = GAME_PAUSED;
            pauseSystems();
        }
    }
}
