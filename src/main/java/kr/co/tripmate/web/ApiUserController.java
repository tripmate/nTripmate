package kr.co.tripmate.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.tripmate.domain.User;
import kr.co.tripmate.domain.UserRepository;

@RestController
@RequestMapping(value = "/api/users")
public class ApiUserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value = "/{id}")
	public User read(@PathVariable Long id) {
		
		return userRepository.findById(id).get();
		
	}
	
}
