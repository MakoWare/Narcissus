package com.makoware.narcissus.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.makoware.narcissus.Universe;
import com.makoware.narcissus.components.MovementComponent;
import com.makoware.narcissus.components.NarcissusComponent;
import com.makoware.narcissus.components.StateComponent;
import com.makoware.narcissus.components.TransformComponent;

import java.util.Random;


public class CollisionSystem extends EntitySystem {
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;

    public static interface CollisionListener {

    }

    private Engine engine;
    private Universe universe;
    private CollisionListener listener;
    private Random rand = new Random();
    private ImmutableArray<Entity> narcisissuses;

    public CollisionSystem(Universe universe, CollisionListener listener) {
        this.universe = universe;
        this.listener = listener;

        mm = ComponentMapper.getFor(MovementComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        narcisissuses = engine.getEntitiesFor(Family.getFor(NarcissusComponent.class, TransformComponent.class, StateComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        NarcissusSystem bobSystem = engine.getSystem(NarcissusSystem.class);


    }
}