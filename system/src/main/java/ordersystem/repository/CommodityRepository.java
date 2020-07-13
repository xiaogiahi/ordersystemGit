package ordersystem.repository;

import ordersystem.mapper.CommodityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityRepository extends JpaRepository<CommodityMapper,Long> {
    Page<CommodityMapper> findByNameAndType(String name, String type, Pageable pageable);
    Page<CommodityMapper> findByNameLike(String name,Pageable pageable);

}
