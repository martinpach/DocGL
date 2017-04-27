package com.docgl.db;

import com.docgl.ValidationException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 * Created by Martin on 27.4.2017.
 */
public class DatabaseCommonMethods {
    public Criteria setPaginationAndSorting(Criteria criteria, int limit, int start, String sortBy, String way){
        if(limit > 0 && start >= 0) {
            criteria.setFirstResult(start)
                    .setMaxResults(limit);
        }
        if(sortBy != null) {
            if (!sortBy.equals("lastName")) {
                throw new ValidationException("Query param '" + sortBy + "' is not valid");
            } else {
                if (way != null) {
                    if (way.equals("asc")) {
                        criteria.addOrder(Order.asc(sortBy));
                    } else if (way.equals("desc")) {
                        criteria.addOrder(Order.desc(sortBy));
                    } else {
                        throw new ValidationException("Query param '" + way + "' is not valid. Use instead : 'asc' or 'desc'");
                    }
                } else {
                    throw new ValidationException("In case of sorting use 'way' query parameter with sortBy parameter");
                }
            }
        }
        return criteria;
    }
}
