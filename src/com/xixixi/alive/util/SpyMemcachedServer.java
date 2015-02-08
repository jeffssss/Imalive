package com.xixixi.alive.util;

/**
 * from :http://blog.csdn.net/gtuu0123/article/details/4849905
 * 考虑到以后可用会拓展到多台memcache。就保留了这个类
 * @author Administrator
 *
 */
public class SpyMemcachedServer {  
    
    private String ip;  
    private int port;  
      
    public void setIp(String ip) {  
        this.ip = ip;  
    }  
      
    public String getIp() {  
        return ip;  
    }  
      
    public void setPort(int port) {  
        if (port < 0 || port > 65535) {  
            throw new IllegalArgumentException("Port number must be between 0 to 65535");  
        }  
        this.port = port;  
    }  
      
    public int getPort() {  
        return port;  
    }  
      
    public String toString() {  
        return getIp() + ":" + getPort();  
    }  
}  