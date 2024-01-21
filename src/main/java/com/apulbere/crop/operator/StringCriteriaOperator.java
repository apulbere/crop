package com.apulbere.crop.operator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

/**
 * Criteria Operator for {@code String} type.
 * Additional operators:
 *  like
 */
public class StringCriteriaOperator extends BaseCriteriaOperator<String> {

    private String like;

    @Override
    public Predicate match(CriteriaBuilder criteriaBuilder, Expression<String> expression) {
        if (like != null) {
            return criteriaBuilder.like(expression, "%" + like + "%");
        }
        return super.match(criteriaBuilder, expression);
    }

    public void setLike(String like) {
        this.like = like;
    }

    @Override
    public String toString() {
        return "StringCriteriaOperator{" +
                "like='" + like + '\'' +
                '}';
    }
}
