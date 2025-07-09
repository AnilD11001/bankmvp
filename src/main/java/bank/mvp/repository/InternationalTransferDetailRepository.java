package bank.mvp.repository;

import bank.mvp.entity.Bank;
import bank.mvp.entity.InternationalTransferDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternationalTransferDetailRepository extends JpaRepository<InternationalTransferDetail, Long> {
}
