package basicapp.back.business.history.repository;

import basicapp.back.business.history.model.HistoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IHistoryLogRepository
    extends JpaRepository<HistoryLog, Long>, JpaSpecificationExecutor<HistoryLog> {}
