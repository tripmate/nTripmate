package kr.co.tripmate.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.tripmate.domain.Answer;
import kr.co.tripmate.domain.AnswerRepository;
import kr.co.tripmate.domain.Question;
import kr.co.tripmate.domain.QuestionRepository;
import kr.co.tripmate.domain.Result;
import kr.co.tripmate.domain.User;

@RestController
@RequestMapping(value = "/api/questions/{questionId}/answers")
public class ApiAnswerController {
	
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;
	
	@PostMapping(value = "")
	public Answer create(@PathVariable Long questionId, String contents, HttpSession session) {
		
		if ( !HttpSessionUtils.isLoginUser(session) ) {
			return null;
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		Question question = questionRepository.findById(questionId).get();
		Answer answer = new Answer(loginUser, question, contents);
		
		return answerRepository.save(answer);
		
	}
	
	@DeleteMapping(value = "/{id}")
	public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
		
		if ( !HttpSessionUtils.isLoginUser(session) ) {
			return Result.fail("로그인해야 합니다.");
		}
		
		Answer answer = answerRepository.findById(id).get();
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		
		if ( !answer.isWriter(loginUser) ) {
			return Result.fail("자신의 글만 삭제할 수 있습니다.");
		}
		
		answerRepository.deleteById(id);
		
		return Result.ok();
		
	}
	
}
