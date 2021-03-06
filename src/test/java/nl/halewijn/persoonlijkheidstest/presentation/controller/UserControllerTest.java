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
import nl.halewijn.persoonlijkheidstest.application.domain.*;
import nl.halewijn.persoonlijkheidstest.application.services.Constants;
import nl.halewijn.persoonlijkheidstest.application.services.PasswordHash;
import nl.halewijn.persoonlijkheidstest.application.services.local.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Transactional
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@ActiveProfiles("test")
public class UserControllerTest {
	
	@Autowired
    private LocalUserService localUserService;
	
	@Autowired
	private LocalPersonalityTypeService localPersonalityTypeService;
	
	@Autowired
	private LocalTheoremService localTheoremService;
	
	@Autowired
	private LocalResultService localResultService;
	
	@Autowired
	private UserController userController;

    @Autowired
    private LocalWebsiteContentTextService localWebsiteContentTextService;
	
	private PasswordHash passwordHash = new PasswordHash();
	
	@Test
	public void myResultsTest() {
		Model model = mock(Model.class);
		HttpSession session = mock(HttpSession.class);
		HttpServletRequest req = mock(HttpServletRequest.class);
		
		assertEquals(Constants.redirect, userController.myResults(model, session, req));
		
		when(session.getAttribute("email")).thenReturn("");
		assertEquals(Constants.redirect, userController.myResults(model, session, req));
		
		when(session.getAttribute("email")).thenReturn("email@mail.com");
		User user = new User("email@mail.com", false);
		user.setPasswordHash(passwordHash.hashPassword("aa"));
		localUserService.save(user);
		assertEquals(Constants.myResults, userController.myResults(model, session, req));
	}
	
	@Test
	public void showResultGETTest() {
		Model model = mock(Model.class);
		HttpSession session = mock(HttpSession.class);
		HttpServletRequest req = mock(HttpServletRequest.class);
		
		assertEquals(Constants.redirect, userController.showResultGET(model, session, req));
	}
	
	@Test
	public void showResultTest() {
		Model model = mock(Model.class);
		HttpSession session = mock(HttpSession.class);
		HttpServletRequest req = mock(HttpServletRequest.class);
		
		when(req.getParameter("number")).thenReturn(Integer.toString(0));
		assertEquals(Constants.redirect, userController.showResult(model, session, req));
		
		Result result1 = new Result(null);
		localResultService.saveResult(result1);
		when(req.getParameter("number")).thenReturn(Integer.toString(result1.getId()));
		assertEquals(Constants.redirect, userController.showResult(model, session, req));
		
		User user1 = new User("test@email.eu", false);
		setUserPassword(user1);
		localUserService.save(user1);
		Result result2 = new Result(user1);
        when(session.getAttribute("email")).thenReturn("");
        assertEquals(Constants.redirect, userController.showResult(model, session, req));
        when(session.getAttribute("email")).thenReturn(null);
		localResultService.saveResult(result2);
		when(req.getParameter("number")).thenReturn(Integer.toString(result2.getId()));
		assertEquals(Constants.redirect, userController.showResult(model, session, req));
		
		User user2 = new User("duncan@email.eu", true);
		setUserPassword(user2);
		localUserService.save(user2);
		when(session.getAttribute("email")).thenReturn(user2.getEmailAddress());
		Result result3 = new Result(user2);
		result3.setScoreDenial(1);
		result3.setScoreRecognition(2);
		result3.setScoreDevelopment(3);
		ArrayList<Answer> testResultAnswers = new ArrayList<>();
		PersonalityType type1 = new PersonalityType("Type1", "PrimaryDescr", "SecondaryDescr");
		PersonalityType type2 = new PersonalityType("Type2", "PrimaryDescr", "SecondaryDescr");
		localPersonalityTypeService.save(type1);
		localPersonalityTypeService.save(type2);
		Theorem theorem1 = new Theorem(type1, "text1", 1, 1, 1, 1);
		Theorem theorem2 = new Theorem(type2, "text2", 1, 1, 1, 1);
		localTheoremService.save(theorem1);
		localTheoremService.save(theorem2);
		Question question = new TheoremBattle("Text", theorem1, theorem2);
		Answer answer = new Answer(question, "A", new Date());
		testResultAnswers.add(answer);
		result3.setTestResultAnswers(testResultAnswers);
		localResultService.saveResult(result3);
		ResultTypePercentage resultTypePercentage = new ResultTypePercentage(result3, type1, 100);
		localResultService.saveResultTypePercentage(resultTypePercentage);
		when(req.getParameter("number")).thenReturn(Integer.toString(result3.getId()));
		assertEquals(Constants.result, userController.showResult(model, session, req));
		when(session.getAttribute(Constants.email)).thenReturn("wrong@email.eu");
		assertEquals(Constants.redirect, userController.showResult(model, session, req));
	}
	
