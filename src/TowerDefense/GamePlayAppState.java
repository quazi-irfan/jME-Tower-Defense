package TowerDefense;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class GamePlayAppState extends AbstractAppState {

    private Trigger towerTrigger = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private Trigger rechargeTrigger = new KeyTrigger(KeyInput.KEY_R);

    private SimpleApplication app;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Camera cam;

    private Geometry floorGeom;
    private Node playerNode, towerNode, creepNode;
    private Node beamNode;

    private int level, score, health = 1, budget = 5;
    private float budgeTimer;
    private boolean lastGameWon;
    private Spatial selectedTower;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.rootNode = this.app.getRootNode(); // notice the user of this.app instead the use of app
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();

        floorGeom = new Geometry("Floor", new Box(33f, 0.1f, 33f));
        Material floorMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMat.setColor("Color", ColorRGBA.Orange);
        floorGeom.setMaterial(floorMat);
        rootNode.attachChild(floorGeom);

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
        towerGeometryA.setUserData("chargesNum", 5);
        towerGeometryA.addControl(new TowerControl(this));      // REMEMBER : addControl initializes spatial variable
        towerNode.attachChild(towerGeometryA);

        Geometry towerGeometryB = towerGeom(new Vector3f(-6, 2f, 4));
        towerGeometryB.setUserData("index", "Tower Green");
        towerGeometryB.setUserData("chargesNum", 5);
        towerGeometryB.addControl(new TowerControl(this));
        towerNode.attachChild(towerGeometryB);

        // Add 5 creeps        
        for (int i = 0; i < 5; i++) {
            Geometry temp = creepGeom(new Vector3f(i*-1, .5f, (i*2)+10), i);
//            Geometry temp = creepGeom(new Vector3f(FastMath.nextRandomInt(-4, 4), .5f, (i * 2) + 10), i);
            temp.setUserData("index", "Creep A");
            temp.setUserData("health", 10);
            temp.addControl(new CreepControl(this));
            creepNode.attachChild(temp);
        }

        inputManager.addMapping("select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("recharge", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(selectAction, "select");
        inputManager.addListener(rechargeAction, "recharge");
    }
    
    @Override
    public void update(float tpf) {
        updateBudget(tpf);
        // updateBeamNod(tpf); // Wait for 1 sec to clear out all the lines
        checkWinLoose();
    }
    
    private void checkWinLoose() {
        if(creepNode.getQuantity() == 0 && health > 0){
            System.out.println("Player won");
            lastGameWon = true;
        }
        
        if(health <= 0){
            System.out.println("Player lost");
            lastGameWon = false;
            this.app.getStateManager().detach(this);
        }
    }
    
    private void updateBudget(float tpf) {
        budgeTimer += tpf;
        if(budgeTimer > 5){
            budget++;
            budgeTimer = 0;
        }
    }

    private ActionListener selectAction = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                CollisionResults results = new CollisionResults();
                Vector3f dir = cam.getLocation().subtract(cam.getWorldCoordinates(inputManager.getCursorPosition(), 1)).negateLocal();
                Ray ray = new Ray(cam.getLocation(), dir);
                towerNode.collideWith(ray, results);

                if (results.size() > 0) {
                    selectedTower = results.getClosestCollision().getGeometry();
                }
            }
        }
    };

    private ActionListener rechargeAction = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if(selectedTower != null && isPressed){
                if(budget > 0){
                    TowerControl tempControl = selectedTower.getControl(TowerControl.class);
                    tempControl.addCharge();
                    budget--;
                }
            }
        }
    };

    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.detachChild(floorGeom);
        rootNode.detachChild(playerNode);
        rootNode.detachChild(towerNode);
        rootNode.detachChild(creepNode);
    }

    Geometry playerGeom(Vector3f position) {
        Geometry geom = new Geometry("PlayerGeom", new Box(5, 2, .25f));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Yellow);
        geom.setMaterial(mat);
        geom.setLocalTranslation(position);
        return geom;
    }

    Geometry towerGeom(Vector3f position) {
        Geometry geom = new Geometry("TowerGeom", new Box(1, 3f, 1));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat);
        geom.setLocalTranslation(position);
        return geom;
    }

    Geometry creepGeom(Vector3f position, int index) {
        Geometry geom = new Geometry("CreepGeom " + index, new Box(.5f, .5f, .5f));
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

    public Node getBeamNode() {
        return beamNode;
    }

    public void setBeamNode(Node beamNode) {
        this.beamNode = beamNode;
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    public Node getPlayerNode() {
        return playerNode;
    }

    public void setPlayerNode(Node playerNode) {
        this.playerNode = playerNode;
    }

    public Node getTowerNode() {
        return towerNode;
    }

    public void setTowerNode(Node towerNode) {
        this.towerNode = towerNode;
    }

    public Node getCreepNode() {
        return creepNode;
    }

    public void setCreepNode(Node creepNode) {
        this.creepNode = creepNode;
    }
}
