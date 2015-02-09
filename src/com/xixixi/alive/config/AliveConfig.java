package com.xixixi.alive.config;

import java.io.IOException;
import java.util.Date;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.test.MemcachedLongTest;
import com.xixixi.alive.controller.LoginController;
import com.xixixi.alive.controller.RunController;
import com.xixixi.alive.model.Aliveuser;
import com.xixixi.alive.model.World;
import com.xixixi.alive.util.MemClient;
import com.xixixi.alive.util.SpyMemcachedServer;

public class AliveConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		//loadPropertyFile("saeconfig.txt");
		loadPropertyFile("a_little_config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		me.setMainRenderFactory(new BeetlRenderFactory());

	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", LoginController.class);
		me.add("/run", RunController.class,"/runpage");
		me.add("/login", LoginController.class,"/");
	}

	@Override
	public void configPlugin(Plugins me) {
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(c3p0Plugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		//arp.addMapping("blog", Blog.class);	// 映射blog 表到 Blog模型
		arp.addMapping("aliveuser","uid", Aliveuser.class);
		arp.addMapping("world", "version",World.class);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("base"));//全局路径base。
	}

	public static void main(String[] args) {
		JFinal.start("WebRoot", 20202, "/", 5);
	}

	@Override
	public void afterJFinalStart() {
		//启动memcache 
/*		spymemcached incr出错 暂时还是用sae的memcached
 * 		SpyMemcachedServer server = new SpyMemcachedServer();
		server.setIp("localhost");
		server.setPort(11211);
		MemClient.getInstance().setServer(server);
		try {
			MemClient.getInstance().connect();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		MemClient.client.init();
		World world = World.dao.findFirst("select * from world  order by version desc");
		MemClient.client.set("world_distance", world.get("distance"),0);
		MemClient.client.set("world_alivenum", world.get("alivenum"),0);
	}

	@Override
	public void beforeJFinalStop() {
		World world = new World();
		world.set("distance", MemClient.client.get("world_distance"));
		world.set("alivenum", MemClient.client.get("world_alivenum"));
		world.set("time", new Date());
		world.save();
	}
	
}
