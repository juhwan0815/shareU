package inu.project.shareu;

import inu.project.shareu.domain.College;
import inu.project.shareu.service.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.initCollege();
        initService.initAdmin();
    }
}
