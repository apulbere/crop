package com.apulbere.crop.service;

import com.apulbere.crop.operator.CriteriaOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@inheritDoc}
 * A criteria operator builder with the focus on joins.
 *
 * @param <ROOT>        the root type
 * @param <SEARCH>      the type containing criteria operator fields
 * @param <PARENT_ROOT> the parent root type
 * @param <PARENT>      the parent criteria operator builder
 */
public class JoinCriteriaOperatorBuilder<ROOT, SEARCH, PARENT_ROOT, PARENT extends BaseCriteriaOperatorBuilder<PARENT_ROOT, SEARCH>>
        extends BaseCriteriaOperatorBuilder<ROOT, SEARCH> {

    private final Supplier<Join<PARENT_ROOT, ROOT>> joinRootSupplier;
    private final PARENT parent;

    JoinCriteriaOperatorBuilder(
            CriteriaBuilder criteriaBuilder,
            Supplier<Join<PARENT_ROOT, ROOT>> joinRootSupplier,
            SEARCH searchRequest,
            PARENT parent
    ) {
        super(criteriaBuilder, searchRequest);
        this.joinRootSupplier = joinRootSupplier;
        this.parent = parent;
    }

    @Override
    public <SEARCH_FIELD> JoinCriteriaOperatorBuilder<ROOT, SEARCH, PARENT_ROOT, PARENT> match(
            SingularAttribute<ROOT, SEARCH_FIELD> attribute,
            Function<SEARCH, ? extends CriteriaOperator<SEARCH_FIELD>> search
    ) {
        CriteriaOperator<SEARCH_FIELD> criteriaOperator = search.apply(searchRequest);
        if (criteriaOperator != null) {
            Expression<SEARCH_FIELD> expression = joinRootSupplier.get().get(attribute);
            predicates.add(criteriaOperator.match(criteriaBuilder, expression));
        }
        return this;
    }

    /**
     * Does a join on specified attribute only if at least one criteria operator match results in a non-null predicate.
     *
     * @param joinAttribute the join attribute
     * @return the builder
     * @param <JOIN_ROOT> the root resulted from the join
     */
    public <JOIN_ROOT> JoinCriteriaOperatorBuilder<JOIN_ROOT, SEARCH, ROOT,
            JoinCriteriaOperatorBuilder<ROOT, SEARCH, PARENT_ROOT, PARENT>> join(
            SingularAttribute<ROOT, JOIN_ROOT> joinAttribute
    ) {
        Supplier<Join<ROOT, JOIN_ROOT>> joinRoot = () -> joinRootSupplier.get().join(joinAttribute);
        return new JoinCriteriaOperatorBuilder<>(
                criteriaBuilder,
                joinRoot,
                searchRequest,
                this
        );
    }

    /**
     * Ends current builder so that you can continue with the previous one/parent.
     * @return parent builder
     */
    public PARENT endJoin() {
        parent.predicates.addAll(this.predicates);
        return parent;
    }
}
