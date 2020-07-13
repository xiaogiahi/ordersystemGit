package ordersystem.service;

import ordersystem.mapper.CommodityMapper;
import ordersystem.mapper.OrderDetailMapper;
import ordersystem.mapper.OrderInfoMapper;
import ordersystem.repository.CommodityRepository;
import ordersystem.repository.OrderDetailRepository;
import ordersystem.util.Common;
import ordersystem.util.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    CommodityRepository commodityRepository;
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    CommodityService commodityService;
    public List<OrderDetailMapper> init(){
        orderDetailRepository.deleteAll();
        List<OrderDetailMapper> orderDetailMappers = new ArrayList<OrderDetailMapper>();
        for(int i=0;i<10;i++){
            OrderDetailMapper orderDetailMapper = new OrderDetailMapper();
            orderDetailMapper.setPrice(36);
            orderDetailMapper.setQuantity(55);
            orderDetailMapper.setCommodityMapper(commodityRepository.findOne(1L));
            orderDetailMapper.setOrderInfoMapper(orderInfoService.findOne(1L));
            orderDetailMappers.add(orderDetailMapper);
        }
        return orderDetailRepository.save(orderDetailMappers);
    }
    public OrderDetailMapper save(OrderDetailMapper orderDetailMapper){
        return orderDetailRepository.save(orderDetailMapper);
    }
    public List<OrderDetailMapper> save(List<OrderDetailMapper> orderDetailMappers){
        for(OrderDetailMapper orderDetailMapper : orderDetailMappers){
            CommodityMapper commodityMapper = orderDetailMapper.getCommodityMapper();
            commodityMapper.setQuantity(commodityMapper.getQuantity()-orderDetailMapper.getQuantity());
        }
        List<CommodityMapper> commodityMappers  = orderDetailMappers.stream().map(o->o.getCommodityMapper()).collect(Collectors.toList());
        commodityService.save(commodityMappers);
        return orderDetailRepository.save(orderDetailMappers);
    }

    public OrderDetailMapper put(OrderDetailMapper orderDetailMapper){
        OrderDetailMapper newOrderDetailMapper = orderDetailRepository.findOne(orderDetailMapper.getId());
        newOrderDetailMapper.setOrderInfoMapper(orderDetailMapper.getOrderInfoMapper());
        newOrderDetailMapper.setCommodityMapper(orderDetailMapper.getCommodityMapper());
        newOrderDetailMapper.setQuantity(orderDetailMapper.getQuantity());
        newOrderDetailMapper.setPrice(orderDetailMapper.getPrice());
        return orderDetailRepository.save(newOrderDetailMapper);
    }
    public List<OrderDetailMapper> put(List<OrderDetailMapper> orderDetailMappers){
        List<OrderDetailMapper> newOrderDetails = new ArrayList<OrderDetailMapper>();
        for(OrderDetailMapper orderDetailMapper:orderDetailMappers){
            OrderDetailMapper newOrderDetailMapper = orderDetailRepository.findOne(orderDetailMapper.getId());
            newOrderDetailMapper.setOrderInfoMapper(orderDetailMapper.getOrderInfoMapper());
            newOrderDetailMapper.setCommodityMapper(orderDetailMapper.getCommodityMapper());
            newOrderDetailMapper.setQuantity(orderDetailMapper.getQuantity());
            newOrderDetailMapper.setPrice(orderDetailMapper.getPrice());
            newOrderDetails.add(newOrderDetailMapper);
        }
        return orderDetailRepository.save(newOrderDetails);
    }
    public void delete(Long id){
        OrderDetailMapper orderDetailMapper = orderDetailRepository.findOne(id);
        orderDetailRepository.delete(orderDetailMapper);
    }
    public List<OrderDetailMapper> findAll(final CommodityMapper commodityMapper,
                                           final OrderInfoMapper orderInfoMapper,
                                           final Signal priceSignal,
                                           final Double price,
                                           final Signal quantitySingal,
                                           final Double quantity
                                           ){
        Specification<OrderDetailMapper> spec = new Specification<OrderDetailMapper>() {
            @Override
            public Predicate toPredicate(Root<OrderDetailMapper> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if(price!=null&&priceSignal!=null){
                    predicateList.add(Common.comparePredicate(root,cq,cb,priceSignal,"price",price));
                }
                if(quantity!=null&&quantitySingal!=null){
                    predicateList.add(Common.comparePredicate(root,cq,cb,quantitySingal,"quantity",quantity));
                }
                if(commodityMapper!=null){
                    predicateList.add(cb.equal(root.get("commodityMapper"),commodityMapper));
                }
                if(orderInfoMapper!=null){
                    predicateList.add(cb.equal(root.get("orderInfoMapper"),orderInfoMapper));
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                return cb.and(predicateList.toArray(predicates));

            }

        };
        return orderDetailRepository.findAll(spec);
    }
    public void deleteInBatch(List<OrderDetailMapper> orderDetailMappers){
        List<CommodityMapper> commodityMappers = orderDetailMappers.stream().map(o->{
            CommodityMapper c = o.getCommodityMapper();
            c.setQuantity(c.getQuantity()+o.getQuantity());
            return c;
        }).collect(Collectors.toList());
        commodityService.update(commodityMappers);
        orderDetailRepository.deleteInBatch(orderDetailMappers);
    }

}
