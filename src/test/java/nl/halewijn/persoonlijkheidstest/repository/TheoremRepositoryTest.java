package nl.halewijn.persoonlijkheidstest.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import nl.halewijn.persoonlijkheidstest.Application;
import nl.halewijn.persoonlijkheidstest.domain.PersonalityType;
import nl.halewijn.persoonlijkheidstest.domain.Theorem;
import nl.halewijn.persoonlijkheidstest.services.local.LocalPersonalityTypeService;
import nl.halewijn.persoonlijkheidstest.services.local.LocalTheoremService;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@ActiveProfiles("test")
public class TheoremRepositoryTest {
	
	@Autowired
	private LocalTheoremService localTheoremService;
	
	@Autowired
	private LocalPersonalityTypeService localPersonalityTypeService;
	
	
	@Test
	@Transactional
	public void testGetAll(){
		List<Theorem> newResults = localTheoremService.getAll();
		PersonalityType type = new PersonalityType("Winnaar", "1", "2");
		localPersonalityTypeService.save(type);
		
		
		Theorem newTheorem = new Theorem(type, "Stelling", 1.0, 0, 0, 0);
		localTheoremService.save(newTheorem);
		newResults.add(newTheorem);
		
		List<Theorem> results = localTheoremService.getAll();
		assertEquals(newResults.size(), results.size());	
	}
	
	@Test
	@Transactional
	public void testGetById(){
		PersonalityType type = new PersonalityType("Winnaar", "1", "2");
		localPersonalityTypeService.save(type);
		
		Theorem newTheorem = new Theorem(type, "Stelling", 1.0, 0, 0, 0);
		localTheoremService.save(newTheorem);
		
		assertEquals(newTheorem.getText(), localTheoremService.getById(1).getText());
	}
	
	@Test
	@Transactional
	public void testUpdate(){
		PersonalityType type = new PersonalityType("Winnaar", "1", "2");
		localPersonalityTypeService.save(type);
		
		Theorem newTheorem = new Theorem(type, "Stelling", 1.0, 0, 0, 0);
		localTheoremService.save(newTheorem);
		
		newTheorem.setText("Andere stelling");
		localTheoremService.update(newTheorem);
		
		assertEquals("Andere stelling", newTheorem.getText());	
	}
	
	@Test
	@Transactional
	public void testDelete(){
		PersonalityType type = new PersonalityType("Winnaar", "1", "2");
		localPersonalityTypeService.save(type);
		
		Theorem newTheorem = new Theorem(type, "Stelling", 1.0, 0, 0, 0);
		localTheoremService.save(newTheorem);
		localTheoremService.delete(newTheorem);
		
		assertEquals(null, localTheoremService.getById(newTheorem.getTheoremID()));	
	}
}