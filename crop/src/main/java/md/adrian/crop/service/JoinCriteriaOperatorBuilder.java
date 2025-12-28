package md.adrian.crop.service;

import md.adrian.crop.operator.CriteriaOperator;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.LinkedList;
import java.util.List;
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

    private final Supplier<Path<ROOT>> joinRootSupplier;
    private final PARENT parent;
    private final Function<Root<PARENT_ROOT>, Path<ROOT>> joinPathFunction;
    private final List<Function<Root<PARENT_ROOT>, Predicate>> rootPredicates = new LinkedList<>();

    JoinCriteriaOperatorBuilder(
            CriteriaBuilder criteriaBuilder,
            Supplier<Path<ROOT>> joinRootSupplier,
            SEARCH searchRequest,
            PARENT parent,
            Function<Root<PARENT_ROOT>, Path<ROOT>> joinPathFunction
    ) {
        super(criteriaBuilder, searchRequest);
        this.joinRootSupplier = joinRootSupplier;
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
            Expression<SEARCH_FIELD> expression = joinRootSupplier.get().get(attribute);
            addPredicateSupplier(cb -> criteriaOperator.match(cb, expression));
            rootPredicates.add(parentRoot -> {
                Path<ROOT> joinPath = joinPathFunction.apply(parentRoot);
                return criteriaOperator.match(criteriaBuilder, joinPath.get(attribute));
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
        Supplier<Path<JOIN_ROOT>> joinRoot = () -> joinRootSupplier.get().get(joinAttribute);
        return new JoinCriteriaOperatorBuilder<>(
                criteriaBuilder,
                joinRoot,
                searchRequest,
                this,
                parentRoot -> joinRootSupplier.get().get(joinAttribute)
        );
    }

    /**
     * Ends current builder so that you can continue with the previous one/parent.
     * @return parent builder
     */
    public PARENT endJoin() {
        parent.addAllPredicateSupplier(this);
        if (parent instanceof CriteriaOperatorBuilder) {
            @SuppressWarnings("unchecked")
            CriteriaOperatorBuilder<PARENT_ROOT, SEARCH> criteriaParent = (CriteriaOperatorBuilder<PARENT_ROOT, SEARCH>) parent;
            criteriaParent.addAllRootPredicates(rootPredicates);
        }
        return parent;
    }

    List<Function<Root<PARENT_ROOT>, Predicate>> getRootPredicates() {
        return rootPredicates;
    }
}
