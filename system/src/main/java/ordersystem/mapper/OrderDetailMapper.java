package ordersystem.mapper;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetailMapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "oid")
    @JsonBackReference
    private OrderInfoMapper orderInfoMapper;
    @ManyToOne
    @JoinColumn(name = "cid")
    private CommodityMapper commodityMapper;
    private double price;
    private double quantity;

}
