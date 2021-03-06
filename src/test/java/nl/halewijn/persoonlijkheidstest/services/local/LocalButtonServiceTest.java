package nl.halewijn.persoonlijkheidstest.services.local;

import nl.halewijn.persoonlijkheidstest.Application;
import nl.halewijn.persoonlijkheidstest.application.domain.Button;
import nl.halewijn.persoonlijkheidstest.application.services.local.LocalButtonService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@Transactional
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@ActiveProfiles("test")
public class LocalButtonServiceTest {

    @Autowired
	private LocalButtonService localButtonService;
	
	@Test
	public void getByButtonIdTest() {
        String buttonText = "Send";
        String buttonDescription = "A button to submit the form";
        Button button = new Button();
        button.setButtonDescription(buttonDescription);
        button.setButtonText(buttonText);
		button = localButtonService.save(button);
        button = localButtonService.getByButtonId(button.getButtonId());
        assertEquals(buttonDescription, button.getButtonDescription());
	}  
}