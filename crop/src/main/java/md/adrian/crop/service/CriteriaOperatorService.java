package md.adrian.crop.service;

import md.adrian.crop.CriteriaOperatorOrder;
import md.adrian.crop.CriteriaOperatorPage;
import jakarta.persistence.EntityManager;

/**
 * The main API that helps to build the Query using criteria operators.
 */
public class CriteriaOperatorService {

    private final EntityManager entityManager;

    public CriteriaOperatorService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Creates a criteria operator builder.
     *
     * @param clazz          the entity class
     * @param searchCriteria the object containing criteria operator fields
     * @param order          the query order
     * @param page           the query page
     * @param <R>            the result type, same as entity class
     * @param <SEARCH_TYPE>  the type containing criteria operator fields
     * @return a builder
     */
    public <R, SEARCH_TYPE> CriteriaOperatorBuilder<R, SEARCH_TYPE> create(
            Class<R> clazz,
            SEARCH_TYPE searchCriteria,
            CriteriaOperatorOrder order,
            CriteriaOperatorPage page
    ) {
        return new CriteriaOperatorBuilder<>(
                entityManager,
                clazz,
                searchCriteria,
                order,
                page
        );
    }

    /**
     * Creates a criteria operator builder.
     *
     * @param clazz          the entity class
     * @param searchCriteria the object containing criteria operator fields
     * @param <R>            the result type, same as entity class
     * @param <SEARCH_TYPE>  the type containing criteria operator fields
     * @return a builder
     */
    public <R, SEARCH_TYPE> CriteriaOperatorBuilder<R, SEARCH_TYPE> create(Class<R> clazz, SEARCH_TYPE searchCriteria) {
        return create(clazz, searchCriteria, null, null);
    }

}
