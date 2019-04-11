package kr.co.tripmate.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.tripmate.domain.User;
import kr.co.tripmate.domain.UserRepository;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	// private List<User> users = new ArrayList<User>();
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value = "/loginForm")
	public String loginForm() {
		
		return "/user/login";
		
	}
	
	@PostMapping(value = "/login")
	public String login(String userId, String password, HttpSession session) {
		
		User user = userRepository.findByUserId(userId);
		
		if ( user == null ) {
			System.out.println("Login Failure!!");
			return "redirect:/users/loginForm";
		}
		
		if ( !user.isMatchPassword(password) ) {
			System.out.println("Login Failure!!");
			return "redirect:/users/loginForm";
		}
		
		System.out.println("Login Success!!");
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		
		return "redirect:/";
		
	}
	
	@GetMapping(value = "/logout")
	public String logout(HttpSession session) {
		
		session.removeAttribute("sessionUser");
		
		return "redirect:/";
		
	}
	
	@GetMapping(value = "/form")
	public String form() {
		
		return "/user/form";
		
	}
	
	@PostMapping(value = "")
	public String create(User user) {
		
		System.out.println("user: " + user);
		
		// users.add(user);
		userRepository.save(user);
		
		return "redirect:/users";
		
	}
	
	@GetMapping(value = "")
	public String list(Model model) {
		
		model.addAttribute("users", userRepository.findAll());
		
		return "/user/list";
		
	}
	
	@GetMapping(value = "/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		
		// User sessionUser = (User) session.getAttribute("sessionUser");
		
		if ( HttpSessionUtils.isLoginUser(session) ) {
			return "redirect:/users/loginForm";
		}
		
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		
		if ( sessionUser.isMatchId(id) ) {
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
		
		User user = userRepository.findById(id).get();
		
		model.addAttribute("user", user);
		
		return "/user/updateForm";
		
	}
	
	@PutMapping(value = "/{id}")
	public String update(@PathVariable Long id, User user, HttpSession session) {
		
		// User sessionUser = (User) session.getAttribute("sessionUser");
		
		if ( HttpSessionUtils.isLoginUser(session) ) {
			return "redirect:/users/loginForm";
		}
		
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		
		if ( !id.equals(sessionUser.getId()) ) {
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
		
		//User userInfo = userRepository.findById(id).get();
		
		userRepository.save(user);
		
		return "redirect:/users";
		
	}
	
}
