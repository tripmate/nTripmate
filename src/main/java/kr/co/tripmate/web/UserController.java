package kr.co.tripmate.web;

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
	public String updateForm(@PathVariable Long id, Model model) {
		
		User user = userRepository.findById(id).get();
		
		model.addAttribute("user", user);
		
		return "/user/updateForm";
		
	}
	
	@PutMapping(value = "/{id}")
	public String update(@PathVariable Long id, User user) {
		
		//User userInfo = userRepository.findById(id).get();
		
		userRepository.save(user);
		
		return "redirect:/users";
		
	}
	
}
