package ordersystem.mapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_info")
public class OrderInfoMapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String customerName;
    private String address;
    private double totalPrice;
    @DateTimeFormat(pattern = "yyyy-MM-ss HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-ss HH:mm:ss", timezone = "GMT+8")
    private Date date;

}
