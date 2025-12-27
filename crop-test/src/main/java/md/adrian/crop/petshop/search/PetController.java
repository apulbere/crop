package md.adrian.crop.petshop.search;

import lombok.AllArgsConstructor;
import md.adrian.crop.CriteriaOperatorOrder;
import md.adrian.crop.CriteriaOperatorPage;
import md.adrian.crop.petshop.domain.*;
import md.adrian.crop.service.CriteriaOperatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class PetController {

    private final PetMapper petMapper;
    private final CriteriaOperatorService cropService;

    @GetMapping("/pets")
    List<PetRecord> findAll(PetCriteriaOperator searchCriteria, CriteriaOperatorOrder order, CriteriaOperatorPage page) {
        return cropService.create(Pet.class, searchCriteria, order, page)
            .match(Pet_.id, PetCriteriaOperator::getId)
            .match(Pet_.name, PetCriteriaOperator::getNickname)
            .match(Pet_.birthdate, PetCriteriaOperator::getBirthdate)
            .match(Pet_.price, PetCriteriaOperator::getPrice)
            .join(Pet_.features)
                .match(PetFeature_.feature, PetCriteriaOperator::getFeatures)
            .endJoin()
            .match(Pet_.active, PetCriteriaOperator::getActive)
            .join(Pet_.petType)
                .match(PetType_.code, PetCriteriaOperator::getType)
                .join(PetType_.petCategory)
                    .match(PetCategory_.code, PetCriteriaOperator::getCategory)
                .endJoin()
            .endJoin()
            .getResultList()
            .stream()
            .map(petMapper::map)
            .toList();
    }

    @GetMapping("/pets/count")
    Long count(PetCriteriaOperator searchCriteria) {
        return cropService.create(Pet.class, searchCriteria)
                .match(Pet_.id, PetCriteriaOperator::getId)
                .match(Pet_.name, PetCriteriaOperator::getNickname)
                .match(Pet_.birthdate, PetCriteriaOperator::getBirthdate)
                .match(Pet_.price, PetCriteriaOperator::getPrice)
                .join(Pet_.features)
                    .match(PetFeature_.feature, PetCriteriaOperator::getFeatures)
                .endJoin()
                .match(Pet_.active, PetCriteriaOperator::getActive)
                .join(Pet_.petType)
                    .match(PetType_.code, PetCriteriaOperator::getType)
                    .join(PetType_.petCategory)
                        .match(PetCategory_.code, PetCriteriaOperator::getCategory)
                    .endJoin()
                .endJoin()
                .getCount();
    }

}
