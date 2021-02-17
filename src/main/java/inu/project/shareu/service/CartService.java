package inu.project.shareu.service;

import inu.project.shareu.advice.exception.CartException;
import inu.project.shareu.advice.exception.ItemException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.domain.Cart;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.ItemStatus;
import inu.project.shareu.domain.Member;
import inu.project.shareu.model.request.cart.CartSaveRequest;
import inu.project.shareu.repository.CartRepository;
import inu.project.shareu.repository.ItemRepository;
import inu.project.shareu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void saveCart(Long memberId, CartSaveRequest cartSaveRequest) {

        Member loginMember = memberRepository.findWithCartById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(cartSaveRequest.getItemId())
                .orElseThrow(() -> new ItemException("존재하지 않는 족보입니다."));

        if(!item.getItemstatus().equals(ItemStatus.SALE)){
            throw new ItemException("판매가 중지된 족보입니다.");
        }

        if(loginMember.equals(item.getMember())){
            throw new CartException("자신이 등록한 족보는 장바구니에 담을 수 없습니다.");
        }

        loginMember.getCarts().forEach(cart -> {
            if(cart.getItem().equals(item)){
                throw new CartException("이미 장바구니에 존재하거나 구매한 족보입니다.");
            }
        });

        Cart cart = Cart.createCart(loginMember, item);

        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCart(Long memberId, Long cartId) {

        Member loginMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        Cart cart = cartRepository.findWithMemberById(cartId)
                .orElseThrow(() -> new CartException("존재하지 않는 장바구니입니다."));

        if(!cart.getMember().equals(loginMember)){
            throw new MemberException("다른 회원의 장바구니를 삭제할 수 없습니다.");
        }

        cartRepository.delete(cart);
    }
}
