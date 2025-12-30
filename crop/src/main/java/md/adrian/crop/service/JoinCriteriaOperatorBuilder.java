package md.adrian.crop.service;

import md.adrian.crop.operator.CriteriaOperator;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.function.Function;

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

    private final PARENT parent;
    private final Function<Root<PARENT_ROOT>, Path<ROOT>> joinPathFunction;

    JoinCriteriaOperatorBuilder(
            SEARCH searchRequest,
            PARENT parent,
            Function<Root<PARENT_ROOT>, Path<ROOT>> joinPathFunction
    ) {
        super(searchRequest);
        this.parent = parent;
        this.joinPathFunction = joinPathFunction;
    }

    @Override
    public <SEARCH_FIELD> JoinCriteriaOperatorBuilder<ROOT, SEARCH, PARENT_ROOT, PARENT> match(
            SingularAttribute<ROOT, SEARCH_FIELD> attribute,
            Function<SEARCH, ? extends CriteriaOperator<SEARCH_FIELD>> search
    ) {
        CriteriaOperator<SEARCH_FIELD> criteriaOperator = search.apply(searchRequest);
        if (criteriaOperator != null) {
            addRootPredicateSupplier((cb, parentRoot) -> {
                Path<ROOT> joinPath = joinPathFunction.apply((Root<PARENT_ROOT>) parentRoot);
                return criteriaOperator.match(cb, joinPath.get(attribute));
            });
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
        return new JoinCriteriaOperatorBuilder<>(
                searchRequest,
                this,
                parentRoot -> {
                    Path<ROOT> joinPath = joinPathFunction.apply((Root<PARENT_ROOT>) parentRoot);
                    return joinPath.get(joinAttribute);
                }
        );
    }

    /**
     * Ends current builder so that you can continue with the previous one/parent.
     * @return parent builder
     */
    public PARENT endJoin() {
        parent.addAllRootPredicateSupplier(this);
        return parent;
    }
}
