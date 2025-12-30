package md.adrian.crop.service;

import md.adrian.crop.operator.CriteriaOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Base class that links a criteria operator to entity metamodel.
 * @param <ROOT> the root type
 * @param <SEARCH> the type containing criteria operator fields
 */
public abstract class BaseCriteriaOperatorBuilder<ROOT, SEARCH> {

    protected final List<BiFunction<CriteriaBuilder, Root<?>, Predicate>> rootPredicates = new LinkedList<>();
    protected final SEARCH searchRequest;

    protected BaseCriteriaOperatorBuilder(SEARCH searchRequest) {
        this.searchRequest = searchRequest;
    }

    /**
     * Matches the attribute with the criteria operator field.
     *
     * @param attribute the attribute of the entity
     * @param criteriaOperatorFunction the function that provides the criteria operator
     * @return the builder
     * @param <SEARCH_FIELD> the criteria operator type
     */
    public abstract <SEARCH_FIELD> BaseCriteriaOperatorBuilder<ROOT, SEARCH> match(
        SingularAttribute<ROOT, SEARCH_FIELD> attribute, Function<SEARCH, ? extends CriteriaOperator<SEARCH_FIELD>> criteriaOperatorFunction
    );

    protected void addRootPredicateSupplier(BiFunction<CriteriaBuilder, Root<?>, Predicate> rootPredicateSupplier) {
        rootPredicates.add(rootPredicateSupplier);
    }

    protected Predicate[] getRootPredicates(CriteriaBuilder criteriaBuilder, Root<?> root) {
        return rootPredicates.stream().map(f -> f.apply(criteriaBuilder, root)).toArray(Predicate[]::new);
    }

    protected void addAllRootPredicateSupplier(BaseCriteriaOperatorBuilder<?, ?> other) {
        rootPredicates.addAll(other.rootPredicates);
    }
}
