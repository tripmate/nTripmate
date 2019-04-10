package kr.co.tripmate.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.tripmate.domain.QuestionRepository;

@Controller
public class HomeController {

	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping(value = "")
	public String home(Model model) {
		
		model.addAttribute("questions", questionRepository.findAll());
		
		return "index";
		
	}
	
}
