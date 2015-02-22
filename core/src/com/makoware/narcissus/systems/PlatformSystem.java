package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.makoware.narcissus.World;
import com.makoware.narcissus.components.MovementComponent;
import com.makoware.narcissus.components.PlatformComponent;
import com.makoware.narcissus.components.StateComponent;
import com.makoware.narcissus.components.TransformComponent;

public class PlatformSystem extends IteratingSystem{
    private static final Family family = Family.getFor(PlatformComponent.class,
            StateComponent.class,
            TransformComponent.class,
            MovementComponent.class);
    private Engine engine;

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<PlatformComponent> pm;
    private ComponentMapper<StateComponent> sm;

    public PlatformSystem() {
        super(family);

        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        pm = ComponentMapper.getFor(PlatformComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PlatformComponent platform = pm.get(entity);

        if (platform.type == PlatformComponent.TYPE_MOVING) {
            TransformComponent pos = tm.get(entity);
            MovementComponent mov = mm.get(entity);

            if (pos.pos.x < PlatformComponent.WIDTH / 2) {
                mov.velocity.x = -mov.velocity.x;
                pos.pos.x = PlatformComponent.WIDTH / 2;
            }
            if (pos.pos.x > World.WORLD_WIDTH - PlatformComponent.WIDTH / 2) {
                mov.velocity.x = -mov.velocity.x;
                pos.pos.x = World.WORLD_WIDTH - PlatformComponent.WIDTH / 2;
            }
        }

        StateComponent state = sm.get(entity);

        if (state.get() == PlatformComponent.STATE_PULVERIZING &&
                state.time > PlatformComponent.PULVERIZE_TIME) {

            engine.removeEntity(entity);
        }
    }

    public void pulverize (Entity entity) {
        if (family.matches(entity)) {
            StateComponent state = sm.get(entity);
            MovementComponent mov = mm.get(entity);

            state.set(PlatformComponent.STATE_PULVERIZING);
            mov.velocity.x = 0;
        }
    }
}
