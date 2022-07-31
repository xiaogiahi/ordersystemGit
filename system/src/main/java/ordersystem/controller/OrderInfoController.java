package ordersystem.controller;

import ordersystem.mapper.OrderDetailMapper;
import ordersystem.mapper.OrderInfoMapper;
import ordersystem.service.OrderInfoService;
import ordersystem.util.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/orderinfo")
public class OrderInfoController {
    @Autowired
    OrderInfoService orderInfoService;
    @RequestMapping
    Page<OrderInfoMapper> findAll(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false)Signal signal,
            @RequestParam(required = false)Double totalPrice,
            @RequestParam(required = false)Integer currentPage){
        return orderInfoService.findAll(customerName,address,startDate,endDate,signal,totalPrice,currentPage);
    }
    @RequestMapping(method = RequestMethod.POST)
    OrderInfoMapper save(@RequestBody OrderInfoMapper orderInfoMapper){
        return orderInfoService.save(orderInfoMapper);
    }
    @RequestMapping("/init")
    void init(){
       orderInfoService.init();
    }
    @RequestMapping(method = RequestMethod.PUT)
    OrderInfoMapper put(@RequestBody OrderInfoMapper orderInfoMapper){
        return orderInfoService.put(orderInfoMapper);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    void delete(@PathVariable long id){
        orderInfoService.delete(id);
    }
}
