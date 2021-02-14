package inu.project.shareu.service;

import inu.project.shareu.advice.exception.CartException;
import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.advice.exception.OrderException;
import inu.project.shareu.domain.*;
import inu.project.shareu.repository.ItemRepository;
import inu.project.shareu.repository.MemberRepository;
import inu.project.shareu.repository.OrderRepository;
import inu.project.shareu.repository.PointRepository;
import inu.project.shareu.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final PointRepository pointRepository;

    @Transactional
    public void saveBulkOrder(Long memberId) {
        // TODO 수정예정
        Member loginMember = memberQueryRepository.findMemberWithCurrentCartsAndItemById(memberId);
        if(loginMember == null){
            throw new CartException("현재 장바구니에 족보가 존재하지 않습니다.");
        }

        List<Cart> currentCarts = loginMember.getCarts();

        List<Point> pointList = currentCarts.stream()
                .map(cart -> Point.createPoint("족보 구매", -3,
                        cart.getItem(), loginMember))
                .collect(Collectors.toList());


        Order order = Order.createBulkOrder(loginMember, currentCarts);

        pointList.forEach(point -> pointRepository.save(point));
        orderRepository.save(order);
    }

    @Transactional
    public void saveSingleOrder(Long memberId, Long itemId) {

        Member loginMember = memberRepository.findWithCartById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        if(!item.getItemstatus().equals(ItemStatus.SALE)){
            throw new ItemException("판매가 중지된 족보입니다.");
        }

        if(item.getMember().equals(loginMember)){
            throw new OrderException("내가 등록한 족보는 구매할 수 없습니다.");
        }

        List<Cart> carts = loginMember.getCarts();

        // TODO 장바구니 구매하지 않고 존재하면 이걸 그대로 결제
        Cart orderCart = null;

        for (Cart cart : carts) {
            if(cart.getItem().equals(item) && cart.getCartStatus().equals(CartStatus.ORDER)){
                throw new CartException("이미 구매한 족보입니다.");
            }
            if(cart.getItem().equals(item) && cart.getCartStatus().equals(CartStatus.CART)){
                orderCart = cart;
                break;
            }
        }

        if(orderCart == null){
            orderCart = Cart.createCart(loginMember,item);
        }

        Order order = Order.createSingleOrder(loginMember, orderCart);

        Point point = Point.createPoint("족보 구매", -3, item, loginMember);

        pointRepository.save(point);
        orderRepository.save(order);
    }

}
