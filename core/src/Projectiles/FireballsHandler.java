/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Projectiles;

import Screen.MainMenu;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;

/**
 *
 * @author Marzat Raphaël
 */

//It is a more complicated arrayList to handle the fireballs.
public class FireballsHandler{
    private World world;
    private ArrayList<Fireball> arFireball;
    
    public FireballsHandler(World world){
        arFireball = new ArrayList();
        this.world = world;
    }
    //Basic ArrayList stuffs
    public void remove(int i){
        this.arFireball.remove(i);
    }
    
    public void remove(Fireball fb){
        this.arFireball.remove(fb);
    }
    
    public void add(Fireball fb){
        this.arFireball.add(fb);
    }
    
    public int size(){
        return this.arFireball.size();
    }
    
    public Fireball get(int i){
        return this.arFireball.get(i);
    }
    //If there is a fireball in the FireballHandler and that fireball has travelled more than 5 units, we delete its body and remove it from the FbHandler.
    public void removeFireballs(){
        if(arFireball.size()>0){
            for(int i = 0; i < arFireball.size(); i++){ 
                arFireball.get(i).update();
                //debug
                //System.out.println(arFireball.get(i).getDistance());
                if(arFireball.get(i).getDistance()>= 5){
                    MainMenu.bodyToRemove.add(this.arFireball.get(i).getBody());
                    this.remove(i);
                }
            }
        }
    }
}
