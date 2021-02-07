package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    private int orderTotalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    public Order(Member member,List<Cart> carts) {
        this.member = member;
        this.orderStatus = OrderStatus.ORDER;

        for (Cart cart : carts) {
            this.orderTotalPrice += cart.getOrderPrice();
        }
    }

    public Order(Member member,Cart cart) {
        this.member = member;
        this.orderStatus = OrderStatus.ORDER;
        this.orderTotalPrice = cart.getOrderPrice();
    }

    public static Order createBulkOrder(Member member, List<Cart> carts){
        Order order = new Order(member,carts);
        for (Cart cart : carts) {
            cart.order(order);
        }
        return order;
    }

    public static Order createSingleOrder(Member member, Cart cart){
        Order order = new Order(member,cart);
        cart.order(order);
        return order;
    }

}
