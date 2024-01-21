package com.apulbere.crop.operator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

/**
 * The base class which holds the operators that can be applied to all data types.
 * Base operators:
 *  eq - equal
 *  neq - not equal
 *  in
 * @param <T> the data type
 */
public abstract class BaseCriteriaOperator<T> implements CriteriaOperator<T> {

    private T eq;
    private T neq;
    private List<T> in;

    @Override
    public Predicate match(CriteriaBuilder criteriaBuilder, Expression<T> expression) {
        if (eq != null) {
            return criteriaBuilder.equal(expression, eq);
        }
        if (neq != null) {
            return criteriaBuilder.notEqual(expression, neq);
        }
        if (in != null && !in.isEmpty()) {
            return expression.in(in);
        }
        return null;
    }

    public void setEq(T eq) {
        this.eq = eq;
    }

    public void setNeq(T neq) {
        this.neq = neq;
    }

    public void setIn(List<T> in) {
        this.in = in;
    }

    @Override
    public String toString() {
        return "CriteriaOperator{" +
                "eq=" + eq +
                ", neq=" + neq +
                ", in=" + in +
                '}';
    }
}
