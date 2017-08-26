package com.mygdx.game;

import Helpers.GameInfo;
import Screen.MainMenu;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	private SpriteBatch batch;
        
	@Override
	public void create () {
            batch = new SpriteBatch();
            setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
            super.render();
	}
        
        public SpriteBatch getBatch(){
            return this.batch;
        }
}
