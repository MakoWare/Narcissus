package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.makoware.narcissus.Universe;
import com.makoware.narcissus.components.B2DComponent;
import com.makoware.narcissus.components.NarcissusComponent;


public class NarcissusSystem extends IteratingSystem{
    private static final Family family = Family.getFor(NarcissusComponent.class,  B2DComponent.class);
    private String movementDirection = "none";
    private Universe universe;

    private ComponentMapper<NarcissusComponent> nc;
    private ComponentMapper<B2DComponent> b2d;

    public NarcissusSystem(Universe universe) {
        super(family);

        this.universe = universe;
        nc = ComponentMapper.getFor(NarcissusComponent.class);
        b2d = ComponentMapper.getFor(B2DComponent.class);
    }

    public void setMovementDirection(String direction) {
        this.movementDirection = direction;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        movementDirection = "none";
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        B2DComponent box2d = b2d.get(entity);

        //TODO this should all probably go in a MovementSystem or ControlsSystem

        Vector2 currentVelocity = box2d.body.getLinearVelocity();
        if(movementDirection.equals("up")){
            float velocityChange = 10 - currentVelocity.y;
            float impulse = box2d.body.getMass() * velocityChange;
            box2d.body.applyLinearImpulse(new Vector2(0, impulse), box2d.body.getWorldCenter(), true);
        } else if(movementDirection.equals("right")){
            float velocityChange = 10 - currentVelocity.x;
            float impulse = box2d.body.getMass() * velocityChange;
            box2d.body.applyLinearImpulse(new Vector2(impulse, 0), box2d.body.getWorldCenter(), true);
        } else if(movementDirection.equals("down")) {
            float velocityChange = -10 - currentVelocity.y;
            float impulse = box2d.body.getMass() * velocityChange;
            box2d.body.applyLinearImpulse(new Vector2(0, impulse), box2d.body.getWorldCenter(), true);
        } else if (movementDirection.equals("left")){
            float velocityChange = -10 - currentVelocity.x;
            float impulse = box2d.body.getMass() * velocityChange;
            box2d.body.applyLinearImpulse(new Vector2(impulse, 0), box2d.body.getWorldCenter(), true);
        } else {
            box2d.body.setLinearVelocity(new Vector2(0, 0));
        }
    }
}
