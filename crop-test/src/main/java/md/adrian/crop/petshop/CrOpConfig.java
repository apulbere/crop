package md.adrian.crop.petshop;

import jakarta.persistence.EntityManager;
import md.adrian.crop.service.CriteriaOperatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrOpConfig {

    @Bean
    CriteriaOperatorService criteriaOperatorService(EntityManager entityManager) {
        return new CriteriaOperatorService(entityManager);
    }
}
