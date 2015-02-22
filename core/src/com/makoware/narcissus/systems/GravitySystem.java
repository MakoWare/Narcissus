package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.makoware.narcissus.World;
import com.makoware.narcissus.components.GravityComponent;
import com.makoware.narcissus.components.MovementComponent;

public class GravitySystem extends IteratingSystem{
    private ComponentMapper<MovementComponent> mm;

    public GravitySystem() {
        super(Family.getFor(GravityComponent.class, MovementComponent.class));

        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent mov = mm.get(entity);
        mov.velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
    }
}
