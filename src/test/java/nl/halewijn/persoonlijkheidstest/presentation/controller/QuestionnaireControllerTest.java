package nl.halewijn.persoonlijkheidstest.presentation.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import nl.halewijn.persoonlijkheidstest.Application;
import nl.halewijn.persoonlijkheidstest.application.domain.OpenQuestion;
import nl.halewijn.persoonlijkheidstest.application.domain.PersonalityType;
import nl.halewijn.persoonlijkheidstest.application.domain.Questionnaire;
import nl.halewijn.persoonlijkheidstest.application.services.Constants;
import nl.halewijn.persoonlijkheidstest.application.services.local.LocalPersonalityTypeService;
import nl.halewijn.persoonlijkheidstest.application.services.local.LocalQuestionService;
import nl.halewijn.persoonlijkheidstest.application.services.local.LocalResultService;
import nl.halewijn.persoonlijkheidstest.application.services.local.LocalUserService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Transactional
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@ActiveProfiles("test")
public class QuestionnaireControllerTest {

	@Autowired
    private LocalQuestionService localQuestionService;

    @Autowired
    private LocalPersonalityTypeService localPersonalityTypeService;
    
    @Autowired
    private LocalResultService localResultService;
    
    @Autowired
    private LocalUserService localUserService;
    
    @Autowired
    private QuestionnaireController questionnaireController;
	
	@Test
	public void questionnaireTest() {
		Model model = mock(Model.class);
		HttpSession httpSession = mock(HttpSession.class);
		assertEquals("questionnaire", questionnaireController.questionnaire(model, httpSession));
		when(httpSession.getAttribute("questionnaire")).thenReturn(Constants.questionnaire);
		assertEquals(Constants.questionnaire, questionnaireController.questionnaire(model, httpSession));
		
		when(httpSession.getAttribute(Constants.questionnaire)).thenReturn(new Questionnaire());
		assertEquals(Constants.redirect+"showQuestion", questionnaireController.questionnaire(model, httpSession));
	
		Questionnaire q = new Questionnaire(); 
		q.setTestFinished(true);
		
		when(httpSession.getAttribute(Constants.questionnaire)).thenReturn(q);
		assertEquals(Constants.questionnaire, questionnaireController.questionnaire(model, httpSession));
	}
	
	@Test
	public void showQuestionPOSTTest() {
		Model model = mock(Model.class);
		HttpSession httpSession = mock(HttpSession.class);
		HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
		
		assertEquals("questionnaire", questionnaireController.showQuestionPOST(model, httpSession, httpServletRequest));
		
		questionnaireController.showQuestionPOST(model, httpSession, httpServletRequest);
		assertNull(httpSession.getAttribute("questionnaire"));
		assertEquals(false, model.containsAttribute("currentQuestion"));
		
		Questionnaire questionnaire = new Questionnaire();
		OpenQuestion question = new OpenQuestion("abc");
		localQuestionService.save(question);
		questionnaire.addQuestion(question);
		when(httpSession.getAttribute("questionnaire")).thenReturn(questionnaire);
		
		for(int i = 0; i < 9; i++) {
			PersonalityType type = new PersonalityType("Type", "primary", "secondary");
			localPersonalityTypeService.save(type);
		}
		
		assertEquals(Constants.result, questionnaireController.showQuestionPOST(model, httpSession, httpServletRequest));
	}
	
	@Test
	public void showQuestionGETTest() {
		Model model = mock(Model.class);
		HttpSession httpSession = mock(HttpSession.class);
		
		assertEquals(Constants.questionnaire, questionnaireController.showQuestionGET(model, httpSession));
		Questionnaire questionnaire = new Questionnaire();
		OpenQuestion question = new OpenQuestion("Open Question");
		questionnaire.addQuestion(question);
		
		when(httpSession.getAttribute("questionnaire")).thenReturn(questionnaire);
		questionnaireController.showQuestionGET(model, httpSession);
		assertEquals(false, model.containsAttribute("currentQuestion"));
		assertEquals(Constants.questionnaire, questionnaireController.showQuestionGET(model, httpSession));
	}
	
	@Test
	public void showLinksTest() {
		Model model = mock(Model.class);
		PersonalityType typeOne = new PersonalityType("Type 1", "prim", "sec");
		localPersonalityTypeService.save(typeOne);
		HttpSession httpSession = mock(HttpSession.class);
		assertEquals(Constants.linkPage, questionnaireController.showLinks(model, httpSession));
	}

}
