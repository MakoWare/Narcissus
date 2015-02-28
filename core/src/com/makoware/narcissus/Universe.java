package com.makoware.narcissus;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.makoware.narcissus.components.B2DComponent;
import com.makoware.narcissus.components.NarcissusComponent;
import com.makoware.narcissus.components.CameraComponent;
import com.makoware.narcissus.components.MovementComponent;
import com.makoware.narcissus.components.StateComponent;
import com.makoware.narcissus.components.TransformComponent;
import com.makoware.narcissus.systems.RenderingSystem;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Universe {
    private static String TAG = "WORLD";

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;

    private Engine engine;
    private World world;
    private RayHandler rayHandler;

    public Universe(Engine engine, World world, RayHandler rayHandler) {
        this.engine = engine;
        this.world = world;
        this.rayHandler = rayHandler;
    }

    public void create() {
        Entity narcissus = createNarcissus();
        createGround();
        createLight();
        createCamera(narcissus);
    }

    private Entity createNarcissus() {
        Entity entity = new Entity();

        NarcissusComponent narcissus = new NarcissusComponent();
        B2DComponent b2d = new B2DComponent();

        //Create B2D Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(7.0f, 5.0f);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(1.0f, 1.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();
        b2d.body = body;

        entity.add(narcissus);
        entity.add(b2d);

        engine.addEntity(entity);

        return entity;
    }

    private void createLight(){
        new PointLight(rayHandler, 500, new Color(1,1,1,1), 30, 2, 3);
    }

    private void createGround(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0.0f, 0.0f);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(11.0f, 1.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();
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