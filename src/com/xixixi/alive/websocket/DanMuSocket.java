package com.xixixi.alive.websocket;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class DanMuSocket extends WebSocketServlet {
	private static List<DanMuMessageInbound> socketList = new ArrayList<DanMuMessageInbound>();
	
	   @Override
	    protected StreamInbound createWebSocketInbound(String subProtocol,
	            HttpServletRequest request) {
	        // TODO Auto-generated method stub
	        return new DanMuMessageInbound();
	    }
	   
	    public static synchronized List<DanMuMessageInbound> getSocketList() {
	        return socketList;
	    }
}
