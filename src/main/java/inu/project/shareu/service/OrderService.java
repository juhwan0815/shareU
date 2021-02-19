package inu.project.shareu.service;

import inu.project.shareu.advice.exception.CartException;
import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.advice.exception.OrderException;
import inu.project.shareu.domain.*;
import inu.project.shareu.repository.*;
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
    private final PointRepository pointRepository;
    private final CartRepository cartRepository;

    /**
     * 장바구니 족보 모두 구매
     * 1. 회원 조회
     * 2. 구매 전 장바구니를 모두 조회
     * 3. 장바구니에 족보가 존재하는지 여부 확인
     * 4. 포인트 생성 리스트 생성 및 저장
     * 5. 구매 생성 및 저장
     */
    @Transactional
    public void saveBulkOrder(Member loginMember) {

        Member member= memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        List<Cart> carts = cartRepository.findByMemberAndCartStatus(member, CartStatus.CART);

        validateCartsExist(carts);

        List<Point> pointList = carts.stream()
                .map(cart -> Point.createPoint("족보 구매", -3,
                                                cart.getItem(), member))
                .collect(Collectors.toList());
        pointList.forEach(point -> pointRepository.save(point));

        Order order = Order.createBulkOrder(member, carts);
        orderRepository.save(order);
    }



    /**
     * 족보 단건 구매
     * 1. 회원 조회
     * 2. 족보 조회
     * 3. 족보 판매 여부 확인
     * 4. 현재 로그인 사용자가 족보 판매자인지 확인
     * 5. 족보를 이미 구매 -> 오류 or 장바구니 -> 장바구니에 있는 것을 구매
     * 6. 구매 생성 및 저장
     * 7. 포인트 이력 생성 및 저장
     */
    @Transactional
    public void saveSingleOrder(Member loginMember, Long itemId) {

        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        validateItemStatus(item);

        validateMemberRegisteredItem(member,item);

        Cart orderCart = checkDuplicatedCartAndCreateCart(member, item);

        Order order = Order.createSingleOrder(member, orderCart);
        orderRepository.save(order);

        Point point = Point.createPoint("족보 구매", -3, item, member);
        pointRepository.save(point);
    }

    /**
     * 족보를 이미 구매하였거나 장바구니에 담겨있는지 확인
     * -> 장바구니에 담겨있으면 장바구니에 담겨있는 것을 가져온다.
     * -> 구매한적이 없거나 장바구니에도 없으면 장바구니 생성
     * @Return Cart
     */
    private Cart checkDuplicatedCartAndCreateCart(Member member, Item item) {

        List<Cart> carts = cartRepository.findByMemberAndItem(member, item);
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
            orderCart = Cart.createCart(member, item);
        }

        return orderCart;
    }

    /**
     * 족보 판매 여부 확인
     */
    private void validateItemStatus(Item item) {
        if(!item.getItemstatus().equals(ItemStatus.SALE)){
            throw new ItemException("판매가 중지된 족보입니다.");
        }
    }

    /**
     * 족보의 판매자인지 확인
     */
    private void validateMemberRegisteredItem(Member member, Item item) {
        if(member.getId().equals(item.getMember().getId())){
            throw new CartException("자신이 등록한 족보는 장바구니에 담을 수 없습니다.");
        }
    }

    /**
     * 장바구니에 족보가 존재하는지 여부 확인
     */
    private void validateCartsExist(List<Cart> carts) {
        if(carts.isEmpty()){
            throw new CartException("장바구니에 담긴 족보가 존재하지 않습니다.");
        }
    }
}
