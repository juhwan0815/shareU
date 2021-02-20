package inu.project.shareu.service.query;

import inu.project.shareu.domain.Cart;
import inu.project.shareu.domain.CartStatus;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.response.cart.CartResponse;
import inu.project.shareu.model.response.item.ItemOrderResponse;
import inu.project.shareu.repository.query.CartQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartQueryService {

    private final CartQueryRepository cartQueryRepository;

    /**
     * 장바구니에 담겨있는 족보 페이징 조회
     * 1. 장바구니 페이징 조회
     * 2. DTO로 변환하여 반환
     * @return Page<CartResponse>
     */
    public Page<CartResponse> findPage(Member member, Pageable pageable){
        Page<Cart> carts = cartQueryRepository
                .findWithItemAndLectureByMemberAndCartStatus(member, CartStatus.CART, pageable);
        return carts.map(cart -> new CartResponse(cart,cart.getItem(),cart.getItem().getLecture()));
    }

    /**
     * 내가 구매한 족보 페이징 조회
     * 1. 내가 구매한 족보 페이징 조회
     * 2. DTO로 변환하여 반환
     * @return Page<ItemOrderResponse>
     */
    public Page<ItemOrderResponse> findOrderItemPage(Member member, Pageable pageable) {

        Page<Cart> carts = cartQueryRepository
                .findWithItemAndLectureByMemberAndCartStatus(member, CartStatus.ORDER, pageable);

        return carts.map(cart -> new ItemOrderResponse(cart,cart.getItem(),cart.getItem().getLecture()));
    }
}
