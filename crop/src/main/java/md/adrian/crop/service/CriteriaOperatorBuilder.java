package md.adrian.crop.service;

import md.adrian.crop.CriteriaOperatorOrder;
import md.adrian.crop.CriteriaOperatorPage;
import md.adrian.crop.operator.CriteriaOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.LinkedList;
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
    private final Class<ROOT> rootType;
    private final Root<ROOT> root;
    private final CriteriaOperatorOrder order;
    private final CriteriaOperatorPage page;
    private final List<Function<Root<ROOT>, Predicate>> rootPredicates = new LinkedList<>();

    CriteriaOperatorBuilder(
            EntityManager entityManager,
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<ROOT> criteriaQuery,
            Class<ROOT> rootType,
            Root<ROOT> root,
            SEARCH searchRequest,
            CriteriaOperatorOrder order,
            CriteriaOperatorPage page
    ) {
        super(criteriaBuilder, searchRequest);
        this.entityManager = entityManager;
        this.criteriaQuery = criteriaQuery;
        this.rootType = rootType;
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
            addPredicateSupplier(cb -> criteriaOperator.match(cb, expression));
            rootPredicates.add(r -> criteriaOperator.match(criteriaBuilder, r.get(attribute)));
        }
        return this;
    }

    public <JOIN> JoinCriteriaOperatorBuilder<JOIN, SEARCH, ROOT, CriteriaOperatorBuilder<ROOT, SEARCH>> join(
        SingularAttribute<ROOT, JOIN> joinAttribute
    ) {
        Supplier<Path<JOIN>> joinRoot = () -> root.get(joinAttribute);
        return new JoinCriteriaOperatorBuilder<>(
            criteriaBuilder,
            joinRoot,
            searchRequest,
            this,
            r -> r.get(joinAttribute)
        );
    }

    /**
     * Join with a {@code ListAttribute}.
     * For example, in a one-to-many relation.
     *
     * @param joinAttribute the attribute that is doing the join
     * @return the builder
     */
    public <JOIN> JoinCriteriaOperatorBuilder<JOIN, SEARCH, ROOT, CriteriaOperatorBuilder<ROOT, SEARCH>> join(
        ListAttribute<ROOT, JOIN> joinAttribute
    ) {
        Supplier<Path<JOIN>> joinRoot = () -> root.join(joinAttribute);
        return new JoinCriteriaOperatorBuilder<>(
                criteriaBuilder,
                joinRoot,
                searchRequest,
                this,
                r -> r.join(joinAttribute)
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
        criteriaQuery.where(getPredicates(criteriaBuilder));
        criteriaQuery.orderBy(createOrderBy(criteriaBuilder));

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

    /**
     * Adds root predicates from a joined builder.
     * @param predicates the predicates to add
     */
    void addAllRootPredicates(List<Function<Root<ROOT>, Predicate>> predicates) {
        rootPredicates.addAll(predicates);
    }

    /**
     * Builds count query with all matched criteria operators as predicates if any.
     *
     * @return count query
     */
    private TypedQuery<Long> getCountQuery() {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<ROOT> countRoot = countQuery.from(rootType);
        countQuery.select(criteriaBuilder.count(countRoot));

        Predicate[] predicates = rootPredicates.stream()
                .map(f -> f.apply(countRoot))
                .toArray(Predicate[]::new);

        countQuery.where(predicates);
        return entityManager.createQuery(countQuery);
    }

    /**
     * Builds the count query {@see CriteriaOperatorBuilder#getCountQuery()} and executes it.
     *
     * @return count of entities
     */
    public Long getCount() {
        return getCountQuery().getSingleResult();
    }

    private List<Order> createOrderBy(CriteriaBuilder criteriaBuilder) {
        if (order == null || order.getOrder() == null) {
            return List.of();
        }
        return order.getOrder()
            .stream()
            .map(order -> toOrder(criteriaBuilder, order))
            .toList();
    }

    private Order toOrder(CriteriaBuilder criteriaBuilder, String order) {
        if (order.startsWith("-")) {
            return criteriaBuilder.desc(root.get(order.substring(1)));
        }
        return criteriaBuilder.asc(root.get(order));
    }
}
