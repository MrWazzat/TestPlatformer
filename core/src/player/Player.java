/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import Helpers.GameInfo;
import Projectiles.Fireball;
import Screen.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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
//TEST GIT.
/**
 *
 * @author Marzat Raphaël
 */
public class Player extends Sprite implements ContactListener{
    //Can be set to private and then use getters;
    private World world;
    private Body body;
    //Static final variables for the position of the player and the direction he is looking towards.
    float timerDash = 3;
    private int position;
    private int direction;
    private static final int RIGHT = 0, LEFT = 1;
    private static final int AIR = 0, GROUND = 1, WALL = 2, WALLandFLOOR = 3;
    private boolean isInvincible;
    //MaxVelocity used for the clamping
    private static final float MAXVELOCITY = 7f;
    
    public Player(World world, String name, int posX, int posY){
        super(new Texture(name));
        this.world = world;
        this.setPosition(posX - getWidth() / 2f / GameInfo.PPM,
                posY - getHeight() / 2f / GameInfo.PPM);
        createBody();
    }
    //Creates the physical body in this order => Bdef to create body, Shape for the fdef and then fdef for the fixture
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
        fixture.setUserData("Player");
        
        shape.dispose();
    }
    //Updates the player's position, and the Fireballs
    public void update(){
        move();
        dash();
        this.setPosition(body.getPosition().x * GameInfo.PPM , body.getPosition().y *GameInfo.PPM);
        checkDirection();
        updateSprite();
        throwFireBall();
        MainMenu.fbToDisplay.removeFireballs();
    }
    
   
    protected void updateSprite(){
        if (direction == RIGHT){
            this.setTexture(new Texture("MageTempRIGHT.png"));
        }else if(direction == LEFT){
            this.setTexture(new Texture("MageTempLEFT.png"));
        }
    }
    
    // Les Linears impulses et les applyForce ne donnent pas un mouvement fluide et réactif (vitesse croissante non désirée dans un platformer).
    // Movement sections, was pretty tough. Still have to debug because it doesn't apply the desired force when you collide a wall not facing it. 
    protected void move(){
        body.setFixedRotation(true);
        if(timerDash>0.5){
            switch (position){
                case WALLandFLOOR:
                case GROUND:{
                    if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)){
                        this.body.applyLinearImpulse(new Vector2(this.body.getLinearVelocity().x,25f), this.body.getWorldCenter(), true);
                    }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)){
                        this.body.applyLinearImpulse(new Vector2(this.body.getLinearVelocity().x,25f), this.body.getWorldCenter(), true); 
                    }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                        this.body.setLinearVelocity(new Vector2(-7,0));
                    }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                        this.body.setLinearVelocity(new Vector2(7,0));
                    }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
                        this.body.applyLinearImpulse(new Vector2(0,50f), this.body.getWorldCenter(), true);
                    }else{
                        body.applyLinearImpulse(new Vector2(-this.body.getLinearVelocity().x,0), this.body.getWorldCenter(), true);
                    }
                }break;
                case WALL:{
                    if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)){
                        if(direction == LEFT){
                            this.body.setLinearVelocity(new Vector2(4f,8f));
                        }  
                    }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)){
                         if (direction == RIGHT){
                            this.body.setLinearVelocity(new Vector2(-4f,8f));
                        }     
                    }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                        if(direction == LEFT){
                            this.body.setLinearVelocity(new Vector2(0,-1f));
                        }else if (direction == RIGHT){
                            this.body.applyLinearImpulse(new Vector2(-7f,0f), this.body.getWorldCenter(), true);
                        }     
                    }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                        if(direction == LEFT){
                            this.body.applyLinearImpulse(new Vector2(7f,0f), this.body.getWorldCenter(), true);
                        }else if (direction == RIGHT){
                            this.body.setLinearVelocity(new Vector2(0,-1f));
                        }
                    }else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                        if(direction == LEFT){
                            this.body.applyLinearImpulse(new Vector2(7f,15f), this.body.getWorldCenter(), true);
                        }else if (direction == RIGHT){
                            this.body.applyLinearImpulse(new Vector2(-7f,15f), this.body.getWorldCenter(), true);
                        }
                    }else{
                        body.applyLinearImpulse(new Vector2(-this.body.getLinearVelocity().x,0), this.body.getWorldCenter(), true);
                    }   
                }break;
                //Gotta fix => When you press up in the air you go higher

                case AIR:{
                    if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                        this.body.applyForce(new Vector2(-100f,0), this.body.getWorldCenter(), true);
                    }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)){
                        this.body.applyForce(new Vector2(-100f,0), this.body.getWorldCenter(), true);
                    }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                        this.body.applyForce(new Vector2(100f,0), this.body.getWorldCenter(), true);
                    }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)){
                        this.body.applyForce(new Vector2(100f, 0), this.body.getWorldCenter(), true);
                    }else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                        this.body.applyForce(new Vector2(0, 30), this.body.getWorldCenter(), true);
                    }else{
                        //body.applyLinearImpulse(new Vector2(-this.body.getLinearVelocity().x,0), this.body.getWorldCenter(), true);
                    }
                }break;
            }
        }
        // CLAMPING (Merci François)
        if(this.body.getLinearVelocity().x > MAXVELOCITY){
            this.body.setLinearVelocity(new Vector2(MAXVELOCITY, body.getLinearVelocity().y));
        }else if(this.body.getLinearVelocity().x < -MAXVELOCITY){
            this.body.setLinearVelocity(new Vector2(-MAXVELOCITY, body.getLinearVelocity().y));
        }
        
        //Debug
        
        System.out.println(position);
