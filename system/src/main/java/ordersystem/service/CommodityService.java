package ordersystem.service;

import ordersystem.mapper.CommodityMapper;
import ordersystem.repository.CommodityRepository;
import ordersystem.util.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommodityService {
    @Autowired
    CommodityRepository commodityRepository;
    public CommodityMapper findOne(long id){
        return commodityRepository.findOne(id);
    }

    public List<CommodityMapper> findAll(){
       return commodityRepository.findAll();
    }
    public Page<CommodityMapper> findByName(String name, Integer currentPage, Integer pageSize){
        return commodityRepository.findByNameLike(name,Common.getPageable(currentPage,pageSize));
    }
    public Page<CommodityMapper> findByNameAndType( String name, String type,Integer currentPage, Integer pageSize){
        return commodityRepository.findByNameAndType(name,type,Common.getPageable(currentPage,pageSize));
    }

    public CommodityMapper save(CommodityMapper commodityMapper){
        return commodityRepository.save(commodityMapper);
    }

    public List<CommodityMapper> save(List<CommodityMapper> commodityMappers){
        return commodityRepository.save(commodityMappers);
    }

    public List<CommodityMapper> update(List<CommodityMapper> newCommodityMappers){
        List<CommodityMapper> saved = new ArrayList<CommodityMapper>();
        for (CommodityMapper commodityMapper:newCommodityMappers){
            CommodityMapper newCommodityMapper = commodityRepository.findOne(commodityMapper.getId());
            newCommodityMapper.setName(commodityMapper.getName());
            newCommodityMapper.setType(commodityMapper.getType());
            newCommodityMapper.setPrice(commodityMapper.getPrice());
            newCommodityMapper.setQuantity(commodityMapper.getQuantity());
            saved.add(newCommodityMapper);
        }
        return commodityRepository.save(newCommodityMappers);
    }
    public CommodityMapper update(CommodityMapper commodityMapper){
        List<CommodityMapper> saved = new ArrayList<CommodityMapper>();
            CommodityMapper newCommodityMapper = commodityRepository.findOne(commodityMapper.getId());
            newCommodityMapper.setName(commodityMapper.getName());
            newCommodityMapper.setType(commodityMapper.getType());
            newCommodityMapper.setPrice(commodityMapper.getPrice());
            newCommodityMapper.setQuantity(commodityMapper.getQuantity());
        return commodityRepository.save(newCommodityMapper);
    }
    public void delete(List<CommodityMapper> commodityMappers){
        commodityRepository.delete(commodityMappers);

    }
    public void delete(CommodityMapper commodityMapper){
        commodityRepository.delete(commodityMapper);
    }

    public void deleteAll(){
        commodityRepository.deleteAll();
    }
    public Page<CommodityMapper> findAll(Integer currentPage, Integer pageSize){
        return commodityRepository.findAll(Common.getPageable(currentPage,pageSize));

    }


}
