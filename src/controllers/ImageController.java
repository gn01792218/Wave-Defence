package controllers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

//單例模式的圖片管理者
//需求: 所有圖片由圖片管理者掌控
//      且每張圖片只會產生一次
public class ImageController {
    private static ImageController imageController; //靜態實體
    private ArrayList<KeyPair> keyPairs; //存放所有圖片的陣列
    private ImageController(){//私有化建構子，杜絕外部new
        keyPairs=new ArrayList<>();
    }

    //單例模式靜態方法;取得實體-->且只會產生一個實體
    public static ImageController getInstance(){
        if(imageController==null){
            imageController=new ImageController();
        }
        return imageController;
    }

    //加入圖片的方法，傳入路徑，創建圖片-->私有化
    private Image add(String path){
        Image img=null;
        try {
            img=ImageIO.read(getClass().getResource(path));
            this.keyPairs.add(new KeyPair(path,img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    //取得陣列中圖片的方法-->根據路徑找該圖片
    public Image tryGet(String path){
        for(int i=0;i<keyPairs.size();i++){
            if(this.keyPairs.get(i).path.equals(path)){ //假如傳進來的路徑相等
                return this.keyPairs.get(i).image;
            }
        }
        return add(path);
    }

    private static class KeyPair{ //私有的靜態內部類
        private String path;
        private Image image;
        public KeyPair(String path,Image image){
            this.path=path;
            this.image=image;
        }
        //不用寫get，因為是內部類，可以直接取得path和image。
    }

}
