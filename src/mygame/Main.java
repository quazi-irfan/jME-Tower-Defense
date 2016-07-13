package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        BufferUtils.setTrackDirectMemoryEnabled(true);
        app.setShowSettings(false);        
        app.start();
    }

    @Override
    public void simpleInitApp() {       
//        setDisplayStatView(false);
//        setDisplayFps(false);
        
        flyCam.setMoveSpeed(50);
        cam.setLocation(new Vector3f(0f, 10f, 50f));

        Geometry floor = new Geometry("Floor", new Box(33f,0.1f, 33f));
        Material floorMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMat.setColor("Color", ColorRGBA.Orange);
        floor.setMaterial(floorMat);
        rootNode.attachChild(floor);
        
        Node playerNode = new Node();
        Node towerNode = new Node();
        Node creepNode = new Node();
        rootNode.attachChild(playerNode);
        rootNode.attachChild(towerNode);
        rootNode.attachChild(creepNode);
        
        playerNode.attachChild(playerGeom(new Vector3f(0, .5f, 0f)));
        
        towerNode.attachChild(towerGeom(new Vector3f(6, 2f, 4)));
        towerNode.attachChild(towerGeom(new Vector3f(-6, 2f, 4)));
        
        creepNode.attachChild(creepGeom(new Vector3f(1, .5f, 10)));
        creepNode.attachChild(creepGeom(new Vector3f(-1, .5f, 11)));
        creepNode.attachChild(creepGeom(new Vector3f(2, .5f, 12)));
        creepNode.attachChild(creepGeom(new Vector3f(-2, .5f, 13)));
        creepNode.attachChild(creepGeom(new Vector3f(0, .5f, 14)));
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    // Advanced renderer/frameBuffer modifications
    @Override
    public void simpleRender(RenderManager rm) {
    }
    
    Geometry playerGeom(Vector3f position){
        Geometry geom = new Geometry("PlayerGeom", new Box(5,2,1));
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
}
