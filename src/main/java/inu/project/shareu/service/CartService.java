package inu.project.shareu.service;

import inu.project.shareu.advice.exception.CartException;
import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.Cart;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.ItemStatus;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.cart.request.CartSaveRequest;
import inu.project.shareu.repository.CartRepository;
import inu.project.shareu.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    /**
     * 장바구니 저장
     * 1. 족보 조회
     * 2. 족보 판매 여부 확인
     * 3. 족보의 판매자인지 확인
     * 4. 사용자의 장바구니에 족보가 존재하는지 확인
     * 5. 장바구니 생성 및 저장
     */
    @Transactional
    public void saveCart(Member member, CartSaveRequest cartSaveRequest) {

        Item item = itemRepository.findById(cartSaveRequest.getItemId())
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        validateItemStatus(item);

        validateMemberRegisteredItem(member, item);

        validateDuplicatedCart(member, item);

        Cart cart = Cart.createCart(member, item);
        cartRepository.save(cart);
    }

    /**
     * 장바구니 삭제
     * 1. 장바구니 조회
     * 2. 장바구니의 주인인지 확인
     * 3. 장바구니 삭제
     */
    @Transactional
    public void deleteCart(Member member, Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartException("존재하지 않는 장바구니입니다."));

        validateCartOwner(member, cart);

        cartRepository.delete(cart);
    }

    /**
     * 장바구니의 주인인지 확인
     */
    private void validateCartOwner(Member member, Cart cart) {
        if(!cart.getMember().getId().equals(member.getId())){
            throw new MemberException("다른 회원의 장바구니를 삭제할 수 없습니다.");
        }
    }

    /**
     * 사용자의 장바구니에 족보가 존재하는지 확인
     */
    private void validateDuplicatedCart(Member member, Item item) {
        cartRepository.findByMemberAndItem(member, item).forEach(cart -> {
            if (cart.getItem().equals(item)){
                throw new CartException("이미 장바구니에 존재하거나 구매한 족보입니다.");
            }
        });
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
     * 족보 판매 여부 확인
     */
    private void validateItemStatus(Item item) {
        if(!item.getItemstatus().equals(ItemStatus.SALE)){
            throw new ItemException("판매가 중지된 족보입니다.");
        }
    }

}
