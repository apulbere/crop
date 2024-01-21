package com.apulbere.crop;

import com.apulbere.crop.annotation.Experimental;

import java.util.List;

/**
 * Contains the list of fields used by the query for sorting/ordering.
 * The fields prefix with '-' are considered as descending order.
 */
@Experimental
public class CriteriaOperatorOrder {
    List<String> order;

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order=" + order +
                '}';
    }
}
