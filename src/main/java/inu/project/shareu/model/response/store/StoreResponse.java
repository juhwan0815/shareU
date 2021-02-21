package inu.project.shareu.model.response.store;

import inu.project.shareu.domain.Store;
import lombok.Data;

@Data
public class StoreResponse {

    private Long storeId;

    private String fileOriginalName;

    private String fileStoreName;

    public StoreResponse(Store store) {
        this.storeId = store.getId();
        this.fileOriginalName = store.getFileOriginalName();
        this.fileStoreName = store.getFileStoreName();
    }
}
