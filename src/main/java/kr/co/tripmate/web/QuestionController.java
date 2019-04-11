package kr.co.tripmate.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.tripmate.domain.Question;
import kr.co.tripmate.domain.QuestionRepository;
import kr.co.tripmate.domain.Result;
import kr.co.tripmate.domain.User;

@Controller
@RequestMapping(value = "/questions")
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping(value = "/form")
	public String form(HttpSession session) {
		
		if ( !HttpSessionUtils.isLoginUser(session) ) {
			return "/users/loginForm";
		}
		
		return "/qna/form";
		
	}
	
	@PostMapping(value = "")
	public String create(String title, String contents, HttpSession session) {
		
		if ( !HttpSessionUtils.isLoginUser(session) ) {
			return "/users/loginForm";
		}
		
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		
		Question question = new Question(sessionUser, title, contents);
		
		questionRepository.save(question);
		
		return "redirect:/";
		
	}
	
	@GetMapping(value = "/{id}")
	public String read(@PathVariable Long id, Model model) {
		
		Question question = questionRepository.findById(id).get();
		
		model.addAttribute("question", question);
		
		return "/qna/show";
		
	}
	
	@GetMapping(value = "/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		
		Question question = questionRepository.findById(id).get();
		
		Result result = valid(session, question);
		
		if ( !result.isValid() ) {
			
			model.addAttribute("errorMessage",result.getErrorMessage());
			
			return "/user/login";
			
		}
			
		model.addAttribute("question", question);
		
		return "/qna/updateForm";
		
	}
	
	public Result valid(HttpSession session, Question question) {
		
		if ( !HttpSessionUtils.isLoginUser(session) ) {
			return Result.fail("로그인이 필요합니다.");
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		if ( !question.isWriter(loginUser) ) {
			return Result.fail("자신이 쓴 글만 수정, 삭제가 가능합니다.");
		}
		
		return Result.ok();
		
	}
	
	public void checkPermission(HttpSession session, Question question) {
		
		if ( !HttpSessionUtils.isLoginUser(session) ) {
			throw new IllegalStateException("로그인이 필요합니다.");
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		if ( !question.isWriter(loginUser) ) {
			throw new IllegalStateException("자신이 쓴 글만 수정, 삭제가 가능합니다.");
		}
		
	}
	
	@PutMapping(value = "/{id}")
	public String update(@PathVariable Long id, String title, String contents, Model model, HttpSession session) {
		
		Question question = questionRepository.findById(id).get();
		
		Result result = valid(session, question);
		
		if ( !result.isValid() ) {
			
			model.addAttribute("errorMessage",result.getErrorMessage());
			
			return "/user/login";
			
		}
		
		question.update(title, contents);
		
		questionRepository.save(question);
		
		return String.format("redirect:/questions/%d", id);
		
	}
	
	@DeleteMapping(value = "/{id}")
	public String delete(@PathVariable Long id, Model model, HttpSession session) {
		
		Question question = questionRepository.findById(id).get();
		
		Result result = valid(session, question);
		
		if ( !result.isValid() ) {
			
			model.addAttribute("errorMessage",result.getErrorMessage());
			
			return "/user/login";
			
		}
		
		questionRepository.deleteById(id);
		
		return "redirect:/";
		
	}
	
}
