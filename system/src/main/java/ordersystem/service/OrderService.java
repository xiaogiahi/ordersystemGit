package ordersystem.service;

import ordersystem.mapper.CommodityMapper;
import ordersystem.mapper.Order;
import ordersystem.mapper.OrderDetailMapper;
import ordersystem.mapper.OrderInfoMapper;
import ordersystem.util.Common;
import ordersystem.util.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    OrderInfoService orderInfoService;
    @Autowired
    CommodityService commodityService;
    public Order save(Order order){
        order.setOrderInfo(orderInfoService.save(order.getOrderInfo()));
        for(OrderDetailMapper orderDetailMapper:order.getOrderDetails()){
            CommodityMapper commodity = orderDetailMapper.getCommodityMapper();
            if(commodity.getId()==null){
                orderDetailMapper.setCommodityMapper(commodityService.save(commodity));
            }
            else {
                orderDetailMapper.setCommodityMapper(commodityService.findOne(commodity.getId()));
            }
            orderDetailMapper.setOrderInfoMapper(order.getOrderInfo());
        }
        orderDetailService.save(order.getOrderDetails());
        return order;
    }
    public List<OrderDetailMapper> findDetailByInfo(OrderInfoMapper orderInfoMapper){
        return orderDetailService.findAll(null,
                orderInfoMapper,
                null,null,null,null);

    }
    public Page<Order> findAll(){
        return findAll(null,null,null,null,null,null,null);
    }
    public Page<Order> findAll(String customerName,
                               String address,
                               Date startDate,
                               Date endDate,
                               Signal signal,
                               Double totalPrice,
                               Integer currentPage){
        Page<OrderInfoMapper> orderInfos = orderInfoService.findAll(customerName,address,startDate,endDate,signal,totalPrice,currentPage);
        List<Order> orders = new ArrayList<Order>();
        for(OrderInfoMapper orderInfo:orderInfos){
            Order order = new Order();
            order.setOrderInfo(orderInfo);
            order.setOrderDetails(findDetailByInfo(orderInfo));
            orders.add(order);
        }
        Page<Order> orderPages = new PageImpl<Order>(orders, Common.getPageable(orderInfos.getNumber()+1,null),orderInfos.getTotalElements());
        return orderPages;
    }
    public Order update(Order order){
        Long orderId = order.getOrderInfo().getId();
        order.getOrderInfo().setId(0);
        order.getOrderDetails().stream().map(o->{
            o.setId(null);
            return o;
        });
        save(order);
        delete(orderId);
//        order.setOrderInfo(orderInfoService.put(order.getOrderInfo()));
//        order.setOrderDetails(orderDetailService.put(order.getOrderDetails()));
        return order;
    }
    public void delete(Order order){
        orderDetailService.deleteInBatch(order.getOrderDetails());
        orderInfoService.delete(order.getOrderInfo().getId());
    }
    public Page<Order> delete(Long id){
        orderDetailService.deleteInBatch(findDetailByInfo(orderInfoService.findOne(id)));
        orderInfoService.delete(id);
        return findAll();

    }
    public Order findOne(Long id){
        Order order = new Order();
        OrderInfoMapper orderInfoMapper = orderInfoService.findOne(id);
        order.setOrderInfo(orderInfoMapper);
        List<OrderDetailMapper> orderDetailMappers= findDetailByInfo(orderInfoMapper);
        order.setOrderDetails(orderDetailMappers);
        return order;
    }

}
