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
import com.makoware.narcissus.components.MovementComponent;
import com.makoware.narcissus.components.StateComponent;
import com.makoware.narcissus.components.TransformComponent;

public class NarcissusSystem extends IteratingSystem{
    private static final Family family = Family.getFor(NarcissusComponent.class,
            StateComponent.class,
            TransformComponent.class,
            MovementComponent.class);

    private float accelX = 0.0f;
    private Universe universe;

    private ComponentMapper<NarcissusComponent> bm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<B2DComponent> b2d;

    public NarcissusSystem(Universe universe) {
        super(family);

        this.universe = universe;

        bm = ComponentMapper.getFor(NarcissusComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        b2d = ComponentMapper.getFor(B2DComponent.class);
    }

    public void setAccelX(float accelX) {
        this.accelX = accelX;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        accelX = 0.0f;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);
        B2DComponent box2d = b2d.get(entity);
        NarcissusComponent narcissus = bm.get(entity);

//        Gdx.app.log("NarcissusSystem", " " +  accelX);
        if(accelX > 0){
            box2d.body.setLinearVelocity(new Vector2(-10, 0));
            //box2d.body.applyLinearImpulse(new Vector2(-1, 0), box2d.body.getWorldCenter(), true);
        } else if(accelX < 0) {
            box2d.body.setLinearVelocity(new Vector2(10, 0));
            //box2d.body.applyLinearImpulse(new Vector2(1, 0), box2d.body.getWorldCenter(), true);
        } else {
            box2d.body.setLinearVelocity(new Vector2(0, 0));
        }


        if (state.get() != NarcissusComponent.STATE_HIT) {
            mov.velocity.x = -accelX / 10.0f * NarcissusComponent.MOVE_VELOCITY;
        }

        if (mov.velocity.y > 0 && state.get() != NarcissusComponent.STATE_HIT) {
            if (state.get() != NarcissusComponent.STATE_JUMP) {
                state.set(NarcissusComponent.STATE_JUMP);
            }
        }

        if (mov.velocity.y < 0 && state.get() != NarcissusComponent.STATE_HIT) {
            if (state.get() != NarcissusComponent.STATE_FALL) {
                state.set(NarcissusComponent.STATE_FALL);
            }
        }

        if (t.pos.x < 0) {
            t.pos.x = Universe.WORLD_WIDTH;
        }

        if (t.pos.x > Universe.WORLD_WIDTH) {
            t.pos.x = 0;
        }

        t.scale.x = mov.velocity.x < 0.0f ? Math.abs(t.scale.x) * -1.0f : Math.abs(t.scale.x);

    }
}
