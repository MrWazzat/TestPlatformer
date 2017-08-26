/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screen;

import Ennemies.ClassicEnnemy;
import Environment.GroundTemp;
import Environment.WallTemp;
import Helpers.GameInfo;
import Projectiles.Fireball;
import Projectiles.FireballsHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import java.util.ArrayList;
import player.Player;

/**
 *
 * @author Marzat Raphaël
 */
public class MainMenu implements Screen, ContactListener{
    private MyGdxGame game;
    private Texture bg;
    private World world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private Player player;
    private ClassicEnnemy en;
    private Fireball fireball;
    private GroundTemp gt;
    private WallTemp wt;
    private WallTemp wt2;
    public static ArrayList<Body> bodyToRemove;
    public static FireballsHandler fbToDisplay;
    public static ArrayList<ClassicEnnemy> ennemiesToDisplay;

    
    public MainMenu(MyGdxGame game) {
        this.game =  game;
        //Basic stuffs : Camera position + settings + assigning value to the variables
        ennemiesToDisplay = new ArrayList();
        bodyToRemove = new ArrayList();
        fbToDisplay = new FireballsHandler(world);
        bg =  new Texture("DesertBackground.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameInfo.WIDTH/GameInfo.PPM, GameInfo.HEIGHT/GameInfo.PPM);
        camera.position.set(GameInfo.WIDTH/2, GameInfo.HEIGHT/2, 0);
        debugRenderer = new Box2DDebugRenderer();
        
        this.world = new World(new Vector2(0, -15f), true);
        //Player initialisation
        player = new Player(world, "MageTempRIGHT.png", GameInfo.WIDTH/2
                ,GameInfo.HEIGHT/2);
        
       //These temps objects have no texture. We might use a tilemap later
        gt =  new GroundTemp(world);
        wt = new WallTemp(world, GameInfo.WIDTH/16, GameInfo.HEIGHT);
        wt2 = new WallTemp(world, GameInfo.WIDTH-GameInfo.WIDTH/16, GameInfo.HEIGHT);
        en = new ClassicEnnemy(world, gt.getX()+gt.getWidth()/2, gt.getY() + (GameInfo.HEIGHT / 4f) + 64); //A modifier quand on aura les sprites
        ennemiesToDisplay.add(en);
        
        world.setContactListener(this);
    }
    
    
    @Override
    public void show() {
    }
    
    public void update(float dt){
        player.update();
        for(int i = 0; i<ennemiesToDisplay.size(); i++){
            ennemiesToDisplay.get(i).update();
        }
        System.out.println("Number of bodies waiting to be removed : " + bodyToRemove.size());
        for (int i = 0; i < bodyToRemove.size(); i++) {
            world.destroyBody(bodyToRemove.get(i));
            bodyToRemove.remove(i);
        }
    }
    
    @Override
    public void render(float delta) {
       //updating values and positions of the objects
        update(delta);
        //Cleaning the screen 
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Drawing all of our objects on the Screen
        game.getBatch().begin();
       
        game.getBatch().draw(bg, 0, 0);
	game.getBatch().draw(player, player.getX() - player.getWidth()/2, player.getY() - player.getHeight()/2);
        //System.out.println("Nombre de boules de feu  :" + fbToDisplay.size()); DEBUG
        for(int i = 0; i < fbToDisplay.size(); i++){
            game.getBatch().draw(fbToDisplay.get(i), 
                    fbToDisplay.get(i).getX()-fbToDisplay.get(i).getWidth()/2,
                    fbToDisplay.get(i).getY()-fbToDisplay.get(i).getHeight()/2);
        }
        for(int i = 0; i < ennemiesToDisplay.size(); i++){
            game.getBatch().draw(ennemiesToDisplay.get(i), 
                    ennemiesToDisplay.get(i).getX()-ennemiesToDisplay.get(i).getWidth()/2,
                    ennemiesToDisplay.get(i).getY()-ennemiesToDisplay.get(i).getHeight()/2);
        }
        
        game.getBatch().end();
        //Debug Renderer displays hitboxes
        debugRenderer.render(world, camera.combined);
        //The world refreshes the gravity and stuffs
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void beginContact(Contact contact) {
        player.beginContact(contact);
        en.beginContact(contact);
        for(int i = 0; i< fbToDisplay.size(); i++){
            fbToDisplay.get(i).beginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        player.endContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
    
}
