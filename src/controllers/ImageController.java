package controllers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//單例模式的圖片管理者
//需求: 所有圖片由圖片管理者掌控
//      且每張圖片只會產生一次
public class ImageController {
    private static ImageController imageController; //靜態實體
    //改成Map的方式
    private Map<String ,BufferedImage> imageMap;
    private ImageController(){//私有化建構子，杜絕外部new
        imageMap=new HashMap<>();
    }

    //單例模式靜態方法;取得實體-->且只會產生一個實體
    public static ImageController getInstance(){
        if(imageController==null){
            imageController=new ImageController();
        }
        return imageController;
    }
    //加入圖片的方法，傳入路徑，創建圖片-->私有化
    private BufferedImage add(String path){
        BufferedImage img=null;
        try {
            img= ImageIO.read(getClass().getResource(path));
            this.imageMap.put(path,img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    //取得陣列中圖片的方法-->根據路徑找該圖片
    public BufferedImage tryGet(String path){
        if(this.imageMap.containsKey(path)){
            return imageMap.get(path);
        }
        return add(path);
    }

}
