package com.xixixi.alive.controller;

import java.util.Date;
import java.util.List;

import com.jfinal.core.Controller;
import com.xixixi.alive.model.Aliveuser;
import com.xixixi.alive.util.MD5Util;
import com.xixixi.alive.util.MemClient;
import com.xixixi.alive.util.TokenUtil;

public class LoginController extends Controller {

	public void index(){
		render("index.html");
	}
	
	public void newrunner(){
		setAttr("error", "");
		String name = getPara("name", "");
		if(Aliveuser.dao.find("select * from aliveuser where uname=?",name).size()!=0){
			setAttr("error", "昵称已存在");
			render("index.html");
			return;
		}
		if(name.equals("")||name==""){
			setAttr("error", "输入空值");
			render("index.html");
			return;
		}
		Aliveuser newuser = new Aliveuser();
		String uuid = MD5Util.randomUUID();
		while(Aliveuser.dao.find("select * from aliveuser where uid=?",uuid).size()!=0){
			uuid = MD5Util.randomUUID();
		}
		newuser.set("uid", uuid);
		newuser.set("uname", name);
		newuser.set("udistance", 0);
		newuser.set("uregtime", new Date());
		newuser.set("token", TokenUtil.createToken(name));
		if(newuser.save()){
			setSessionAttr("user", newuser);
			boolean a = MemClient.client.add(newuser.getStr("token"), 0,0);
			//System.out.println("add  key=token :" + a);
			long b = MemClient.client.incr("world_alivenum", 1);
			//System.out.println("incr key = world_alivenum:"+b);
			redirect("/run");
			//render("debug.html");
		}
		else{
			setAttr("error", "创建用户失败");
			render("index.html");
			return;
		}
	}
	
	public void goon(){
		setAttr("error", "");
		String token = getPara("token", "");
		List<Aliveuser> result = Aliveuser.dao.find("select * from aliveuser where token=?",token);
		if(result.isEmpty()){
			setAttr("error", "无效的token");
			render("index.html");
			return;
		}
		else{
			setSessionAttr("user", result.get(0));
			boolean a = MemClient.client.set(result.get(0).getStr("token"), result.get(0).get("udistance"),0);
			//System.out.println("set key=token:"+a);
			long b = MemClient.client.incr("world_alivenum", 1);
			//System.out.println("incr key=world_alivenum:"+b);
			long c = MemClient.client.incr("world_distance", 1);
			//System.out.println("incr key=world_distance:"+c);
			
			redirect("/run");
			//render("debug.html");
			return;
		}
	}
	public void readToken(){
		Object obj =getSessionAttr("user");
		if(obj==null){
			redirect("/");
		}else{
			Aliveuser user = (Aliveuser)obj;
			renderText(user.getStr("token"));
		}
	}
}
