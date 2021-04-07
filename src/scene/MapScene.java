package scene;

import gameobj.GameObject;
import gameobj.TestObject1;
import gameobj.TestObject2;
import gameobj.TestObject3;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.CommandSolver;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapScene extends Scene {

    private ArrayList<GameObject> gameObjectArr = new ArrayList();

    @Override
    public void sceneBegin() {
        try {
            MapLoader mapLoader = new MapLoader("genMap.bmp", "genMap.txt");
            ArrayList<MapInfo> test = mapLoader.combineInfo();
            this.gameObjectArr = mapLoader.creatObjectArray("P1", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new TestObject1(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
            );
            this.gameObjectArr.addAll(mapLoader.creatObjectArray("P2", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new TestObject2(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
            ));
            this.gameObjectArr.addAll(mapLoader.creatObjectArray("P3", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new TestObject3(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
                    )
            );
            this.gameObjectArr.addAll(mapLoader.creatObjectArray("P3", 32, test, new MapLoader.CompareClass() {
                        @Override
                        public GameObject compareClassName(String gameObject, String name, MapInfo mapInfo, int size) {
                            GameObject tmp = null;
                            if (gameObject.equals(name)) {
                                tmp = new TestObject3(mapInfo.getX() * size, mapInfo.getY() * size);
                                return tmp;
                            }
                            return null;
                        }
                    }
                    )
            );
//            for (int i = 0; i < test.size(); i++) {    //  這邊可以看array內容  {String name ,int x, int y, int xSize, int ySize}
//                System.out.println(test.get(i).getName());
//                System.out.println(test.get(i).getX());
//                System.out.println(test.get(i).getY());
//                System.out.println(test.get(i).getSizeX());
//                System.out.println(test.get(i).getSizeY());
//            }
            this.gameObjectArr.forEach(a -> System.out.println(a.getClass().getSimpleName()));
        } catch (IOException ex) {
            Logger.getLogger(MapScene.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {
        return null;
    }

    @Override
    public void paint(Graphics g) {
        this.gameObjectArr.forEach(a -> a.paint(g));
    }

    @Override
    public void update() {

    }
}