//        System.out.println(body.getLinearVelocity());
            
        }
    
    
    
    protected void dash(){
        
        timerDash+=Gdx.graphics.getDeltaTime();
        System.out.println("Timer :" + timerDash);
        if(Gdx.input.isKeyPressed(Input.Keys.A) && timerDash > 3){
            if(direction == LEFT){
                this.body.setLinearVelocity(new Vector2(-12,0));
            }else if(direction == RIGHT){
                this.body.setLinearVelocity(new Vector2(12,0));
            } 
        timerDash = 0;
        }
    
    }
    
    protected void checkDirection(){
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            direction = LEFT;
        }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            direction = RIGHT;
        }
    //Uncomment to debug
//            System.out.println(direction);
        }
    //Uses the FireballHanler to throw fireball.
    protected void throwFireBall(){
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            if(direction == LEFT){
                MainMenu.fbToDisplay.add(new Fireball(world,this.getX() - this.getWidth(), this.getY(), new Vector2(-10,0)));
            }else if(direction == RIGHT){
                MainMenu.fbToDisplay.add(new Fireball(world,this.getX() + this.getWidth(), this.getY(), new Vector2(10,0)));
            }
        }   
    }
    
    //Contact listener
    @Override
    public void beginContact(Contact contact) {
        //Those horrors conditions are just here to check if it is the player and the environnement that are colliding
        if((contact.getFixtureA().getUserData() == "Wall" && contact.getFixtureB().getUserData() =="Player") || 
            (contact.getFixtureB().getUserData() == "Wall" && contact.getFixtureA().getUserData() =="Player")){
            if(position == GROUND){
                position = WALLandFLOOR;
            }else
            position = WALL;
        }else if ((contact.getFixtureA().getUserData() == "Ground" && contact.getFixtureB().getUserData() =="Player")||
                   (contact.getFixtureB().getUserData() == "Ground" && contact.getFixtureA().getUserData() =="Player")) {
            if(position == WALL){
                position = WALLandFLOOR;
            }else
            position = GROUND;
        }
        
        System.out.println(position);
    }

    
    @Override
    public void endContact(Contact contact) {
        
        if((contact.getFixtureA().getUserData() == "Wall" && contact.getFixtureB().getUserData() =="Player") || 
        (contact.getFixtureB().getUserData() == "Wall" && contact.getFixtureA().getUserData() =="Player")){
            if(position==WALLandFLOOR){
                position = GROUND;
            }else{
                position = AIR;
            }
        }else if((contact.getFixtureA().getUserData() == "Ground" && contact.getFixtureB().getUserData() =="Player")||
                   (contact.getFixtureB().getUserData() == "Ground" && contact.getFixtureA().getUserData() =="Player")){
            if(position==WALLandFLOOR){
                position = WALL;
            }else{
                position = AIR;
            }
        }
        System.out.println(position);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        
    }
}





































