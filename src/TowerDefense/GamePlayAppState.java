package TowerDefense;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class GamePlayAppState extends AbstractAppState {
    private SimpleApplication app;
    private Node rootNode;
    private AssetManager assetManager;
    
    public Node playerNode, towerNode, creepNode;
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
        rootNode.attachChild(playerNode);
        rootNode.attachChild(towerNode);
        rootNode.attachChild(creepNode);
        
        // Add Player
        Geometry playerGeometry = playerGeom(new Vector3f(0, .5f, 0f));
        playerNode.attachChild(playerGeometry);

        // Add 2 towers
        Geometry towerGeometryA = towerGeom(new Vector3f(6, 2f, 4));
        towerGeometryA.setUserData("index", "Tower A");
        towerGeometryA.setUserData("chargesNum", 10);
        towerNode.attachChild(towerGeometryA);
        
        Geometry towerGeometryB = towerGeom(new Vector3f(-6, 2f, 4));
        towerGeometryB.setUserData("index", "Tower B");
        towerGeometryB.setUserData("chargesNum", 10);
        towerNode.attachChild(towerGeometryB);
        
        // Add 5 creeps
        Geometry creepGeometryA = creepGeom(new Vector3f(1, .5f, 10));
        creepGeometryA.setUserData("index", "Creep A");
        creepGeometryA.setUserData("health", 100);
        creepGeometryA.addControl(new CreepControl(this));
        creepNode.attachChild(creepGeometryA);
        
        Geometry creepGeometryB = creepGeom(new Vector3f(-1, .5f, 12));
        creepGeometryB.setUserData("index", "Creep B");
        creepGeometryB.setUserData("health", 100);
        creepGeometryB.addControl(new CreepControl(this));
        creepNode.attachChild(creepGeometryB);
        
        Geometry creepGeometryC = creepGeom(new Vector3f(2, .5f, 14));
        creepGeometryC.setUserData("index", "Creep C");
        creepGeometryC.setUserData("health", 100);
        creepGeometryC.addControl(new CreepControl(this));
        creepNode.attachChild(creepGeometryC);
        
        Geometry creepGeometryD = creepGeom(new Vector3f(-2, .5f, 16));
        creepGeometryD.setUserData("index", "Creep D");
        creepGeometryD.setUserData("health", 100);
        creepGeometryD.addControl(new CreepControl(this));
        creepNode.attachChild(creepGeometryD);
        
        Geometry creepGeometryE = creepGeom(new Vector3f(0, .5f, 18));
        creepGeometryE.setUserData("index", "Creep E");
        creepGeometryE.setUserData("health", 100);
        creepGeometryE.addControl(new CreepControl(this));
        creepNode.attachChild(creepGeometryE);
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
        Geometry geom = new Geometry("PlayerGeom", new Box(1,3f,1));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat);
        geom.setLocalTranslation(position);
        return geom;
    }
        
    Geometry creepGeom(Vector3f position){
        Geometry geom = new Geometry("PlayerGeom", new Box(.5f,.5f,.5f));
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
}