	private void setUserPassword(User user) {
		String password = "x"; // Plaintext password
		String passwordHash = new PasswordHash().hashPassword(password); // Hashed password
		password = null; // Prepare plaintext password for clearing from memory by the Java garbage collector.
		user.setPasswordHash(passwordHash); // Stored hash in user
	}

	@Test
	public void setResultPrimaryTypeTest() {
        User user = new User("a@a.com", false);
        user.setPasswordHash("x");
        localUserService.save(user);

		Result result1 = new Result(user);
		Result result2 = new Result(user);
		localResultService.saveResult(result1);
		localResultService.saveResult(result2);

        PersonalityType type1 = new PersonalityType("Type 1", "Primary description", "Secondary description");
        PersonalityType type2 = new PersonalityType("Type 2", "Primary description", "Secondary description");
        localPersonalityTypeService.save(type1);
        localPersonalityTypeService.save(type2);

        List<PersonalityType> personalityTypes = localPersonalityTypeService.getAll();

		ResultTypePercentage result1TypePercentage1 = new ResultTypePercentage(result1, personalityTypes.get(0), 50.0);
		ResultTypePercentage result1TypePercentage2 = new ResultTypePercentage(result1, personalityTypes.get(1), 75.0);
		ResultTypePercentage result2TypePercentage1 = new ResultTypePercentage(result2, personalityTypes.get(0), 80.0);
		ResultTypePercentage result2TypePercentage2 = new ResultTypePercentage(result2, personalityTypes.get(1), 60.0);
		localResultService.saveResultTypePercentage(result1TypePercentage1);
		localResultService.saveResultTypePercentage(result1TypePercentage2);
		localResultService.saveResultTypePercentage(result2TypePercentage1);
		localResultService.saveResultTypePercentage(result2TypePercentage2);

		List<Result> userResults = new ArrayList<>();
		userResults.add(result1);
		userResults.add(result2);

		userController.setResultPrimaryType(userResults);

		PersonalityType result1ActualPrimaryType = userResults.get(0).getPrimaryType();
		PersonalityType result2ActualPrimaryType = userResults.get(1).getPrimaryType();

		assertEquals(type2, result1ActualPrimaryType);
		assertEquals(type1, result2ActualPrimaryType);
	}

    @Test
    public void contactTest() {
        Model model = mock(Model.class);

        WebsiteContentText text7 = new WebsiteContentText(7);
		text7.setContentText("");
        localWebsiteContentTextService.save(text7);

        assertEquals("contact", userController.contact(model));
        when(model.containsAttribute("SeventhContentBox")).thenReturn(true);
        assertTrue(model.containsAttribute("SeventhContentBox"));
        when(model.containsAttribute("FirstImage")).thenReturn(true);
        assertTrue(model.containsAttribute("FirstImage"));
    }

    @Test
    public void aboutUsTest() {
        Model model = mock(Model.class);

        WebsiteContentText text8 = new WebsiteContentText(8);
		text8.setContentText("");
        localWebsiteContentTextService.save(text8);

        assertEquals("aboutUs", userController.aboutUs(model));
        when(model.containsAttribute("EigthContentBox")).thenReturn(true);
        assertTrue(model.containsAttribute("EigthContentBox"));
        when(model.containsAttribute("FirstImage")).thenReturn(true);
        assertTrue(model.containsAttribute("FirstImage"));
    }

	@Test
	public void changeEmailTest() {
		Model model = mock(Model.class);
		HttpSession session = mock(HttpSession.class);
		HttpServletRequest req = mock(HttpServletRequest.class);

		when(session.getAttribute(Constants.email)).thenReturn(null);
		assertEquals(Constants.redirect, userController.changeEmail(model, session, req));

		when(session.getAttribute(Constants.email)).thenReturn("email@mail.com");
		assertEquals("changeEmail", userController.changeEmail(model, session, req));
	}

