package com.makoware.narcissus.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.makoware.narcissus.Assets;
import com.makoware.narcissus.NarcissusGame;
import com.makoware.narcissus.World;
import com.makoware.narcissus.systems.AnimationSystem;
import com.makoware.narcissus.systems.BackgroundSystem;
import com.makoware.narcissus.systems.BobSystem;
import com.makoware.narcissus.systems.BoundsSystem;
import com.makoware.narcissus.systems.CameraSystem;
import com.makoware.narcissus.systems.CollisionSystem;
import com.makoware.narcissus.systems.CollisionSystem.CollisionListener;
import com.makoware.narcissus.systems.GravitySystem;
import com.makoware.narcissus.systems.MovementSystem;
import com.makoware.narcissus.systems.PlatformSystem;
import com.makoware.narcissus.systems.RenderingSystem;
import com.makoware.narcissus.systems.StateSystem;

public class TestScreen extends ScreenAdapter{
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
    private static final String TAG = "TestScreen";

    CollisionListener collisionListener;
    NarcissusGame game;
    OrthographicCamera guiCam;
    World world;
    Engine engine;
    private int state;

    public TestScreen (NarcissusGame game) {
        Gdx.app.log(TAG, "constructor()");
        this.game = game;

        state = GAME_RUNNING;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        engine = new Engine();
        world = new World(engine);

        engine.addSystem(new BobSystem(world));
        engine.addSystem(new PlatformSystem());
        engine.addSystem(new CameraSystem());
        engine.addSystem(new BackgroundSystem());
        engine.addSystem(new GravitySystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new StateSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new CollisionSystem(world, collisionListener));
        engine.addSystem(new RenderingSystem(game.batcher));

        engine.getSystem(BackgroundSystem.class).setCamera(engine.getSystem(RenderingSystem.class).getCamera());

        world.create();
        pauseSystems();
    }

    public void update (float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        engine.update(deltaTime);
        updateRunning(deltaTime);
    }

    private void updateRunning (float deltaTime) {
        ApplicationType appType = Gdx.app.getType();

        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        float accelX = 0.0f;

        if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
            accelX = Gdx.input.getAccelerometerX();
        } else {
            if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) accelX = 5f;
            if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) accelX = -5f;
        }

        engine.getSystem(BobSystem.class).setAccelX(accelX);
    }


    public void drawUI () {
        guiCam.update();
        game.batcher.setProjectionMatrix(guiCam.combined);
        game.batcher.begin();
        presentRunning();
        game.batcher.end();
    }


    private void presentRunning () {
        game.batcher.draw(Assets.pause, 320 - 64, 480 - 64, 64, 64);
        Assets.font.draw(game.batcher, "Taco", 16, 480 - 20);
    }


    private void pauseSystems() {
        engine.getSystem(BobSystem.class).setProcessing(false);
        engine.getSystem(PlatformSystem.class).setProcessing(false);
        engine.getSystem(GravitySystem.class).setProcessing(false);
        engine.getSystem(MovementSystem.class).setProcessing(false);
        engine.getSystem(BoundsSystem.class).setProcessing(false);
        engine.getSystem(StateSystem.class).setProcessing(false);
        engine.getSystem(AnimationSystem.class).setProcessing(false);
        engine.getSystem(CollisionSystem.class).setProcessing(false);
    }

    private void resumeSystems() {
        engine.getSystem(BobSystem.class).setProcessing(true);
        engine.getSystem(PlatformSystem.class).setProcessing(true);
        engine.getSystem(GravitySystem.class).setProcessing(true);
        engine.getSystem(MovementSystem.class).setProcessing(true);
        engine.getSystem(BoundsSystem.class).setProcessing(true);
        engine.getSystem(StateSystem.class).setProcessing(true);
        engine.getSystem(AnimationSystem.class).setProcessing(true);
        engine.getSystem(CollisionSystem.class).setProcessing(true);
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
