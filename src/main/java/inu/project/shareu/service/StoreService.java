package inu.project.shareu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import inu.project.shareu.advice.exception.CartException;
import inu.project.shareu.advice.exception.StoreException;
import inu.project.shareu.domain.Cart;
import inu.project.shareu.domain.Item;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.Store;
import inu.project.shareu.repository.StoreRepository;
import inu.project.shareu.repository.query.CartQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private final CartQueryRepository cartQueryRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 족보의 파일 저장
     * 1. MultiPartFile -> File로 변환
     * 2. UUID를 이용하여 고유한 저장이름 생성
     * 3. S3에 저장
     * 4. 변환한 파일 삭제
     * 5. S3의 파일 접근 경로를 얻어낸다.
     * 6. 저장내역 생성 및 저장
     */
    @Transactional
    public void saveFile(List<MultipartFile> files, Item item)  {

        for (MultipartFile file : files) {

            File uploadFile = null;

            try {
                uploadFile = convert(file)
                        .orElseThrow(() -> new StoreException("MultipartFile -> File로 전환이 실패했습니다."));
            } catch (IOException e) {
                throw new StoreException("파일 변환 실패");
            }

            String storeName = UUID.randomUUID().toString() + file.getOriginalFilename();

            amazonS3Client.putObject(new PutObjectRequest(bucket,storeName,uploadFile)
                          .withCannedAcl(CannedAccessControlList.PublicRead));

            removeNewFile(uploadFile);

            String resourcePath = amazonS3Client.getUrl(bucket, storeName).toString();

            Store store = Store.creatStore(file, storeName, resourcePath,item);
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

    public Store findStore(Member member, String storeName){
        // TODO 족보의 주인도 다운이 가능하게끔

        Store store = storeRepository.findWithItemByFileStoreName(storeName)
                .orElseThrow(() -> new StoreException("존재하지 않는 파일입니다."));

        List<Cart> carts = cartQueryRepository.findCartByItemAndMemberId(store.getItem(), memberId);

        if(carts.isEmpty()){
           throw new CartException("상품을 구매하지 않으면 다운로드 할 수 없습니다.");
        }

        return store;
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

    /**
     * 변환 파일 삭제
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    /**
     * MultiPartFile -> File로 변환
     */
    private Optional<File> convert(MultipartFile file) throws IOException {

        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


}
