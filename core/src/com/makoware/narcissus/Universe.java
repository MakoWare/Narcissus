package com.makoware.narcissus;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.makoware.narcissus.components.AnimationComponent;
import com.makoware.narcissus.components.NarcissusComponent;
import com.makoware.narcissus.components.CameraComponent;
import com.makoware.narcissus.components.MovementComponent;
import com.makoware.narcissus.components.StateComponent;
import com.makoware.narcissus.components.TextureComponent;
import com.makoware.narcissus.components.TransformComponent;
import com.makoware.narcissus.systems.RenderingSystem;

import java.util.Random;

public class Universe {
    private static String TAG = "WORLD";

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;

    private Engine engine;
    private int state;

    public Universe(Engine engine) {
        this.engine = engine;
    }

    public void create() {
        Entity bob = createBob();
        createCamera(bob);

        this.state = WORLD_STATE_RUNNING;
    }

    private Entity createBob() {
        Entity entity = new Entity();

        AnimationComponent animation = new AnimationComponent();
        NarcissusComponent narcissus = new NarcissusComponent();
        MovementComponent movement = new MovementComponent();
        TransformComponent position = new TransformComponent();
        StateComponent state = new StateComponent();
        TextureComponent texture = new TextureComponent();

        animation.animations.put(NarcissusComponent.STATE_FALL, Assets.bobFall);
        animation.animations.put(NarcissusComponent.STATE_HIT, Assets.bobHit);
        animation.animations.put(NarcissusComponent.STATE_JUMP, Assets.bobJump);

        position.pos.set(5.0f, 1.0f, 0.0f);

        state.set(NarcissusComponent.STATE_JUMP);

        entity.add(animation);
        entity.add(narcissus);
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
}