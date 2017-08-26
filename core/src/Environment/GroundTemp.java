/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Environment;

import Helpers.GameInfo;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author Marzat Raphaël
 */
public class GroundTemp extends Sprite{
    private World world;
    private Body body;
    
    public GroundTemp(World world){
        this.world = world;
        setPosition(GameInfo.WIDTH/2, 0);
        createBody();
    }
    
     public void createBody(){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(getX()/GameInfo.PPM, getY()/GameInfo.PPM);
        
        body = world.createBody(bdef);
        
        PolygonShape shape = new PolygonShape(); 
        shape.setAsBox(GameInfo.WIDTH/GameInfo.PPM,
                (GameInfo.HEIGHT / 4f)/GameInfo.PPM);
        
        FixtureDef fdef = new FixtureDef();
        fdef.shape =  shape;
               
        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData("Ground");
        
        
        shape.dispose();
    }
     
     
}
