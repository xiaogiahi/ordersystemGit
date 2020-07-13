package ordersystem.util;

import ordersystem.mapper.OrderDetailMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class Common {
    private static Integer PAGE_SIZE = 5;

    public static Pageable getPageable(Integer currentPage, Integer pageSize) {
        if (currentPage == null) currentPage = 0;
        else currentPage -= 1;
        if (pageSize == null) pageSize = PAGE_SIZE;
        return new PageRequest(currentPage, pageSize);
    }

    public static boolean stringIsEmpty(String s) {
        if (s == null) return true;
        if ("".equals(s)) return true;
        return false;
    }

    public static Predicate comparePredicate(
            Root<OrderDetailMapper> root, CriteriaQuery<?> cq, CriteriaBuilder cb,
            Signal signal, String attrName, Double quantity) {
        switch (signal) {
            case GT: {
                return cb.greaterThan(root.get(attrName), quantity);
            }
            case LESS: {
                return cb.lessThan(root.get(attrName), quantity);
            }
            case GTorEQ: {
                return cb.greaterThanOrEqualTo(root.get(attrName), quantity);
            }
            case LESSorEQ: {
                return cb.lessThanOrEqualTo(root.get(attrName), quantity);
            }
        }
        return null;
    }
}
