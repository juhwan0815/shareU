package inu.project.shareu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import inu.project.shareu.advice.exception.CartException;
import inu.project.shareu.advice.exception.StoreException;
import inu.project.shareu.domain.*;
import inu.project.shareu.model.store.StoreDto;
import inu.project.shareu.repository.CartRepository;
import inu.project.shareu.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final AmazonS3Client amazonS3Client;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 족보의 파일 저장
     * 1. UUID를 이용하여 고유한 저장이름 생성
     * 2. S3에 저장
     * 3. 변환한 파일 삭제
     * 4. S3의 파일 접근 경로를 얻어낸다.
     * 5. 저장내역 생성 및 저장
     */
    @Transactional
    public void saveFile(List<MultipartFile> files, Item item)  {

        for (MultipartFile file : files) {

            String storeName = UUID.randomUUID().toString() + file.getOriginalFilename();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try {
                amazonS3Client.putObject(new PutObjectRequest(bucket,storeName,file.getInputStream(),objectMetadata)
                              .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new StoreException("파일 업로드 실패");
            }

            Store store = Store.creatStore(file, storeName,item);
            storeRepository.save(store);
        }
    }

    /**
     * 족보의 파일 삭제
     * 1. 족보의 파일 조회
     * 2. 파일 삭제
     * 3. 파일 저장내역 모두 삭제
     */
    @Transactional
    public void deleteStores(Item item){

        List<Store> stores = storeRepository.findByItem(item);

        stores.forEach(store -> {
            deleteS3Object(store);
            storeRepository.delete(store);
        });
    }

    /**
     * 파일 단건 삭제
     * 1. 저장내역 조회
     * 2. 로그인한 사용자가 파일의 주인 여부 확인
     * 3. S3 파일 삭제
     * 4. 저장 내역 삭제
     */
    @Transactional
    public void deleteStore(Member member, Long storeId){

        Store store = storeRepository.findWithItemById(storeId)
                .orElseThrow(() -> new StoreException("존재하지 않는 파일입니다."));

        validateStoreOwner(member, store);

        deleteS3Object(store);
        storeRepository.delete(store);
    }

    /**
     * 파일 다운
     * 1. 파일 저장 내역 조회
     * 2. 족보 구매 여부 확인
     * 3. S3에서 파일을 가져오고 바이트로 변환
     * @Return byte[]
     */
    public StoreDto downloadFile(Member member, Long storeId){

        Store store = storeRepository.findWithItemById(storeId)
                .orElseThrow(() -> new StoreException("존재하지 않는 파일입니다."));

        validateItemPurchase(member, store.getItem());

        return new StoreDto(store,fileDownload(store));
    }

    /**
     * 관리자 파일 다운
     * 1. 파일 저장 내역 조회
     * 3. S3에서 파일을 가져오고 바이트로 변환
     * @Return byte[]
     */
    public StoreDto downloadFileByAdmin(Long storeId) {

        Store store = storeRepository.findWithItemById(storeId)
                .orElseThrow(() -> new StoreException("존재하지 않는 파일입니다."));

        return new StoreDto(store,fileDownload(store));
    }

    /**
     * S3에서 파일을 가져오고 바이트로 변환
     */
    private byte[] fileDownload(Store store) {
        if(amazonS3Client.doesObjectExist(bucket, store.getFileStoreName())){
            S3Object s3Object = amazonS3Client.getObject(bucket, store.getFileStoreName());
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            try {
                byte[] contents = IOUtils.toByteArray(inputStream);
                return contents;
            } catch (IOException e) {
                throw new StoreException("파일을 서버로 가져오는 중 오류 발생");
            }

        }
        return null;
    }

    /**
     * 현재 로그인한 사용자가 족보를 구매했는지 확인
     * 족보를 구매하지 않았거나 족보의 판매자가 아니면 오류
     */
    private void validateItemPurchase(Member member, Item item) {
        List<Cart> carts = cartRepository.findByMemberAndItemAndCartStatus(member, item, CartStatus.ORDER);

        if(carts.isEmpty()){
            if(!item.getMember().getId().equals(member.getId())){
                throw new CartException("상품을 구매하지 않으면 다운로드 할 수 없습니다.");
            }
        }
    }

    /**
     * S3 에 파일이 존재하면 삭제
     */
    private void deleteS3Object(Store store) {
        if (amazonS3Client.doesObjectExist(bucket, store.getFileStoreName())) {
            amazonS3Client.deleteObject(bucket, store.getFileStoreName());
        }
    }

    /**
     * 로그인한 사용자가 파일의 주인 여부 확인
     */
    private void validateStoreOwner(Member member, Store store) {
        if(member.getId().equals(store.getItem().getMember().getId())){
            throw new StoreException("파일을 수정할 권한이 없습니다.");
        }
    }



}
