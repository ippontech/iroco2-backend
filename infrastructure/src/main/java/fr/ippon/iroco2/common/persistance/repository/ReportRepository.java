package fr.ippon.iroco2.common.persistance.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReportRepository<T> extends JpaRepository<T, UUID> {
    List<T> findByOwnerOrderByCreationDateAsc(String owner);
}
