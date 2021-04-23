package utils;

import menu.EditText;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class RankList implements GameKernel.PaintInterface {
//    public Scanner sc= new Scanner(System.in);

    public ArrayList<String> name = new ArrayList<>();
    public ArrayList<Integer> score = new ArrayList<>();
    public EditText editText;
    int rankCount =5;//排行榜上限

    Path path = Paths.get("");
    String directoryName = path.toAbsolutePath().toString();

    //讀取紀錄檔
    public RankList(){
        System.out.println(directoryName);
        try{
            String s;
            BufferedReader br = new BufferedReader(new FileReader(directoryName+"\\output.txt"));
            while ((s=br.readLine())!=null){
                int ch = s.indexOf('/',0);
                name.add(s.substring(0,ch));
                score.add(Integer.valueOf(s.substring(ch+1)));
            }
        }catch (Exception e){
            try{
                BufferedWriter bw = new BufferedWriter(
                        new FileWriter(directoryName+"\\output.txt"));
                for(int i=0;i<5;i++){
                    bw.write("non/0");
                    bw.write("\n");
                }
                bw.close();
            }catch (Exception ee){
                return;
            }

            try {
                String s;
                BufferedReader br = new BufferedReader(new FileReader(directoryName+"\\output.txt"));
                while ((s=br.readLine())!=null){
                    int ch = s.indexOf('/',0);
                    name.add(s.substring(0,ch));
                    score.add(Integer.valueOf(s.substring(ch+1)));
                }
            }catch (Exception eee){
                return;
            }
        }
    }

    public void newScore(int x){

        int newScore= x;
        int min= (int) score.get(score.size()-1);;

        if(newScore>min){

            int rank =0;
            while(newScore<score.get(rank)){
                rank++;
            }

            for(int i=4;i>rank;i--){
                score.set(i,score.get(i-1));
                name.set(i,name.get(i-1));
            }

//            System.out.println("Pleas type in your name");
            name.set(rank,"");
            score.set(rank,newScore);

            //寫入新資料
            try{
                BufferedWriter bw = new BufferedWriter(
                        new FileWriter(directoryName+"\\output.txt"));
                for(int i=0;i<5;i++){
                    bw.write(name.get(i)+'/'+score.get(i));
                    bw.write("\n");
                }
                bw.close();
            }catch (Exception e){
                return;
            }
        }
    }

    @Override
    public void paint(Graphics g) {

    }
}
