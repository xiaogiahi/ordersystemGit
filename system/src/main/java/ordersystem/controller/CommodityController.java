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


@Controller
@RequestMapping("commodity")
public class CommodityController {
    @Autowired
    CommodityService commodityService;

    @RequestMapping("/init")
    String init(){
        commodityService.deleteAll();
        List<CommodityMapper> initCommodities = new ArrayList<CommodityMapper>();
        for (int i=0;i<10;i++){
            CommodityMapper commodityMapper = new CommodityMapper();
            commodityMapper.setName("G"+i);
            commodityMapper.setType(i+"");
            commodityMapper.setPrice(i);
            commodityMapper.setQuantity(10+i);
            initCommodities.add(commodityMapper);
        }
        commodityService.save(initCommodities);
        return "redirect:/commodity/index";
    }
    @RequestMapping()
    String indexPage(Model model){
        model.addAttribute("commoditiesPage",commodityService.findAll(null,null));
        return "commodity/selectPage";
    }

    @RequestMapping("/index")
    @ResponseBody
    Page<CommodityMapper>  findPage(Model model,
            @RequestParam(value = "currentPage",required = false) Integer currentPage,
                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                     @RequestParam(value = "name",required = false) String name,
                     @RequestParam(value = "type",required = false) String type
                     ){

        if("".equals(name)){
            Page<CommodityMapper> commodityMappers = commodityService.findAll(currentPage,pageSize);
            model.addAttribute("commoditiesPage",commodityMappers);
            return commodityMappers;
        }
        else if("".equals(type)){
            Page<CommodityMapper> commodityMappers = commodityService.findByName(name,currentPage,pageSize);
            model.addAttribute("commoditiesPage",commodityMappers);
            return commodityMappers;
        }
        else {
            Page<CommodityMapper> commodityMappers = commodityService.findByNameAndType(name,type,currentPage,pageSize);
            model.addAttribute("commoditiesPage",commodityMappers);
            return commodityMappers;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    String saveCommodity(@RequestBody ArrayList<CommodityMapper> commodityMappers){
        commodityService.save(commodityMappers);
        return "redirect:/commodity";
    }
    @RequestMapping("/{id}")
    String update(Model model, @PathVariable long id){
        CommodityMapper commodityMapper = commodityService.findOne(id);
        model.addAttribute("commodity", commodityMapper);
        return "commodity/update";
    }

    @RequestMapping (method = RequestMethod.PUT)
    String updateCommodity(@ModelAttribute CommodityMapper commodity){
        commodityService.update(commodity);
        return "redirect:/commodity";
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    Page<CommodityMapper> deleteCommodity(
            @PathVariable long id){
        CommodityMapper commodityMapper = commodityService.findOne(id);
        commodityService.delete(commodityMapper);
        return commodityService.findAll(null,null);
    }
    @RequestMapping("/save")
    String save(){
        return "commodity/save";
    }
}
