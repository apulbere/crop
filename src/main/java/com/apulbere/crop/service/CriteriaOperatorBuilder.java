package com.apulbere.crop.service;

import com.apulbere.crop.CriteriaOperatorOrder;
import com.apulbere.crop.CriteriaOperatorPage;
import com.apulbere.crop.operator.CriteriaOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNullElse;

/**
 * {@inheritDoc}
 * The builder provides functionality to match, start a join, build and execute query.
 *
 * @param <ROOT> the root type
 * @param <SEARCH> the type containing criteria operator fields
 */
public class CriteriaOperatorBuilder<ROOT, SEARCH> extends BaseCriteriaOperatorBuilder<ROOT, SEARCH> {

    private final EntityManager entityManager;
    private final CriteriaQuery<ROOT> criteriaQuery;
    private final Root<ROOT> root;
    private final CriteriaOperatorOrder order;
    private final CriteriaOperatorPage page;

    CriteriaOperatorBuilder(
        EntityManager entityManager,
        CriteriaBuilder criteriaBuilder,
        CriteriaQuery<ROOT> criteriaQuery,
        Root<ROOT> root,
        SEARCH searchRequest,
        CriteriaOperatorOrder order,
        CriteriaOperatorPage page
    ) {
        super(criteriaBuilder, searchRequest);
        this.entityManager = entityManager;
        this.criteriaQuery = criteriaQuery;
        this.root = root;
        this.order = order;
        this.page = page;
    }

    @Override
    public <SEARCH_FIELD> CriteriaOperatorBuilder<ROOT, SEARCH> match(
        SingularAttribute<ROOT, SEARCH_FIELD> attribute,
        Function<SEARCH, ? extends CriteriaOperator<SEARCH_FIELD>> search
    ) {
        CriteriaOperator<SEARCH_FIELD> criteriaOperator = search.apply(searchRequest);
        if (criteriaOperator != null) {
            var expression = root.get(attribute);
            predicates.add(criteriaOperator.match(criteriaBuilder, expression));
        }
        return this;
    }

    public <JOIN> JoinCriteriaOperatorBuilder<JOIN, SEARCH, ROOT, CriteriaOperatorBuilder<ROOT, SEARCH>> join(
        SingularAttribute<ROOT, JOIN> joinAttribute
    ) {
        Supplier<Join<ROOT, JOIN>> joinRoot = () -> root.join(joinAttribute);
        return new JoinCriteriaOperatorBuilder<>(
            criteriaBuilder,
            joinRoot,
            searchRequest,
            this
        );
    }

    /**
     * Join with a {@code ListAttribute}.
     * For example, in a one to many relation.
     *
     * @param joinAttribute the attribute that is doing the join
     * @return the builder
     * @param <JOIN>
     */
    public <JOIN> JoinCriteriaOperatorBuilder<JOIN, SEARCH, ROOT, CriteriaOperatorBuilder<ROOT, SEARCH>> join(
        ListAttribute<ROOT, JOIN> joinAttribute
    ) {
        Supplier<Join<ROOT, JOIN>> joinRoot = () -> root.join(joinAttribute);
        return new JoinCriteriaOperatorBuilder<>(
                criteriaBuilder,
                joinRoot,
                searchRequest,
                this
        );
    }

    /**
     * Builds the query with all matched criteria operators as predicates if any.
     * Adds order by if it was provided on creation.
     * Adds max result and the offset to the query if page was provided on creation.
     *
     * @return the query
     */
    public TypedQuery<ROOT> getQuery() {
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.orderBy(createOrderBy());

        TypedQuery<ROOT> query = entityManager.createQuery(criteriaQuery);

        if (page != null && page.getSize() != null) {
            var offset = requireNonNullElse(page.getOffset(), 0);
            query = query.setFirstResult(offset);
            query = query.setMaxResults(page.getSize());
        }
        return query;
    }

    /**
     * Builds the query {@see CriteriaOperatorBuilder#getQuery()} and executes it.
     *
     * @return list of entities
     */
    public List<ROOT> getResultList() {
        return getQuery().getResultList();
    }

    private List<Order> createOrderBy() {
        if (order == null || order.getOrder() == null) {
            return List.of();
        }
        return order.getOrder()
            .stream()
            .map(this::toOrder)
            .toList();
    }

    private Order toOrder(String order) {
        if (order.startsWith("-")) {
            return criteriaBuilder.desc(root.get(order.substring(1)));
        }
        return criteriaBuilder.asc(root.get(order));
    }
}
