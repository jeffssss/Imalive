package com.test;

import java.io.IOException;

import com.xixixi.alive.util.MemClient;
import com.xixixi.alive.util.SpyMemcachedServer;

public class MemcachedLongTest {

	public static void main(String[] args) {
		//启动memcache 
/*				SpyMemcachedServer server = new SpyMemcachedServer();
				server.setIp("localhost");
				server.setPort(11211);
				MemClient.getInstance().setServer(server);*/
/*				try {
					MemClient.getInstance().connect();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				MemClient.client.init();
				int a = 44447;
				System.out.println(MemClient.client.set("world-recordd", 1, 0)+"");
				long b = MemClient.client.incr("world-recordd", 1);
				System.out.println(b+"");
				

	}

}
