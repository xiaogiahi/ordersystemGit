package ordersystem.controller;

import ordersystem.mapper.Order;
import ordersystem.mapper.OrderDetailMapper;
import ordersystem.mapper.OrderInfoMapper;
import ordersystem.service.CommodityService;
import ordersystem.service.OrderService;
import ordersystem.util.Signal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("totalorder")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    CommodityService commodityService;
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    Order save(@RequestBody Order order){
        return orderService.save(order);
    }
    @RequestMapping("/info")
    @ResponseBody
    List<OrderDetailMapper> findDetailByInfo(@RequestParam OrderInfoMapper orderInfoMapper){
        return orderService.findDetailByInfo(orderInfoMapper);
    }
    @RequestMapping
    String findAll(Model model){
        model.addAttribute("ordersPage",orderService.findAll());
        return "orderTemplates/selectPage";
    }
    @RequestMapping("save")
    String savePage(Model model){
        model.addAttribute("commodities",commodityService.findAll());
        return "orderTemplates/savePage";
    }
    @RequestMapping("index")
    @ResponseBody
    Page<Order> find(@RequestParam(required = false) Integer currentPage,
                     @RequestParam(required = false) String customerName,
                     @RequestParam(required = false) String address,
                     @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startDate,
                     @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date endDate,
                     @RequestParam(required = false)Signal signal,
                     @RequestParam(required = false)Double totalPrice){
        return orderService.findAll(customerName,address,startDate,endDate,signal,totalPrice,currentPage);

    }
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    Order update(@RequestBody Order order){
        return orderService.update(order);
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    void delete(Model model, @PathVariable Long id){
        Page<Order> orders = orderService.delete(id);
        model.addAttribute("response",orders);
    }

    @RequestMapping(value = "/{id}")
    String  updatePage(Model model,@PathVariable Long id){
        model.addAttribute("order",orderService.findOne(id));
        return "orderTemplates/updatePage";
    }


}
