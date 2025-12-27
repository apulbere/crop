package md.adrian.crop.petshop.search;

import lombok.Getter;
import lombok.Setter;
import md.adrian.crop.operator.*;

@Getter
@Setter
public class PetCriteriaOperator {

    private LongCriteriaOperator id;
    private StringCriteriaOperator nickname;
    private StringCriteriaOperator type;
    private StringCriteriaOperator category;
    private LocalDateCriteriaOperator birthdate;
    private BigDecimalCriteriaOperator price;
    private BooleanCriteriaOperator active;
    private StringCriteriaOperator features;
}
