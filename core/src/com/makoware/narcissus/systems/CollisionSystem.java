package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.makoware.narcissus.World;
import com.makoware.narcissus.components.BobComponent;
import com.makoware.narcissus.components.BoundsComponent;
import com.makoware.narcissus.components.MovementComponent;
import com.makoware.narcissus.components.PlatformComponent;
import com.makoware.narcissus.components.StateComponent;
import com.makoware.narcissus.components.TransformComponent;

import java.util.Random;

/**
 * Created by yin on 2/21/15.
 */
public class CollisionSystem extends EntitySystem {
    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;

    public static interface CollisionListener {
        public void jump ();
        public void highJump ();
        public void hit ();
        public void coin ();
    }

    private Engine engine;
    private World world;
    private CollisionListener listener;
    private Random rand = new Random();
    private ImmutableArray<Entity> bobs;
    private ImmutableArray<Entity> coins;
    private ImmutableArray<Entity> squirrels;
    private ImmutableArray<Entity> springs;
    private ImmutableArray<Entity> castles;
    private ImmutableArray<Entity> platforms;

    public CollisionSystem(World world, CollisionListener listener) {
        this.world = world;
        this.listener = listener;

        bm = ComponentMapper.getFor(BoundsComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        bobs = engine.getEntitiesFor(Family.getFor(BobComponent.class, BoundsComponent.class, TransformComponent.class, StateComponent.class));
        platforms = engine.getEntitiesFor(Family.getFor(PlatformComponent.class, BoundsComponent.class, TransformComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        BobSystem bobSystem = engine.getSystem(BobSystem.class);
        PlatformSystem platformSystem = engine.getSystem(PlatformSystem.class);

        for (int i = 0; i < bobs.size(); ++i) {
            Entity bob = bobs.get(i);

            StateComponent bobState = sm.get(bob);

            if (bobState.get() == BobComponent.STATE_HIT) {
                continue;
            }

            MovementComponent bobMov = mm.get(bob);
            BoundsComponent bobBounds = bm.get(bob);

            if (bobMov.velocity.y < 0.0f) {
                TransformComponent bobPos = tm.get(bob);

                for (int j = 0; j < platforms.size(); ++j) {
                    Entity platform = platforms.get(j);

                    TransformComponent platPos = tm.get(platform);

                    if (bobPos.pos.y > platPos.pos.y) {
                        BoundsComponent platBounds = bm.get(platform);

                        if (bobBounds.bounds.overlaps(platBounds.bounds)) {
                            bobSystem.hitPlatform(bob);
                            listener.jump();
                            if (rand.nextFloat() > 0.5f) {
                                platformSystem.pulverize(platform);
                            }

                            break;
                        }
                    }
                }
            };
        }
    }
}
