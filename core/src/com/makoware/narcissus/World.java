package com.makoware.narcissus;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.makoware.narcissus.components.AnimationComponent;
import com.makoware.narcissus.components.BackgroundComponent;
import com.makoware.narcissus.components.BobComponent;
import com.makoware.narcissus.components.BoundsComponent;
import com.makoware.narcissus.components.CameraComponent;
import com.makoware.narcissus.components.CastleComponent;
import com.makoware.narcissus.components.CoinComponent;
import com.makoware.narcissus.components.GravityComponent;
import com.makoware.narcissus.components.MovementComponent;
import com.makoware.narcissus.components.PlatformComponent;
import com.makoware.narcissus.components.SpringComponent;
import com.makoware.narcissus.components.SquirrelComponent;
import com.makoware.narcissus.components.StateComponent;
import com.makoware.narcissus.components.TextureComponent;
import com.makoware.narcissus.components.TransformComponent;
import com.makoware.narcissus.systems.RenderingSystem;

import java.util.Random;

public class World {
    private static String TAG = "WORLD";

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0, -12);

    public final Random rand;

    public float heightSoFar;
    public int score;
    public int state;

    private Engine engine;

    public World(Engine engine) {
        this.engine = engine;
        this.rand = new Random();
    }

    public void create() {
        Entity bob = createBob();
        createCamera(bob);

        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
    }

    private Entity createBob() {
        Entity entity = new Entity();

        AnimationComponent animation = new AnimationComponent();
        BobComponent bob = new BobComponent();
        BoundsComponent bounds = new BoundsComponent();
        //GravityComponent gravity = new GravityComponent();
        MovementComponent movement = new MovementComponent();
        TransformComponent position = new TransformComponent();
        StateComponent state = new StateComponent();
        TextureComponent texture = new TextureComponent();

        animation.animations.put(BobComponent.STATE_FALL, Assets.bobFall);
        animation.animations.put(BobComponent.STATE_HIT, Assets.bobHit);
        animation.animations.put(BobComponent.STATE_JUMP, Assets.bobJump);

        bounds.bounds.width = BobComponent.WIDTH;
        bounds.bounds.height = BobComponent.HEIGHT;

        position.pos.set(5.0f, 1.0f, 0.0f);

        state.set(BobComponent.STATE_JUMP);

        entity.add(animation);
        entity.add(bob);
        entity.add(bounds);
        //entity.add(gravity);
        entity.add(movement);
        entity.add(position);
        entity.add(state);
        entity.add(texture);

        engine.addEntity(entity);

        return entity;
    }


    private void createCamera(Entity target) {
        Entity entity = new Entity();

        CameraComponent camera = new CameraComponent();
        camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
        camera.target = target;

        entity.add(camera);

        engine.addEntity(entity);
    }

    private void createBackground() {
        Entity entity = new Entity();

        BackgroundComponent background = new BackgroundComponent();
        TransformComponent position = new TransformComponent();
        TextureComponent texture = new TextureComponent();

        texture.region = Assets.backgroundRegion;

        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }
}