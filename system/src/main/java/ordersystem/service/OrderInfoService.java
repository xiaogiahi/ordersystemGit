package ordersystem.service;

import ordersystem.mapper.OrderDetailMapper;
import ordersystem.mapper.OrderInfoMapper;
import ordersystem.repository.OrderDetailRepository;
import ordersystem.repository.OrderInfoRepository;
import ordersystem.util.Common;
import ordersystem.util.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoService {
    @Autowired
    OrderInfoRepository orderInfoRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    public void init(){
        orderInfoRepository.deleteAll();
        List<OrderInfoMapper> orderInfoMappers = new ArrayList<OrderInfoMapper>();
        for(int i=30;i<35;i++){
            OrderInfoMapper orderInfoMapper = new OrderInfoMapper();
            orderInfoMapper.setAddress("address");
            orderInfoMapper.setCustomerName("customer"+i);
            orderInfoMappers.add(orderInfoMapper);
         }
        orderInfoRepository.save(orderInfoMappers);
    }
    public Page<OrderInfoMapper> findAll(final String customerName,
                                         final String address,
                                         final Date startDate,
                                         final Date endDate,
                                         final Signal signal,
                                         final Double totalPrice,
                                         Integer currentPage){
        Specification<OrderInfoMapper> spec = new Specification<OrderInfoMapper>() {
            @Override
            public Predicate toPredicate(Root<OrderInfoMapper> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if(!Common.stringIsEmpty(customerName)){
                    predicateList.add(cb.like(root.<String>get("customerName"),customerName));
                }
                if(!Common.stringIsEmpty(address)){
                    predicateList.add(cb.like(root.<String>get("address"),address));
                }
                if(startDate!=null&&endDate!=null){
                    predicateList.add(cb.between(root.get("date"),startDate,endDate));
                }
                if(signal!=null&&totalPrice!=null){
                    switch (signal){
                        case GT:{
                            predicateList.add(cb.greaterThan(root.get("totalPrice"),totalPrice));
                            break;
                        }
                        case LESS:{
                            predicateList.add(cb.lessThan(root.get("totalPrice"),totalPrice));
                            break;
                        }
                        case GTorEQ:{
                            predicateList.add(cb.greaterThanOrEqualTo(root.get("totalPrice"),totalPrice));
                            break;
                        }
                        case LESSorEQ:{
                            predicateList.add(cb.lessThanOrEqualTo(root.get("totalPrice"),totalPrice));
                            break;
                        }
                    }

                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                return cb.and(predicates);
            }
        };
        return orderInfoRepository.findAll(spec,Common.getPageable(currentPage,null));


    }
    public OrderInfoMapper save(OrderInfoMapper orderInfoMapper){
        orderInfoRepository.save(orderInfoMapper);
        return orderInfoMapper;
    }
    public void delete(long id){
        OrderInfoMapper orderInfoMapper = orderInfoRepository.findOne(id);
        orderInfoRepository.delete(orderInfoMapper);
    }
    public OrderInfoMapper put(OrderInfoMapper orderInfo){
        OrderInfoMapper newOrderInfo = orderInfoRepository.findOne(orderInfo.getId());
        newOrderInfo.setCustomerName(orderInfo.getCustomerName());
        newOrderInfo.setAddress(orderInfo.getAddress());
        newOrderInfo.setDate(orderInfo.getDate());
        newOrderInfo.setTotalPrice(orderInfo.getTotalPrice());
        return orderInfoRepository.save(newOrderInfo);
    }
    public OrderInfoMapper findOne(Long id){
        return orderInfoRepository.findOne(id);
    }
}
