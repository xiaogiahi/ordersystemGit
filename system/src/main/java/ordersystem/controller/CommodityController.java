package ordersystem.controller;

import ordersystem.mapper.CommodityMapper;
import ordersystem.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("commodity")
public class CommodityController {
    @Autowired
    CommodityService commodityService;

    @RequestMapping("/init")
    List<CommodityMapper> init(){
        commodityService.deleteAll();
        List<CommodityMapper> initCommodities = new ArrayList<>();
        for (int i=0;i<10;i++){
            CommodityMapper commodityMapper = new CommodityMapper();
            commodityMapper.setName("G"+i);
            commodityMapper.setType(i+"");
            commodityMapper.setPrice(i);
            commodityMapper.setQuantity(10+i);
            initCommodities.add(commodityMapper);
        }
        return commodityService.save(initCommodities);
    }

    @RequestMapping("/index")
    Page<CommodityMapper>  findPage(
            @RequestParam(value = "currentPage",required = false) Integer currentPage,
                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                     @RequestParam(value = "name",required = false) String name,
                     @RequestParam(value = "type",required = false) String type
                     ){

        if("".equals(name)){
            return commodityService.findAll(currentPage,pageSize);
        }
        else if("".equals(type)){
            return commodityService.findByName(name,currentPage,pageSize);
        }
        else {
            return commodityService.findByNameAndType(name,type,currentPage,pageSize);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    List<CommodityMapper> saveCommodity(@RequestBody ArrayList<CommodityMapper> commodityMappers){
        return commodityService.save(commodityMappers);
    }
    @RequestMapping("/{id}")
    CommodityMapper update(Model model, @PathVariable long id){
        return commodityService.findOne(id);
    }

    @RequestMapping (method = RequestMethod.PUT)
    CommodityMapper updateCommodity(@ModelAttribute CommodityMapper commodity){
        return commodityService.update(commodity);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    Page<CommodityMapper> deleteCommodity(
            @PathVariable long id){
        CommodityMapper commodityMapper = commodityService.findOne(id);
        commodityService.delete(commodityMapper);
        return commodityService.findAll(null,null);
    }
}
