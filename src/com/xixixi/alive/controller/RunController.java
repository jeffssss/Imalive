package com.xixixi.alive.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.xixixi.alive.model.Aliveuser;
import com.xixixi.alive.model.World;
import com.xixixi.alive.util.MemClient;

public class RunController extends Controller {
	public void index(){
		render("run.html"); 
	}
	
	public void run(){
		Object obj = getPara("token");
		Object obj2 = getPara("counter");
		Gson gson = new Gson();
		Map<String, String> map  =new HashMap<String, String>();
		//判断token是否为null
		if(obj==null||obj.equals("")){
			map.put("state", "-1");
			map.put("error", "token null value");
			renderJson(gson.toJson(map));
			return;
		}
		String token = (String)obj;
		//如果token找不到我不知道会出现什么情况。
		long distance =MemClient.getInstance().increment(token, 1);
		long worldDistance = MemClient.getInstance().increment("world_distance", 1);
		//world信息定时写入数据库。
		if(worldDistance%1000==0){
			World world = new World();
			world.set("distance", worldDistance);
			world.set("alivenum", MemClient.getInstance().get("world_alivenum"));
			world.set("time", new Date());
			world.save();
		}
		if(distance<0){
			map.put("state", "-1");
			map.put("error", "token not found in memcache.");
			renderJson(gson.toJson(map));
			return;
		}
		//找到counter，定时更新数据库
		if(obj2!=null&&(!obj2.equals(""))){
			int counter =0;
			try {
				counter = Integer.parseInt((String)obj2);
			} catch (Exception e) {
				counter = -1;
			}
			if(counter%100 ==0){
				Aliveuser user = Aliveuser.dao.findFirst("select uid  from aliveuser where token=?",token);
				user.set("udistance", distance);
				user.update();
			}
		}
		
		map.put("state", "1");
		map.put("distance", distance+"");
		map.put("world_distance", ""+worldDistance);
		map.put("world_alivenum", MemClient.getInstance().get("world_alivenum")+"");
		renderJson(gson.toJson(map));
		return;
	}
	
	//TODO:差个注销/退出的方法。退出后可以持久化数据
}
