package camera;

import gameobj.GameObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class SmallMap extends Camera {

    private double smallMapZoomX; // 小地圖的X縮放率
    private double smallMapZoomY; // 小地圖的Y縮放率
    
    //顯示全圖：鏡頭大小與地圖大小相同，不要setChaseObj，並且要setCameraStartLocation將鏡頭起始位置設成(0,0)的位置
    //不顯示全圖：鏡頭大小自訂，設成要畫出來的大小，並且要setChaseObj。

    public SmallMap(Camera cam, Double smallMapZoomX, Double smallMapZoomY) {
        super(cam);
        this.smallMapZoomX = smallMapZoomX;
        this.smallMapZoomY = smallMapZoomY;
        setChaseDivisorX(1);
        setChaseDivisorY(1);
    }
    /*
    以下paint為提供常用的方法overload目前除放入圖片外，皆是畫長方形，
    如果要改成其他形狀，請利用Paint介面自定義要怎麼paint。
    如果有自定義物件畫出大小時，由於鏡頭視野框線，是以該物件實際所在的中心點畫出，
    因此小地圖上的物件可能不在鏡頭中心點。
    */
    
    public interface Paint{ // 可以使用這個接口自定義paint 的方法
        public void paint(Graphics g, GameObject obj);
    }
    
    //將物件轉換成方格畫出，自動將該類型物件生成相同顏色，大小依物件縮放
    public void paint(Graphics g, GameObject obj) { 
        Color c = getColor("" + obj.getClass());
        g.setColor(c);
        g.fillRect(obj.painter().left(), obj.painter().top(), 
                   obj.painter().width(), obj.painter().height());
        g.setColor(Color.BLACK);
    }
    //將物件轉換成方格畫出，自動將該類型物件生成相同顏色，大小自訂
    public void paint(Graphics g, GameObject obj, int width, int height) { 
        Color c = getColor("" + obj.getClass());
        g.setColor(c);
        g.fillRect(obj.painter().left(), obj.painter().top(), 
                   width, height);
        g.setColor(Color.BLACK);
    }
    //將物件轉換成方格畫出，顏色自訂，大小依物件縮放
    public void paint(Graphics g, GameObject obj, Color c) { 
        g.setColor(c);
        g.fillRect(obj.painter().left(), obj.painter().top(), 
                   obj.painter().width(), obj.painter().height());
        g.setColor(Color.BLACK);
    }
    //將物件轉換成方格畫出，顏色、大小自訂
    public void paint(Graphics g, GameObject obj, Color c, int width, int height) { 
        g.setColor(c);
        g.fillRect(obj.painter().left(), obj.painter().top(), 
                   width, height);
        g.setColor(Color.BLACK);
    }
    // 小地圖的物件顯示圖片，大小依物件縮放
    public void paint(Graphics g, GameObject obj, Image img){ 
        g.drawImage(img, obj.painter().left(), obj.painter().top(), 
           obj.painter().width(), obj.painter().height(), null);
    }
    // 小地圖的物件顯示圖片，大小自訂
    public void paint(Graphics g, GameObject obj, Image img, int width, int height){ 
        g.drawImage(img, obj.painter().left(), obj.painter().top(), width, height, null);
    }
    
    //畫追蹤物件的鏡頭框大小，camera放入追蹤物件的主鏡頭，即可在小地圖上顯示目前主鏡頭可見的範圍。
    public void paint(Graphics g, Camera camera, Color color) { 
        g.setColor(color);
        int targetX = camera.obj().painter().centerX() - camera.painter().width() / 2;
        int targetY = camera.obj().painter().centerY() - camera.painter().height() / 2;
        if(targetX < painter().left()){
            targetX = painter().left();
        }
       if(targetY < painter().top()){
            targetY = painter().top();
        }
        if(targetX > painter().right() - camera.painter().width()){
            targetX = painter().right() - camera.painter().width();
        }
       if(targetY > painter().bottom()  - camera.painter().height()){
            targetY = painter().bottom()  - camera.painter().height();
        }
        g.drawRect(targetX,  targetY, 
                 camera.painter().width(),  camera.painter().height());
        g.setColor(Color.BLACK);
    }

    /*使用時，請在場景的paint方法中
    1.smallMap.start(g) //將畫布移動到您的顯示視窗範圍(0,0)
    2.放入您的物件(請讓每個物件與smallMap做isCollision碰撞判斷，有重疊到才paint)
    EX: if(smallMap.isCollision(ac)){  // 如果只要主鏡頭內才顯示，就把smallMap 改成主鏡頭
            smallMap.paint(g,ac);
        }
    3. smallMap.end(g) 將畫布移回原位
    4. 如果有第二顆camera 再次操作 1 ~ 3。 
    */
    @Override
    public void start(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; //Graphics2D 為Graphics的子類，先做向下轉型。
        tmpCanvas = g2d.getTransform(); // 暫存畫布
        g2d.scale(smallMapZoomX, smallMapZoomY);  // 將畫布整體做縮放 ( * 縮放的係數)
        // 先將畫布初始移到(0,0) 然後再到自己想定位的地點(+ cameraWindowX. Y)，因為有被縮放的話要將為位移點調整-> (/ 縮放的係數)
        g2d.translate((-painter().left() + cameraWindowX() / smallMapZoomX), (-painter().top() + cameraWindowY() / smallMapZoomY));
        // 將畫布依照鏡頭大小作裁切
        g.setClip(painter().left(), painter().top(), painter().width(), painter().height());
    }

    public Color getColor(String str) {
        int colorCode = 0;
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            colorCode += (int) arr[i];
        }
        int[] colorCodeArr = new int[3];
        int[] temp = {colorCode % 255, colorCode % 175, colorCode % 90}; // 裝可能性，將得到的數字分不同參數做mod運算，以獲得差異較大的色碼
        for (int i = 0; i < 2; i++) {
            colorCodeArr[i] = temp[i];
        }
        Color c = new Color(colorCodeArr[0], colorCodeArr[1], colorCodeArr[2]);
        return c;
    }

    public void setSmallMapZoomX(double num) {
        smallMapZoomX = num;
    }
    public double smallMapZoomX() {
        return smallMapZoomX;
    }
    public void setSmallMapZoomY(double num) {
        smallMapZoomY = num;
    }
    public double smallMapZoomY() {
        return smallMapZoomY;
    }

}