    @Test
    public void changeEmailCheckTest() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest req = mock(HttpServletRequest.class);

        String email = "email@mail.com";
        String newEmail = "email@email.com";
        String password = "abc";
        User user = new User(email, false);
        user.setPasswordHash(new PasswordHash().hashPassword(password));
        user = localUserService.save(user);

        when(session.getAttribute(Constants.email)).thenReturn(null);
        assertEquals(Constants.redirect, userController.changeEmailCheck(model, session, req));

        when(session.getAttribute(Constants.email)).thenReturn("");
        assertEquals(Constants.redirect, userController.changeEmailCheck(model, session, req));

        when(session.getAttribute(Constants.email)).thenReturn(email);
        when(req.getParameter(Constants.password)).thenReturn("a");
        assertEquals(Constants.redirect + "changeEmail?attempt=wrong", userController.changeEmailCheck(model, session, req));

        when(req.getParameter(Constants.password)).thenReturn(password);
        when(req.getParameter(Constants.email)).thenReturn(null);
        assertEquals(Constants.redirect + "changeEmail?attempt=empty", userController.changeEmailCheck(model, session, req));

        when(req.getParameter(Constants.email)).thenReturn("");
        assertEquals(Constants.redirect + "changeEmail?attempt=empty", userController.changeEmailCheck(model, session, req));

        when(req.getParameter(Constants.email)).thenReturn(email);
        assertEquals(Constants.redirect + "changeEmail?attempt=wrong", userController.changeEmailCheck(model, session, req));

        when(req.getParameter(Constants.email)).thenReturn(newEmail);
        assertEquals(Constants.redirect + "logOut", userController.changeEmailCheck(model, session, req));

        localUserService.delete(user);
        assertEquals(Constants.redirect, userController.changeEmailCheck(model, session, req));
    }

	@Test
	public void changePasswordTest() {
		Model model = mock(Model.class);
		HttpSession session = mock(HttpSession.class);
		HttpServletRequest req = mock(HttpServletRequest.class);

		when(session.getAttribute(Constants.email)).thenReturn(null);
		assertEquals(Constants.redirect, userController.changePassword(model, session, req));

		when(session.getAttribute(Constants.email)).thenReturn("email@mail.com");
		assertEquals("changePassword", userController.changePassword(model, session, req));
	}

    @Test
    public void changePasswordCheckTest() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest req = mock(HttpServletRequest.class);

        String email = "email@mail.com";
        String password = "abc";
        String newPassword = "abcdefg123";
        String newPasswordTooShort = "abcd";
        User user = new User(email, false);
        user.setPasswordHash(new PasswordHash().hashPassword(password));
        user = localUserService.save(user);

        when(session.getAttribute(Constants.email)).thenReturn(null);
        assertEquals(Constants.redirect, userController.changePasswordCheck(model, session, req));

        when(session.getAttribute(Constants.email)).thenReturn("");
        assertEquals(Constants.redirect, userController.changePasswordCheck(model, session, req));

        when(session.getAttribute(Constants.email)).thenReturn(email);
        when(req.getParameter("oldPassword")).thenReturn("a");
        assertEquals(Constants.redirect + "changePassword?attempt=mismatch", userController.changePasswordCheck(model, session, req));

        when(req.getParameter("oldPassword")).thenReturn(password);
        when(req.getParameter("newPassword")).thenReturn("abcdefg12");
        when(req.getParameter("newPassword2")).thenReturn(newPassword);
        assertEquals(Constants.redirect + "changePassword?attempt=mismatch", userController.changePasswordCheck(model, session, req));

        when(req.getParameter("newPassword")).thenReturn(newPasswordTooShort);
        when(req.getParameter("newPassword2")).thenReturn(newPasswordTooShort);
        assertEquals(Constants.redirect + "changePassword?attempt=length", userController.changePasswordCheck(model, session, req));

        when(req.getParameter("newPassword")).thenReturn(newPassword);
        when(req.getParameter("newPassword2")).thenReturn(newPassword);
        assertEquals(Constants.redirect + "logOut", userController.changePasswordCheck(model, session, req));

        localUserService.delete(user);
        assertEquals(Constants.redirect, userController.changePasswordCheck(model, session, req));
    }

}