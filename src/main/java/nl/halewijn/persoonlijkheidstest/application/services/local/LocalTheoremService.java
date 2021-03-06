
package nl.halewijn.persoonlijkheidstest.application.services.local;


import nl.halewijn.persoonlijkheidstest.application.domain.PersonalityType;
import nl.halewijn.persoonlijkheidstest.application.domain.Theorem;
import nl.halewijn.persoonlijkheidstest.application.services.IObjectService;
import nl.halewijn.persoonlijkheidstest.application.services.ITheoremService;
import nl.halewijn.persoonlijkheidstest.datasource.repository.TheoremRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalTheoremService implements IObjectService<Theorem>, ITheoremService {

	@Autowired
	private TheoremRepository theoremRepository;

	@Override
	public Theorem save(Theorem theorem) {
		return theoremRepository.save(theorem);
	}

	@Override
	public void delete(Theorem theorem) {
		theoremRepository.delete(theorem);
	}

	@Override
	public List<Theorem> getAll() {
		return theoremRepository.findAll();
	}
	
	@Override
	public List<Theorem> getAllByPersonalityType(PersonalityType type) {
		return theoremRepository.findByPersonalityType(type);
	}
	
	@Override
	public Theorem getById(int id) {
		return theoremRepository.findByTheoremId(id);
	}

}