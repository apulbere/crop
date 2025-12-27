package md.adrian.crop.operator;

import md.adrian.crop.exception.CriteriaOperatorException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

/**
 * Abstract Criteria Operator for {@code Comparable} types.
 * Additional operators:
 *  btw - between
 *  gt - greater than
 *  gte - greater than or equal
 *  lt - less than
 *  lte - less than or equal
 *
 * @param <T> the comparable type
 */
public abstract class ComparableCriteriaOperator<T extends Comparable> extends BaseCriteriaOperator<T> {

    private List<T> btw;
    private T gt;
    private T gte;
    private T lt;
    private T lte;

    /**
     * {@inheritDoc}
     *
     * @throws CriteriaOperatorException if between operator is specified with wrong number of arguments, other than 2
     *
     * @param criteriaBuilder the criteria builder
     * @param expression the expression
     * @return
     */
    @Override
    public Predicate match(CriteriaBuilder criteriaBuilder, Expression<T> expression) {
        if (btw != null && btw.size() != 2) {
            throw new CriteriaOperatorException("between must have two arguments");
        } else if (btw != null) {
            return criteriaBuilder.between(expression, btw.get(0), btw.get(1));
        } else if (gt != null) {
            return criteriaBuilder.greaterThan(expression, gt);
        } else if (gte != null) {
            return criteriaBuilder.greaterThanOrEqualTo(expression, gte);
        } else if (lt != null) {
            return criteriaBuilder.lessThan(expression, lt);
        } else if (lte != null) {
            return criteriaBuilder.lessThanOrEqualTo(expression, lte);
        }
        return super.match(criteriaBuilder, expression);
    }

    public void setBtw(List<T> btw) {
        this.btw = btw;
    }

    public void setGt(T gt) {
        this.gt = gt;
    }

    public void setGte(T gte) {
        this.gte = gte;
    }

    public void setLt(T lt) {
        this.lt = lt;
    }

    public void setLte(T lte) {
        this.lte = lte;
    }

    @Override
    public String toString() {
        return "ComparableCriteriaOperator{" +
                "between=" + btw +
                ", gt=" + gt +
                ", gte=" + gte +
                ", lt=" + lt +
                ", lte=" + lte +
                '}';
    }
}
