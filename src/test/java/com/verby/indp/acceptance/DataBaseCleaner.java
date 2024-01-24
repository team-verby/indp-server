package com.verby.indp.acceptance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.Type;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataBaseCleaner {

    private final EntityManager entityManager;
    private final List<String> tableNames;

    public DataBaseCleaner(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.tableNames = entityManager.getMetamodel()
            .getEntities()
            .stream()
            .map(Type::getJavaType)
            .map(javaType -> javaType.getAnnotation(Table.class))
            .map(Table::name)
            .toList();
    }

    public void clear() {
        entityManager.flush();

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager
                .createNativeQuery("TRUNCATE TABLE " + tableName + " RESTART IDENTITY")
                .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE")
            .executeUpdate();

    }
}
