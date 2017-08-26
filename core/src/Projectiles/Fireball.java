/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Projectiles;

import Ennemies.ClassicEnnemy;
import Helpers.GameInfo;
import Screen.MainMenu;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author Marzat Raphaël
 */
public class Fireball extends Sprite implements ContactListener{
    private World world;
    private Body body;
    private int direction;
    private float distance;
    private float posInit;
    
    public Fireball(World world, float posX, float posY, Vector2 direction){
        super(new Texture("Fireball.png"));
        this.world = world;
        this.setPosition(posX - getWidth() / 2f / GameInfo.PPM,
        posY - getHeight() / 2f / GameInfo.PPM);
        createBody(posX, posY);
        posInit = body.getPosition().x;
        move(direction);
    }
    //creating the physical body of the firebal in oreder to detect collision and forces later on
    public void createBody(float posX, float posY){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.KinematicBody;
        bdef.position.set(getX()/GameInfo.PPM, getY()/GameInfo.PPM);
        
        body = world.createBody(bdef);
   
        CircleShape shape = new CircleShape();
        shape.setRadius(this.getWidth()/2/GameInfo.PPM);
       
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        Fixture fixture = body.createFixture(fdef);
        
        fixture.setUserData("Fireball");
        shape.dispose();
    }
    
    public Body getBody(){
        return this.body;
    }
    //moving the fb is simple, it moves horizontally in the direction of the player
    public void move(Vector2 direction){
        body.setLinearVelocity(direction);
    }
    //updating the position of the fireball
    public void update(){
        this.setPosition(body.getPosition().x * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
    }
    //Useful to know how many unity the fireball has travelled. Used later to delete it at a certain point
    public double getDistance(){
        return Math.sqrt((this.body.getPosition().x - this.posInit)*(this.body.getPosition().x  - this.posInit));
    }

    @Override
    public void beginContact(Contact contact) {
        if((contact.getFixtureA().getUserData() == "Fireball" && contact.getFixtureB().getUserData() =="ClassicEnnemy") || 
            (contact.getFixtureB().getUserData() == "Fireball" && contact.getFixtureA().getUserData() =="ClassicEnnemy")){
                MainMenu.fbToDisplay.remove(this);
                MainMenu.bodyToRemove.add(body);
        }
           
    }

    @Override
    public void endContact(Contact contact) {
        
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
       
    }
    
    
}
