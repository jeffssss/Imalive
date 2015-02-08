package com.xixixi.alive.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

public class SessionInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		// 判断session是否存在
		Object user = ai.getController().getSessionAttr("user");
		if (user == null) {
			ai.getController().render("login.html");
		}else{
			ai.invoke();
		}
		
	}

}
