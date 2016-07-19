package TowerDefense;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Line;
import java.util.ArrayList;

public class TowerControl extends AbstractControl{
    private int chargesNum;
    private ArrayList<Geometry> reachable = new ArrayList<Geometry>();
    private GamePlayAppState appState;
    
    private long startTime = System.currentTimeMillis();
    private long fireInterval = 250;
    private long nextTime = startTime + fireInterval;
    
    private Geometry fireLineGeom;
    private boolean fired;

    public TowerControl(GamePlayAppState appState) {
        this.appState = appState;               // state reference is needed to list of creeps and beam_node object
    }

    @Override
    protected void controlUpdate(float tpf) {
        
        // As long as the tower has charges
        if(getChargesNum() > 0){
            // check distance against all creeps
            for(int i = 0; i<appState.getCreepGeometryArray().size(); i++){
                // if found one close enough
                if(spatial.getLocalTranslation()
                        .distance(appState.getCreepGeometryArray().get(i).getLocalTranslation()) < 7){
                    // check if it's already exists in the reachable arraylist
                    if(reachable.indexOf((Geometry)appState.getCreepGeometryArray().get(i)) == -1){
                        // if wasn't added already, add it to the reachable list
                        reachable.add(appState.getCreepGeometryArray().get(i));
                    }
                }
            }
        }

        // check again all the added reachable creep hasn't reached the player
        for(int i = 0; i < reachable.size(); i++){
            // if yes, then remove it from the reachable list, cause it has been removed
            if(appState.getCreepGeometryArray().contains((Geometry)reachable.get(i)) == false){
                reachable.remove(reachable.get(i));
            }
        }
            
        // If the reachable list isn't draw a fireline to the closest creep
        if(reachable.size() > 0 && System.currentTimeMillis() > nextTime){
            
            fire();
            fired = true;   // now we have a valid fireline
            
            nextTime = System.currentTimeMillis() + fireInterval;
        }
        
        // after 150 milisec(<next file time) removing the fire line
        if(fired && System.currentTimeMillis() > nextTime - 150){
            fireLineGeom.removeFromParent();
            fired = false;
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public String getIndex(){
        return (String) spatial.getUserData("index");
    }
    
    public int getChargesNum(){
        return (Integer) spatial.getUserData("chargesNum");
    }

    private void fire() {
        Line fireLine = new Line(spatial.getLocalTranslation(), reachable.get(0).getLocalTranslation());
        fireLineGeom = new Geometry("FireLine", fireLine);
        Material fireLineMat = new Material(appState.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        if(getIndex().equals("Tower Red"))
            fireLineMat.setColor("Color", ColorRGBA.Red);
        else 
            fireLineMat.setColor("Color", ColorRGBA.Green);
        fireLineGeom.setMaterial(fireLineMat);
        appState.getBeamNode().attachChild(fireLineGeom);
    }
}
