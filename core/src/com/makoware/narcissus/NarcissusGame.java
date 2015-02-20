package com.makoware.narcissus;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.makoware.narcissus.screens.TestScreen;

public class NarcissusGame extends Game {
    // used by all screens
    public SpriteBatch batcher;

    @Override
    public void create () {
        batcher = new SpriteBatch();
        //Settings.load();
        //Assets.load();
        setScreen(new TestScreen(this));
    }

    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }
}