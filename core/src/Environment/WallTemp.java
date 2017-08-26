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
public class WallTemp extends Sprite{
    private World world;
    private Body body;
    
    public WallTemp(World world, int posX, int posY){
        this.world = world;
        setPosition(posX, posY);
        createBody();
    }
    
     public void createBody(){
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(getX()/GameInfo.PPM, getY()/GameInfo.PPM);
        
        body = world.createBody(bdef);
        
        PolygonShape shape = new PolygonShape(); 
        shape.setAsBox((GameInfo.WIDTH / 16f) / GameInfo.PPM,
                (GameInfo.HEIGHT)/GameInfo.PPM);
        
        FixtureDef fdef = new FixtureDef();
        fdef.shape =  shape;
        fdef.friction = 70;
        fdef.restitution = 0.5f;
        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData("Wall");
        
        shape.dispose();
    }
     
     
}
