package kr.co.tripmate.web;

import javax.servlet.http.HttpSession;

import kr.co.tripmate.domain.User;

public class HttpSessionUtils {

	public static final String USER_SESSION_KEY = "sessionUser";
	
	public static boolean isLoginUser(HttpSession session) {
		
		User sessionUser = (User) session.getAttribute(USER_SESSION_KEY);
		
		if ( sessionUser == null ) {
			return false;
		}
		
		return true;
		
	}
	
	public static User getUserFromSession(HttpSession session) {
		
		if ( !isLoginUser(session) ) {
			return null;
		}
		
		return (User) session.getAttribute(USER_SESSION_KEY);
		
	}
	
}
