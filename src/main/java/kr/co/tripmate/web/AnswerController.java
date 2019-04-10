package kr.co.tripmate.web;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.tripmate.domain.Answer;
import kr.co.tripmate.domain.AnswerRepository;
import kr.co.tripmate.domain.Question;
import kr.co.tripmate.domain.QuestionRepository;
import kr.co.tripmate.domain.User;

@Controller
@RequestMapping(value = "/questions/{questionId}/answers")
@Transactional
public class AnswerController {
	
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;
	
	@PostMapping(value = "")
	public String create(@PathVariable Long questionId, String contents, HttpSession session) {
		
		if ( !HttpSessionUtils.isLoginUser(session) ) {
			return "redirect:/users/loginForm";
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		Question question = questionRepository.findById(questionId).get();
		Answer answer = new Answer(loginUser, question, contents);
		
		answerRepository.save(answer);
		
		return String.format("redirect:/questions/%d", questionId);
		
	}
	
}
