package com.apulbere.crop.operator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

/**
 * Represents a function that accepts {@code CriteriaBuilder} and {@code Expression} and creates a {@code Predicate}.
 *
 * @param <T> the type of the expression
 */
@FunctionalInterface
public interface CriteriaOperator<T> {

    /**
     * Creates a predicate with the help of criteria builder on the expression by using first non-null operator in implementation.
     *
     * @param criteriaBuilder the criteria builder
     * @param expression the expression
     * @return the created predicate or null
     */
    Predicate match(CriteriaBuilder criteriaBuilder, Expression<T> expression);

}
