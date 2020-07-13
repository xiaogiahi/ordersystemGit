package ordersystem.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private OrderInfoMapper orderInfo;
    private List<OrderDetailMapper> orderDetails;
}
