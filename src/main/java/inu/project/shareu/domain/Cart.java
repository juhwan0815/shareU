package inu.project.shareu.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
        Cart cart = Cart.builder()
                .cartStatus(CartStatus.CART)
                .member(member)
                .item(item)
                .orderPrice(item.getPrice())
                .build();

        return cart;
    }

    public void order(Order order){
        this.order = order;
        order.getCarts().add(this);
        this.cartStatus = CartStatus.ORDER;


    }



}
