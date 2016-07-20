package TowerDefense;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Line;
import java.util.ArrayList;
import java.util.LinkedList;

public class TowerControl extends AbstractControl {

    private ArrayList<AbstractControl> reachable = new ArrayList<AbstractControl>();
    private GamePlayAppState appState;

    private long startTime = System.currentTimeMillis();
    private long fireInterval = 650;
    private long nextTime = startTime + fireInterval;

    private Geometry fireLineGeom;
    private boolean fired;
    private ArrayList<Charge> chargesArray = new ArrayList<Charge>();

    public TowerControl(GamePlayAppState appState) {
        this.appState = appState;               // state reference is needed to list of creeps and beam_node object
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);  // otherwise the spatial would be null
        
        // variable initialization
        for(int i = 0;i<getChargesNum(); i++){
            chargesArray.add(new Charge());
        }
    }
    
    @Override
    protected void controlUpdate(float tpf) {

        // As long as the tower has charges
        if (availableCharges() > 0) {
            // check distance against all creeps
            for (int i = 0; i < appState.getCreepNode().getQuantity(); i++) {
                // if found one close enough
                if (spatial.getLocalTranslation()
                        .distance(appState.getCreepNode().getChildren().get(i).getLocalTranslation()) < 8) {
                    // check if it's already exists in the reachable arraylist
                    if (reachable.indexOf(appState.getCreepNode().getChildren().get(i).getControl(CreepControl.class)) == -1) {
                        // if wasn't added already, add it to the reachable list
                        reachable.add(appState.getCreepNode().getChildren().get(i).getControl(CreepControl.class));
                    }
                }
            }

            // if the apatial has been detached, remove the respective control from reachable list
            for (int i = 0; i < reachable.size(); i++) {
                if(reachable.get(i).getSpatial().getParent() == null){
                    reachable.remove(i);
                }
            }
            

            // If the reachable list isn't draw a fireline to the closest creep
            if (reachable.size() > 0 && System.currentTimeMillis() > nextTime) {

                fire();
                fired = true;   // now we have a valid fireline

                nextTime = System.currentTimeMillis() + fireInterval;
            }
        }

        // after 150 milisec(<next file time) removing the fire line
        if (fired && System.currentTimeMillis() > nextTime - 500) {
            fireLineGeom.removeFromParent();
            fired = false;
        }
        
        // remove charges if bullets are all depleted
        for(int i = 0; i< chargesArray.size(); i++){
            if(chargesArray.get(i).getBulletCount() == 0){
                chargesArray.remove(chargesArray.get(i));
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public String getIndex() {
        return (String) spatial.getUserData("index");
    }

    public int getChargesNum() {
        return (Integer) spatial.getUserData("chargesNum");
    }
    
    public void addCharge(){
//        spatial.setUserData("chargesNum", (Integer) spatial.getUserData("chargesNum") + 1);
        chargesArray.add(new Charge());
    }

    private void fire() {
        // subtract bullet count before drawing it
        chargesArray.get(0).fireBullet();
        
        // draw the bullet line
        Line fireLine = new Line(spatial.getLocalTranslation(), reachable.get(0).getSpatial().getLocalTranslation());
        fireLineGeom = new Geometry("FireLine", fireLine);
        Material fireLineMat = new Material(appState.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        if (getIndex().equals("Tower Red")) {
            fireLineMat.setColor("Color", ColorRGBA.Red);
        } else {
            fireLineMat.setColor("Color", ColorRGBA.Green);
        }
        fireLineGeom.setMaterial(fireLineMat);
        appState.getBeamNode().attachChild(fireLineGeom);
        
        // deduct the creeps health
        ((CreepControl)reachable.get(0)).setHealth(((CreepControl)reachable.get(0)).getHealth()-chargesArray.get(0).getDamageValue());
    }
    
    public int availableCharges(){
        return chargesArray.size();
    }
}
