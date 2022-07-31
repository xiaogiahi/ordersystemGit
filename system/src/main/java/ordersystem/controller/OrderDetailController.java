package ordersystem.controller;

import ordersystem.mapper.CommodityMapper;
import ordersystem.mapper.OrderDetailMapper;
import ordersystem.mapper.OrderInfoMapper;
import ordersystem.service.OrderDetailService;
import ordersystem.util.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orderdetail")
public class OrderDetailController {
    @Autowired
    OrderDetailService orderDetailService;
    @RequestMapping
    List<OrderDetailMapper> findAll(@RequestParam(required = false)CommodityMapper commodityMapper,
                                    @RequestParam(required = false)OrderInfoMapper orderInfoMapper,
                                    @RequestParam(required = false)Signal priceSignal,
                                    @RequestParam(required = false)Double price,
                                    @RequestParam(required = false)Signal quantitySignal,
                                    @RequestParam(required = false)Double quantity){
        return orderDetailService.findAll(commodityMapper,
                orderInfoMapper,
                priceSignal,
                price,
                quantitySignal,
                quantity);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    void delete(long id){
        orderDetailService.delete(id);
    }
    @RequestMapping(method = RequestMethod.POST)
    OrderDetailMapper save(@RequestBody OrderDetailMapper orderDetailMapper){
        return null;
    }
    @RequestMapping(method = RequestMethod.PUT)
    OrderDetailMapper put(@RequestBody OrderDetailMapper orderDetailMapper){
        return orderDetailService.put(orderDetailMapper);
    }
}
