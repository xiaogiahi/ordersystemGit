package ordersystem.repository;

import ordersystem.mapper.OrderInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfoMapper,Long>, JpaSpecificationExecutor {
    Page<OrderInfoMapper> findAll(Specification specification, Pageable pageable);

}
