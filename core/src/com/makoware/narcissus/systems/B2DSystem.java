package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;

public class B2DSystem extends IteratingSystem {
    private World world;

    public B2DSystem(Family family, World world) {
        super(family);
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {




    }
}
