package com.food.order.system.common.data.access.entity;


import lombok.*;


import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_restaurant_m_view", schema = "restaurant")
@Getter
@Setter
@Builder
@IdClass(RestaurantEntityId.class)
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {

    @Id
    private UUID restaurantId;

    @Id
    private UUID productId;

    private String restaurantName;

    private Boolean restaurantActive;

    private String productName;

    private BigDecimal productPrice;

    private Boolean productAvailable;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RestaurantEntity that))
            return false;
        if (!restaurantId.equals(that.restaurantId))
            return false;
        return productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = restaurantId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }
}
