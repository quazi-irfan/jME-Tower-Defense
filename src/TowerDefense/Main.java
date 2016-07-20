package TowerDefense;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
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

        flyCam.setMoveSpeed(150);
        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
        
        cam.setLocation(new Vector3f(0f, 10f, 50f));
        
        GamePlayAppState gamePlayState = new GamePlayAppState();
        stateManager.attach(gamePlayState);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    // Advanced renderer/frameBuffer modifications
    @Override
    public void simpleRender(RenderManager rm) {
    }
    
    
}
