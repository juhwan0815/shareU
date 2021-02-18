package inu.project.shareu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import inu.project.shareu.advice.exception.CartException;
import inu.project.shareu.advice.exception.StoreException;
import inu.project.shareu.domain.Cart;
import inu.project.shareu.domain.Item;
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

    @Transactional
    public void deleteStores(Item item){

        List<Store> stores = storeRepository.findByItem(item);

        stores.forEach(store -> {
            if(amazonS3Client.doesObjectExist(bucket, store.getFileStoreName())){
                amazonS3Client.deleteObject(bucket,store.getFileStoreName());
            }

            storeRepository.delete(store);
        });
    }

    @Transactional
    public void deleteStore(Long storeId){

        // TODO 족보와 작성자를 찾아서 비교

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException("존재하지 않는 파일입니다."));

         if(amazonS3Client.doesObjectExist(bucket, store.getFileStoreName())){
             amazonS3Client.deleteObject(bucket,store.getFileStoreName());
         }

         storeRepository.delete(store);
    }

    @Transactional
    public void deleteStore(Store store){

        if(amazonS3Client.doesObjectExist(bucket,store.getFileStoreName())){
            amazonS3Client.deleteObject(bucket,store.getFileStoreName());
        }
        storeRepository.delete(store);
    }


    public Store findStore(Long memberId, String storeName){
        // TODO 족보의 주인도 다운이 가능하게끔

        Store store = storeRepository.findWithItemByFileStoreName(storeName)
                .orElseThrow(() -> new StoreException("존재하지 않는 파일입니다."));

        List<Cart> carts = cartQueryRepository.findCartByItemAndMemberId(store.getItem(), memberId);

        if(carts.isEmpty()){
           throw new CartException("상품을 구매하지 않으면 다운로드 할 수 없습니다.");
        }

        return store;
    }


    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

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
