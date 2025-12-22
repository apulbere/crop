package com.apulbere.crop.service;

import com.apulbere.crop.operator.CriteriaOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Base class that links a criteria operator to entity meta-model.
 * @param <ROOT> the root type
 * @param <SEARCH> the type containing criteria operator fields
 */
public abstract class BaseCriteriaOperatorBuilder<ROOT, SEARCH> {

    private final List<Function<CriteriaBuilder, Predicate>> predicates = new LinkedList<>();
    protected final CriteriaBuilder criteriaBuilder;
    protected final SEARCH searchRequest;

    protected BaseCriteriaOperatorBuilder(CriteriaBuilder criteriaBuilder, SEARCH searchRequest) {
        this.criteriaBuilder = criteriaBuilder;
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

    protected void addPredicateSupplier(Function<CriteriaBuilder, Predicate> predicateSupplier) {
        predicates.add(predicateSupplier);
    }

    protected Predicate[] getPredicates(CriteriaBuilder criteriaBuilder) {
        return predicates.stream().map(f -> f.apply(criteriaBuilder)).toArray(Predicate[]::new);
    }

    protected void addAllPredicateSupplier(BaseCriteriaOperatorBuilder<?, ?> other) {
        predicates.addAll(other.predicates);
    }
}
