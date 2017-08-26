/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ennemies;

import Helpers.GameInfo;
import Projectiles.FireballsHandler;
import Screen.MainMenu;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author Marzat Raphaël
 */
public class ClassicEnnemy extends Sprite implements ContactListener{
    private World world;
    private Body body;
    private boolean IsAlive;
    
    public ClassicEnnemy(World world, float posX, float posY){
        super(new Texture("Ombre.png"));
        this.world = world;
        IsAlive = true;
        this.setPosition(posX - getWidth() / 2f / GameInfo.PPM,
                posY - getHeight() / 2f / GameInfo.PPM);
        createBody();    
        world.setContactListener(this);
    }
    
    public void createBody(){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(getX()/GameInfo.PPM, getY()/GameInfo.PPM);
        
        body = world.createBody(bdef);
        
        PolygonShape shape = new PolygonShape(); 
        shape.setAsBox((getWidth() / 2f)/GameInfo.PPM,
                (getHeight() / 2f)/GameInfo.PPM);
        
        FixtureDef fdef = new FixtureDef();
        fdef.shape =  shape;
        fdef.density =16;
        fdef.friction = 0.1f;
        
        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData("ClassicEnnemy");
        
        shape.dispose();
    }
    
    public void die(){
        System.out.println("removed");
        MainMenu.ennemiesToDisplay.remove(this);
        MainMenu.bodyToRemove.add(body);
    }
    
    public boolean IsAlive(){
        return IsAlive;
    }
    
    public void update(){
        this.setPosition(body.getPosition().x * GameInfo.PPM , body.getPosition().y *GameInfo.PPM);
    }

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getUserData() == "Fireball" || contact.getFixtureB().getUserData() == "Fireball"){
            this.die();
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
