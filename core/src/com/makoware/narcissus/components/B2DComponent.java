package com.makoware.narcissus.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class B2DComponent extends Component{
    private Body body;

    public void setBody(Body body){
        this.body = body;
    }

}
