package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.makoware.narcissus.components.TextureComponent;

import java.util.Comparator;

import box2dLight.RayHandler;

public class RenderingSystem extends IteratingSystem{
    static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;
    static final float PIXELS_TO_METRES = 1.0f / 32.0f;

    private SpriteBatch batch;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    private OrthographicCamera cam;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private RayHandler rayHandler;

    private ComponentMapper<TextureComponent> textureM;

    public RenderingSystem(SpriteBatch batch, World world, Box2DDebugRenderer debugRenderer, RayHandler rayHandler) {
        super(Family.getFor(TextureComponent.class));

        textureM = ComponentMapper.getFor(TextureComponent.class);

        renderQueue = new Array<Entity>();
        this.batch = batch;
        this.world = world;
        this.debugRenderer = debugRenderer;
        this.rayHandler = rayHandler;

        cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);


        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        debugRenderer.render(world, cam.combined);


        renderQueue.sort(comparator);

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        batch.end();

        rayHandler.setCombinedMatrix(cam.combined);
        rayHandler.updateAndRender();

        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }
}
