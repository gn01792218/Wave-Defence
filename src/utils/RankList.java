package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RankList {
    public Scanner sc= new Scanner(System.in);

    public ArrayList<String> name = new ArrayList<>();
    public ArrayList<Integer> score = new ArrayList<>();
    int rankCount =5;//排行榜上限

    //讀取紀錄檔
    public RankList(){
        try{
            String s;
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\ANDY\\test\\output.txt"));
            while ((s=br.readLine())!=null){
                int ch = s.indexOf('/',0);
                name.add(s.substring(0,ch));
                score.add(Integer.valueOf(s.substring(ch+1)));
            }
        }catch (Exception e){
            return;
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

            System.out.println("Pleas type in your name");
            name.set(rank,sc.next());
            score.set(rank,newScore);

            //寫入新資料
            try{
                BufferedWriter bw = new BufferedWriter(
                        new FileWriter("C:\\Users\\ANDY\\test\\output.txt"));
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

}
