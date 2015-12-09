package nl.halewijn.persoonlijkheidstest.services.local;

import nl.halewijn.persoonlijkheidstest.datasource.repository.RoutingRuleRepository;
import nl.halewijn.persoonlijkheidstest.datasource.repository.RoutingTableRepository;
import nl.halewijn.persoonlijkheidstest.datasource.repository.UserRepository;
import nl.halewijn.persoonlijkheidstest.domain.Question;
import nl.halewijn.persoonlijkheidstest.domain.RoutingRule;
import nl.halewijn.persoonlijkheidstest.domain.RoutingTable;
import nl.halewijn.persoonlijkheidstest.domain.User;
import nl.halewijn.persoonlijkheidstest.services.IObjectService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalRoutingService {

	@Autowired
	private RoutingTableRepository routingTableRepository;

    public RoutingTable findById(int tableId) {
        return routingTableRepository.findByRouteId(tableId);
    }

    public List<RoutingTable> getRoutingRulesByQuestion(Question question) {
        return routingTableRepository.findByQuestion(question);
    }

}