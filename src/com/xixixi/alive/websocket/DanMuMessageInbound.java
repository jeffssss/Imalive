package com.xixixi.alive.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

public class DanMuMessageInbound extends MessageInbound {
	
	 private int userIdName = 0;
	 

	 public int getUserIdName() {
		 return userIdName;
	 }
	    protected void onOpen(WsOutbound outbound) {
	        super.onOpen(outbound);
	        userIdName = outbound.hashCode();
	        DanMuSocket.getSocketList().add(this);
	    }
	    
	    protected void onClose(int status) {
	    	DanMuSocket.getSocketList().remove(this);
	        super.onClose(status);

	    }
	    
	@Override
	protected void onBinaryMessage(ByteBuffer buffer) throws IOException {
		System.out.println("Binary Message Receive: "+buffer.remaining());
	}

	@Override
	protected void onTextMessage(CharBuffer buffer) throws IOException {
		String msgOriginal = buffer.toString();

        int startIndex = msgOriginal.indexOf("!@#$%");
        String nikeName = msgOriginal.substring(0, startIndex);
        String textMsg = msgOriginal.substring(startIndex + 5);
        // 将字符数组包装到缓冲区中
        // 给定的字符数组将支持新缓冲区；即缓冲区修改将导致数组修改，反之亦然

        String countMsg = DanMuSocket.getSocketList().size() + "人同时在线";
        System.out.println("Server onTextMessage: " + countMsg + nikeName + ":"
                + textMsg);

        String msg1 = nikeName + ": " + textMsg;
        String msg2 = "我: " + textMsg;

        for (DanMuMessageInbound messageInbound : DanMuSocket.getSocketList()) {
            CharBuffer msgBuffer1 = CharBuffer.wrap(msg1);
            CharBuffer msgBuffer2 = CharBuffer.wrap(msg2);

            WsOutbound outbound = messageInbound.getWsOutbound();
            if (messageInbound.getUserIdName() != this.getUserIdName()) {
                outbound.writeTextMessage(msgBuffer1);
                outbound.flush();
            } else {
                outbound.writeTextMessage(msgBuffer2);
            }
        }

	}

}
