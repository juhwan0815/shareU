package inu.project.shareu.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public JPAQueryFactory queryFactory(EntityManager em){
        return new JPAQueryFactory(em);
    }
}
