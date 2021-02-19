package inu.project.shareu.model.common.store;

import inu.project.shareu.domain.Store;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreDto {

    private Store store;

    private byte[] data;

}
