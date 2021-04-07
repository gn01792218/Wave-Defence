package utils;

public class Delay {
    private int count; //計時器，計算當下經過的偵數
    private int countLimit;// 計數的上限(總共要記幾禎)
    private boolean isPause;//是否暫停記數
    private boolean isLoop;//是否進行週期性的延遲
    //建構子
    public Delay(int countLimit){
        this.countLimit=countLimit;
        count=0;
        isPause=true; //剛開始的時候沒有要記時
        isLoop=false; //剛開始不要週期性延遲，需要時再用
    }

    //方法區
    //停止記時的方法
    public void stop(){
        count=0; //記時器歸0
        this.isPause=true; //暫停記時
    }
    //開始記時
    public void play(){
        this.isPause=false;
    }
    //執行週期延遲
    public void loop(){
        this.isLoop=true; //開始週期延遲
        this.isPause=false; //不要暫停
    }
    //暫停記時
    public void pause(){
        this.isPause=true;
    }
    //是否未開始延遲
    public boolean isStop(){
        return count==0 && isPause; //計數器是0的時候，且是暫停的狀態-->表示還沒開始記時
    }
    //是否正在延遲中
    public boolean isPlaying(){
        return !this.isPause; //非暫停，就是在延遲中
    }
    //是否是暫停的狀態
    public boolean isPause(){
        return isPause;
    }


    // 這個方法需要反覆在更新中被呼叫，並通過被呼叫的次數來進行延遲的判斷
    public boolean count(){  //會回傳是否觸發目標動作 的計時器
        if(isPause){ //假如現在是暫停記時狀態
            return false; //即不會計算也不會觸發延遲後要執行的事件
        }
        if(count>=countLimit){ //假如記數記到大於等於記數上限時
            if(this.isLoop){
                // 如果是週期性執行的情況就重新將count歸零繼續計算
                this.count = 0;
            }else {
                this.stop(); //就停止記數
            }
            return true;  //觸發我們要的動作
        }
        count++; //記時
        return false;   //還不要觸發我們要的動作
    }
}
