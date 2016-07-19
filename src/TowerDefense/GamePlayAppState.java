package TowerDefense;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;

public class GamePlayAppState extends AbstractAppState {
    private SimpleApplication app;
    private Node rootNode;
    private AssetManager assetManager;
    
    public Node playerNode, towerNode, creepNode;
    private ArrayList<Geometry> creepGeometryArray;
    private Node beamNode;

    private int level, score, health, budget;
    private boolean lastGameWon;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication)app;
        this.rootNode = this.app.getRootNode(); // notice the user of this.app instead the use of app
        this.assetManager = this.app.getAssetManager();
        
        Geometry floor = new Geometry("Floor", new Box(33f,0.1f, 33f));
        Material floorMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMat.setColor("Color", ColorRGBA.Orange);
        floor.setMaterial(floorMat);
        rootNode.attachChild(floor);
        
        playerNode = new Node();
        towerNode = new Node();
        creepNode = new Node();
        beamNode = new Node();
        rootNode.attachChild(playerNode);
        rootNode.attachChild(towerNode);
        rootNode.attachChild(creepNode);
        rootNode.attachChild(beamNode);
        
        // Add Player
        Geometry playerGeometry = playerGeom(new Vector3f(0, .5f, 0f));
        playerNode.attachChild(playerGeometry);

        // Add 2 towers
        Geometry towerGeometryA = towerGeom(new Vector3f(6, 2f, 4));
        towerGeometryA.getMaterial().setColor("Color", ColorRGBA.Red);
        towerGeometryA.setUserData("index", "Tower Red");
        towerGeometryA.setUserData("chargesNum", 10);
        towerGeometryA.addControl(new TowerControl(this));      // REMEMBER : addControl initializes spatial variable
        towerNode.attachChild(towerGeometryA);
        
        Geometry towerGeometryB = towerGeom(new Vector3f(-6, 2f, 4));
        towerGeometryB.setUserData("index", "Tower Green");
        towerGeometryB.setUserData("chargesNum", 5);
        towerGeometryB.addControl(new TowerControl(this));
        towerNode.attachChild(towerGeometryB);
        
        // Add 5 creeps
        creepGeometryArray = new ArrayList<Geometry>();
        for(int i = 0; i<5; i++){
            creepGeometryArray.add(creepGeom(new Vector3f(i*-1, .5f, (i*2)+10), i));
//            creepGeometryArray.add(creepGeom(new Vector3f(FastMath.nextRandomInt(-4, 4), .5f, (i*2)+10), i));
            creepGeometryArray.get(i).setUserData("index", "Creep A");
            creepGeometryArray.get(i).setUserData("health", 10);
            creepGeometryArray.get(i).addControl(new CreepControl(this));
            creepNode.attachChild(creepGeometryArray.get(i));
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.detachChild(playerNode);
        rootNode.detachChild(towerNode);
        rootNode.detachChild(creepNode);
    }

    Geometry playerGeom(Vector3f position){
        Geometry geom = new Geometry("PlayerGeom", new Box(5,2,.25f));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Yellow);
        geom.setMaterial(mat);
        geom.setLocalTranslation(position);
        return geom;
    }
    
    Geometry towerGeom(Vector3f position){
        Geometry geom = new Geometry("TowerGeom", new Box(1,3f,1));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat);
        geom.setLocalTranslation(position);
        return geom;
    }
        
    Geometry creepGeom(Vector3f position, int index){
        Geometry geom = new Geometry("CreepGeom " + index, new Box(.5f,.5f,.5f));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        geom.setMaterial(mat);
        geom.setLocalTranslation(position);
        return geom;
    }
    
    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
    
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public boolean isLastGameWon() {
        return lastGameWon;
    }

    public void setLastGameWon(boolean lastGameWon) {
        this.lastGameWon = lastGameWon;
    }
    
    public ArrayList<Geometry> getCreepGeometryArray() {
        return creepGeometryArray;
    }

    public void setCreepGeometryArray(ArrayList<Geometry> creepGeometryArray) {
        this.creepGeometryArray = creepGeometryArray;
    }
    
    public Node getBeamNode() {
        return beamNode;
    }

    public void setBeamNode(Node beamNode) {
        this.beamNode = beamNode;
    }
    
    public AssetManager getAssetManager(){
        return this.assetManager;
    }
}
