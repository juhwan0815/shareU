package inu.project.shareu.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    private String fileOriginalName;

    private String fileStoreName;

    private Long fileSize;

    private String fileContentType;

    @Column(columnDefinition = "TEXT")
    private String resourcePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public static Store creatStore(MultipartFile file, String storeName,
                                   String resourcePath,Item item){
        Store store = new Store();
        store.fileOriginalName = file.getOriginalFilename();
        store.fileStoreName = storeName;
        store.fileContentType = file.getContentType();
        store.fileSize = file.getSize();
        store.resourcePath = resourcePath;
        store.item = item;
        return store;
    }
}
