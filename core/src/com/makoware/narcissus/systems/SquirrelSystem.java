package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.makoware.narcissus.World;
import com.makoware.narcissus.components.MovementComponent;
import com.makoware.narcissus.components.SquirrelComponent;
import com.makoware.narcissus.components.TransformComponent;

public class SquirrelSystem extends IteratingSystem{
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;

    public SquirrelSystem() {
        super(Family.getFor(SquirrelComponent.class,
                TransformComponent.class,
                MovementComponent.class));

        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        MovementComponent mov = mm.get(entity);

        if (t.pos.x < SquirrelComponent.WIDTH * 0.5f) {
            t.pos.x = SquirrelComponent.WIDTH * 0.5f;
            mov.velocity.x = SquirrelComponent.VELOCITY;
        }
        if (t.pos.x > World.WORLD_WIDTH - SquirrelComponent.WIDTH * 0.5f) {
            t.pos.x = World.WORLD_WIDTH - SquirrelComponent.WIDTH * 0.5f;
            mov.velocity.x = -SquirrelComponent.VELOCITY;
        }

        t.scale.x = mov.velocity.x < 0.0f ? Math.abs(t.scale.x) * -1.0f : Math.abs(t.scale.x);
    }
}
