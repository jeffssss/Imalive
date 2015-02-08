package com.xixixi.alive.util;

import java.util.ArrayList;
import java.util.List;

public class TokenUtil {
	public static String createToken(String name){
		
		String[] addition1 = {"想吃","喜欢","正在","有点"};
		String[] item1 = {"冰激凌","大鸡腿","麻辣烫","甜筒","火锅"};
		String[] item2 = {"看电影","爬楼梯","发呆","做题目","打代码"};
		String[] item3 = {"自习","睡觉","做作业","玩电脑","装高冷"};
		String[] item4 = {"逗","开心","忧郁","花痴","寂寞"};
		Object[] addition2 = {item1,item2,item3,item4};
		int first = (int) (Math.random()*1000 % 4);
		int second = (int)(Math.random()*1000 % 5);
		return name+addition1[first]+((String[])addition2[first])[second];
		
	}
}
