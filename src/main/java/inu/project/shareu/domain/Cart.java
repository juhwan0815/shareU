package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    private int orderPrice;

    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public static Cart createCart(Member member,Item item){
        Cart cart = new Cart();
        cart.cartStatus = CartStatus.CART;
        cart.member = member;
        cart.item = item;
        cart.orderPrice = item.getPrice();
        return cart;
    }

    public void order(Order order){
        this.order = order;
        order.getCarts().add(this);
        this.cartStatus = CartStatus.ORDER;

    }

    public void cancel(){
        this.cartStatus = CartStatus.CANCEL;
    }

}
