package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.makoware.narcissus.components.CameraComponent;

public class CameraSystem extends IteratingSystem {
    private ComponentMapper<CameraComponent> cm;

    public CameraSystem() {
        super(Family.getFor(CameraComponent.class));

        cm = ComponentMapper.getFor(CameraComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        CameraComponent cam = cm.get(entity);

        if (cam.target == null) {
            return;
        }

        //cam.camera.position.y = Math.max(cam.camera.position.y, target.pos.y);
    }
}
