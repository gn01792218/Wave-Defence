package menu;

import utils.Global;
//帶有角色種類的Button
public class ActorButton extends Button{
    private Global.ActorType actorType;
    private int number;//存放這個角色的數量

    public ActorButton(int x, int y, Style style,Global.ActorType actorType) {
        super(x, y, style);
        this.actorType=actorType;
    }

    //方法
    public Global.ActorType getActorType(){
        return this.actorType;
    }

    public int getNumber() {
        return number;
    }
    public void offSetNumber(int number){
        this.number+=number;
    }
}
