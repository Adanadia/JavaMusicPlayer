package music.player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.media.*;
import java.io.IOException;


/**
 * @author HeDiya
 */
public class Audioplay {
    private Player player;
    public void SetPlayAudioPath(String path){//选择播放文件
        MediaLocator locator=new MediaLocator("file:"+path);
        try {
            player = Manager.createRealizedPlayer(locator);
            player.prefetch();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoPlayerException e) {
            e.printStackTrace();
        } catch (CannotRealizeException e) {
            e.printStackTrace();
        }
    }
    public void play(){
        player.start();
    }
    public void stop(){
        player.stop();
    }
}
class Filemusic{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String str = null;
        System.out.print("请输入文件路径：");
        while (true){
            str = sc.nextLine();
            try {
                FileInputStream fileInputStream = new FileInputStream(str);
                break;
            } catch (FileNotFoundException e) {
                System.out.println("文件找不到请重新输入");
            }
        }
        System.out.println("音乐文件\""+str.substring(5,str.length())+"\"读取成功");
        Menu.showAndOption(str);
        System.exit(0);
    }
    static class Menu{
        public static void showAndOption(String string){
            while(true){
                Audioplay audioPlay=new Audioplay();
                audioPlay.SetPlayAudioPath(string);
                System.out.println("------***********-------");
                System.out.println(" 播放：play；停止：stop；退出：exit ");
                System.out.println("------***********-------");
                Scanner sc = new Scanner(System.in);
                String i = sc.next();
                if(i.equals("play")){
                    System.out.println("option:play");
                    System.out.println(string.substring(5,string.length())+"播放中\n");
                    audioPlay.play();
                }
                else if(i.equals("stop")){
                    System.out.println("option:stop");
                    System.out.println(string.substring(5,string.length())+"暂停\n");
                    audioPlay.stop();
                }
                else if(i.equals("exit")){
                    audioPlay.stop();
                    System.out.println("option:退出\n");
                    break;
                }else{
                    System.out.println("option:next");
                    System.out.println("无效的指令\n");
                }
            }
        }
    }
}