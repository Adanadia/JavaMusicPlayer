package com.lrc.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lrc.lrc.LRC;
import com.lrc.tar.impl.TarName;
import com.lrc.tar.impl.id.Al;
import com.lrc.tar.impl.id.Ar;
import com.lrc.tar.impl.id.By;
import com.lrc.tar.impl.id.Offset;
import com.lrc.tar.impl.id.Ti;
import com.lrc.tar.impl.time.TimeTar;

/**
 * 从文件中读取lrc
 * @author asus
 *
 */
public class ReadLRC {
	public static LRC readLRC(String path)  {
		BufferedReader br = null;
		try {
//			br = new BufferedReader(new FileReader(path));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
		StringBuffer sb=new StringBuffer();//为了兼容不是标签开头的行[可能会影响效率]
		Pattern p;
		Matcher m;
		Al al=new Al();
		Ar ar=new Ar();
		By by=new By();
		Offset offset=new Offset();
		Ti ti=new Ti();
		while(true){
			String line=br.readLine(); 
			if(line==null){
				break;
			}
			p=Pattern.compile("\\[\\w*?:.*?\\]");//解析标识标签
			m=p.matcher(line);
			if(m.find()){
				String tar=m.group();
				if(tar.length()>3){//标准标签的必要条件
					if(tar.substring(0,4).equalsIgnoreCase("["+TarName.AL+":")){
						al.setValue(tar.substring(4,tar.length()-1));
					}
					if(tar.substring(0,4).equalsIgnoreCase("["+TarName.AR+":")){
						ar.setValue(tar.substring(4,tar.length()-1));
					}
					if(tar.substring(0,4).equalsIgnoreCase("["+TarName.BY+":")){
						by.setValue(tar.substring(4,tar.length()-1));
					}
					if(tar.substring(0,4).equalsIgnoreCase("["+TarName.TI+":")){
						ti.setValue(tar.substring(4,tar.length()-1));
					}
				}
				if(tar.length()>7){//为时间补偿标签的必要条件
					if(tar.substring(0,8).equalsIgnoreCase("["+TarName.OFFSET+":")){
						offset.setValue(tar.substring(8,tar.length()-1));
					}
				}
			}
			sb.append(line); //为解析时间标签做准备
		}
		p=Pattern.compile("\\[\\d{2,3}:\\d{2}(.\\d{2})?\\]");//解析时间标签[负时间标签暂未处理]
		m=p.matcher(sb);
		m.find();
		int prev = m.end();//上一个匹配的终点 
		String prevGroup=m.group();//上一个匹配的group
		List<TimeTar> timeTars=new ArrayList<TimeTar>();
		while(m.find()){ 
			int start=prev;
			int end=m.start();
			prev=m.end();
			String txt=sb.substring(start,end); 
			TimeTar timeTar=new TimeTar();
			timeTar.setValue(prevGroup);//设置值  [xx:xx.xx]
			timeTar.setText(txt.toString());//设置文本 [xx:xx.xx]后面的东西
			timeTars.add(timeTar);
			prevGroup=m.group(); 
		}
		TimeTar timeTar=new TimeTar();
		timeTar.setValue(prevGroup);//设置值  [xx:xx.xx]
		timeTar.setText(sb.substring(prev,sb.length()));//设置文本 [xx:xx.xx]后面的东西
		timeTars.add(timeTar);
		LRC lrc=new LRC();
		lrc.setAl(al);
		lrc.setAr(ar);
		lrc.setBy(by);
		lrc.setOffset(offset);
		lrc.setTi(ti);
		lrc.setTimeTars(timeTars);
		return lrc;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
