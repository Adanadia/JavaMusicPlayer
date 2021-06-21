package com.zlqhe.xyz;

import music.player.*;
import java.awt.*;

import javax.swing.*;
import java.awt.event.*;

import java.io.File;
import java.net.URL;
import java.util.*;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.Font;
import java.awt.FileDialog;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyExtendsJFrame frame=new MyExtendsJFrame();//创建聊天程序窗口
    }
}

class MyExtendsJFrame extends JFrame {
    int nPlayTime=0;
    int musicTime;
    Vector<String> vt=new Vector<>();//创建Vector对象，通过add方法添加多行
    TreeMap<Integer,String> treeMap = new TreeMap<>();//设置播放文件及其路径
    TreeMap<Integer,String> treeMapLrc = new TreeMap<>();//设置音乐歌词及其路径,路径必须在music同级别的lrc目录下且lrc必须和music同名
    Integer audioNum = 0;//设置音乐数量存入treeMap
    public void timerFun(int select){
        if(nTimer!=null){
            nTimer.cancel();
        }
        nTimer = new Timer();
        TimerTask timerTask1 = new TimerTask() {
            @Override
            public void run() {
                playTime.setBounds(0, 485, nPlayTime+=((int)(1080/musicTime)+1), 4);
            }
        };
        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                playTime.setBounds(0, 485, nPlayTime+=0, 4);
            }
        };
        if(select == 1){
            nTimer.schedule(timerTask1,nPlayTime,1000);
        }else if(select == 2){
            nTimer.schedule(timerTask2,nPlayTime,1000);
        }
    }
    public void audioPlayFun(){
        if(lrcThread!=null) {
            lrcThread.resume();
            countDownThread.resume();
        }else{
            try {
                playLrc(treeMapLrc.get(listPlayFile.getAnchorSelectionIndex()));
                countDown(treeMap.get(listPlayFile.getAnchorSelectionIndex()));
                lrcThread.start();
                countDownThread.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        try{
            musicTime=new MusicTime(treeMap.get(listPlayFile.getAnchorSelectionIndex())).havTime();
            audioPlay.play();//开始播放
            add(buttonStop);
            add(playGifLabel);
            remove(buttonPlay);
            remove(readyPlayImg);


            timerFun(1);
        }catch (NullPointerException nullPointerException){
            JOptionPane.showMessageDialog(rootPane,"播放列表里面没有音乐");
        }catch (Exception exception){
            JOptionPane.showMessageDialog(rootPane,"未知错误");
        }
    }
    public void audioStopFun(){
        add(buttonPlay);
        add(readyPlayImg);
        remove(buttonStop);
        remove(playGifLabel);
        timerFun(2);
        audioPlay.stop();
        if(lrcThread!=null) {
            lrcThread.suspend();
            countDownThread.suspend();
        }
    }
    Thread lrcThread;
    Runnable lrcRunnable;
    public void playLrc(String load) throws InterruptedException {
        lrcRunnable=new LrcThread(load,lrcLyrics);
        lrcThread = new Thread(lrcRunnable);
        //lrcThread.start();
    }
    Thread countDownThread;
    Runnable countDwonRunnable;
    public void countDown(String path) throws Exception {
        countDwonRunnable = new CountDownThread(path,timePlne,buttonNext);
        countDownThread=new Thread(countDwonRunnable);
    }
    class ActListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(buttonPlay == e.getSource()){
                if(listPlayFile.getAnchorSelectionIndex()==-1 && !vt.isEmpty()) {
                    listPlayFile.setSelectedIndex(vt.size()-1);
                    audioPlay.SetPlayAudioPath(treeMap.get(listPlayFile.getAnchorSelectionIndex()));
                }
                audioPlayFun();
            }
            if(buttonStop == e.getSource()){
                audioStopFun();
            }
            if(buttonOpenFile == e.getSource()){
                FileDialog openFile=new FileDialog(new Dialog(new Frame()),"打开文件");//创建打开文件对话框
                openFile.setVisible(true);//对话框可见
                if (openFile.getFile()==null){
                    return;
                }
                if(!vt.isEmpty()){
                    audioStopFun();
                    nPlayTime = 0;
                }
                String playFileName=openFile.getFile();//获取打开的文件名
                String playFileDirectory=openFile.getDirectory();//获取打开的文件路径
                String playFile=playFileDirectory+playFileName;//完整的路径+文件名
                String[] aftSpl = playFile.split("\\\\");
                if(playFile.toString().equals("nullnull")){
                    return;
                }
                if(vt.contains((String)aftSpl[aftSpl.length-1])){
                    return;
                }else{
                    vt.add(aftSpl[aftSpl.length-1]);
                }
                if(treeMap.containsValue(playFile)){
                }else {
                    treeMap.put(audioNum, playFile);
                }
                String lrcFile = playFile.split("music")[0]+"lrc"+playFile.split("music")[1].split("wav")[0]+"lrc";
                File lrcMatchFile = new File(lrcFile);
                if(lrcMatchFile.exists()){
                    if(treeMapLrc.containsValue(lrcFile)){
                    }else {
                        treeMapLrc.put(audioNum, lrcFile);
                    }
                }else{
                    treeMapLrc.put(audioNum,null);
                }
                audioNum++;
                audioPlay.SetPlayAudioPath(playFile);//设置播放文件
                audioPlay.stop();
                listPlayFile.setListData(vt);//将文件添加到播放菜单
            }
            if(buttonNext == e.getSource()){
                nPlayTime = 0;
                audioStopFun();
                if(listPlayFile.getAnchorSelectionIndex()!=vt.size()-1){
                    listPlayFile.setSelectedIndex(listPlayFile.getAnchorSelectionIndex()+1);
                    audioPlay.SetPlayAudioPath(treeMap.get(listPlayFile.getAnchorSelectionIndex()));
                }
                else{
                    listPlayFile.setSelectedIndex(0);
                    audioPlay.SetPlayAudioPath(treeMap.get(0));
                }
                lrcThread.interrupt();
                lrcThread=null;
                audioPlayFun();
            }
            if(buttonPrev == e.getSource()){
                nPlayTime = 0;
                audioStopFun();
                //System.out.println(vt.size()-1);
                if(listPlayFile.getAnchorSelectionIndex()!=0){
                    listPlayFile.setSelectedIndex(listPlayFile.getAnchorSelectionIndex()-1);
                    audioPlay.SetPlayAudioPath(treeMap.get(listPlayFile.getAnchorSelectionIndex()));
                }
                else{
                    listPlayFile.setSelectedIndex(vt.size()-1);
                    audioPlay.SetPlayAudioPath(treeMap.get(vt.size()-1));
                }
                lrcThread.interrupt();
                lrcThread=null;
                audioPlayFun();
            }
            if(rePlay == e.getSource()){
                try {
                    nPlayTime = 0;
                    audioStopFun();
                    listPlayFile.setSelectedIndex(listPlayFile.getAnchorSelectionIndex());
                    audioPlay.SetPlayAudioPath(treeMap.get(listPlayFile.getAnchorSelectionIndex()));
                    lrcThread.interrupt();
                    lrcThread = null;
                    audioPlayFun();
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(rootPane,"错误，首先检查播放列表是否为空");
                }
            }
        }
    }
    class MouListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() == listPlayFile){
                if(e.getClickCount()==2){
                    nPlayTime = 0;
                    audioStopFun();
                    audioPlay.SetPlayAudioPath(treeMap.get(listPlayFile.getAnchorSelectionIndex()));
                    if(lrcThread!=null) {
                        lrcThread.interrupt();
                    }
                    lrcThread = null;
                    audioPlayFun();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            buttonOpenFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonPlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonStop.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            buttonPrev.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            rePlay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    ActListener actListener = new ActListener();
    MouListener mouListener = new MouListener();

    JButton buttonOpenFile;//打开文件按钮
    Audioplay audioPlay=new Audioplay();  //创建播放对象
    JLabel background = new JLabel();
    JButton buttonPlay = new JButton();
    JButton buttonStop = new JButton();
    JTextPane textLyrics = new JTextPane();
    JLabel playTime = new JLabel();
    public JList listPlayFile = new JList();
    Timer nTimer = new Timer();

    JButton buttonNext;//下一条音乐
    JButton buttonPrev;//上一条音乐
    JTextPane lrcLyrics;//音乐歌词显示
    JButton rePlay;//重新播放歌曲

    JLabel gifLabel;
    JLabel playGifLabel;
    JLabel readyPlayImg;

    JTextPane timePlne;//显示时间倒计时
    public MyExtendsJFrame(){
        setTitle("史上最牛逼的播放器");
        setBounds(320,100,1080,640);
        setLayout(null);
        init();   //添加控件的操作封装成一个函数
        setVisible(true);//放在添加组件后面执行
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void init(){//添加的控件
        Toolkit tk=Toolkit.getDefaultToolkit();
        Image image=tk.createImage("Main\\img\\head.jpg");
        this.setIconImage(image);

        ImageIcon imageIcon = new ImageIcon("Main\\img\\centerGif.gif");//添加gif的类
        gifLabel=new JLabel();
        gifLabel.setIcon(imageIcon);
        gifLabel.setBounds(750,90,345,219);
        add(gifLabel);

        ImageIcon imageIcon2=new ImageIcon("Main\\img\\videoPlayGif.gif");
        playGifLabel = new JLabel();
        playGifLabel.setIcon(imageIcon2);
        playGifLabel.setBounds(70,80,400,300);
        //add(playGifLabel);

        ImageIcon imageIcon3=new ImageIcon("Main\\img\\readyToPlay.png");
        readyPlayImg = new JLabel();
        readyPlayImg.setIcon(imageIcon3);
        readyPlayImg.setBounds(146,80,400,300);
        add(readyPlayImg);

        Icon img=new ImageIcon("Main\\img\\时光旅客background.jpg");
        background = new JLabel(img);
        background.setBounds(0,0,1080,640);
        getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        ((JPanel)getContentPane()).setOpaque(false);

        buttonPlay=new JButton();
        buttonPlay.setBounds(522,500,60,60);
        Icon playIcon=new ImageIcon("Main\\img\\play.png");    //创建图标对象
        buttonPlay.setIcon(playIcon);
        buttonPlay.setContentAreaFilled(false);
        //remove(buttonStop);
        buttonPlay.addActionListener(actListener);
        buttonPlay.addMouseListener(mouListener);
        add(buttonPlay);

        buttonStop=new JButton();
        //buttonStop.setBorder(null);
        buttonStop.setBounds(522,500,60,60);
        Icon stopIcon=new ImageIcon("Main\\img\\stop.png");     //创建图标对象
        buttonStop.setIcon(stopIcon);
        buttonStop.setContentAreaFilled(false);
        buttonStop.addActionListener(actListener);
        buttonStop.addMouseListener(mouListener);
        //add(buttonStop);

        buttonOpenFile=new JButton("");//添加打开文件按钮
        buttonOpenFile.setBounds(800,520,40,40); //设置打开文件按钮大小
        Icon openIcon=new ImageIcon("Main\\img\\open.png"); //创建图标对象
        buttonOpenFile.setIcon(openIcon);    //设置播放图标
        buttonOpenFile.setContentAreaFilled(false);
        buttonOpenFile.addActionListener(actListener);//添加单击事件关联
        buttonOpenFile.addMouseListener(mouListener);
        add(buttonOpenFile);//添加打开文件至窗口中

        rePlay=new JButton();
        rePlay.setBounds(845,520,40,40);
        Icon replayIcon=new ImageIcon("Main\\img\\replay.png");
        rePlay.setIcon(replayIcon);    //设置播放图标
        rePlay.setContentAreaFilled(false);
        rePlay.addActionListener(actListener);//添加单击事件关联
        rePlay.addMouseListener(mouListener);
        add(rePlay);

        buttonNext=new JButton();
        //buttonStop.setBorder(null);
        buttonNext.setBounds(590,510,40,40);
        Icon nextIcon=new ImageIcon("Main\\img\\next.png");     //创建图标对象
        buttonNext.setIcon(nextIcon);
        buttonNext.setContentAreaFilled(false);
        buttonNext.addActionListener(actListener);
        buttonNext.addMouseListener(mouListener);
        add(buttonNext);

        buttonPrev=new JButton();
        //buttonStop.setBorder(null);
        buttonPrev.setBounds(470,510,40,40);
        Icon prevIcon=new ImageIcon("Main\\img\\prev.png");     //创建图标对象
        buttonPrev.setIcon(prevIcon);
        buttonPrev.setContentAreaFilled(false);
        buttonPrev.addActionListener(actListener);
        buttonPrev.addMouseListener(mouListener);
        add(buttonPrev);

        listPlayFile.setBounds(880,100,150,150); //设置播放列表大小
        listPlayFile.setOpaque(false);//设置播放列表透明
        listPlayFile.setBackground(new Color(0, 0, 0, 0));//设置播放列表背景颜色
        listPlayFile.setForeground(Color.white);//设置播放列表字体颜色
        listPlayFile.addMouseListener(mouListener);
        listPlayFile.setForeground(Color.BLUE);
        listPlayFile.setFont(new Font("宋体",Font.CENTER_BASELINE,16));
        add(listPlayFile);       //添加播放列表至窗口中

        //文本框
        textLyrics.setBounds(200,20,1080,100);
        textLyrics.setForeground(Color.ORANGE);//字体颜色
        textLyrics.setOpaque(false);//背景透明
        textLyrics.setFont(new Font("宋体",Font.CENTER_BASELINE,24));
        textLyrics.setEditable(false);
        add(textLyrics);
        textLyrics.setText("逆风的方向，更适合飞翔,我不怕千万人阻挡只怕自己投降");

        //进度条
        Icon img2=new ImageIcon("Main\\img\\time.jpg");
        playTime = new JLabel(img2);
        playTime.setBounds(0,485,0,4);
        add(playTime);

        //歌词文本框
        lrcLyrics = new JTextPane();
        lrcLyrics.setBounds(195,400,200,50);
        lrcLyrics.setOpaque(false);
        lrcLyrics.setFont(new Font("宋体",Font.CENTER_BASELINE,18));
        lrcLyrics.setEditable(false);
        lrcLyrics.setText("此处显示歌词");
        add(lrcLyrics);

        timePlne = new JTextPane();
        timePlne.setBounds(970,490,50,50);
        timePlne.setOpaque(false);
        timePlne.setFont(new Font("宋体",Font.CENTER_BASELINE,14));
        timePlne.setEditable(false);
        timePlne.setText("00:00");
        add(timePlne);
    }
}
