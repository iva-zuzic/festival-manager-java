package hr.tvz.festivalmanager.app;

import hr.tvz.festivalmanager.repository.WorkerRepository;
import hr.tvz.festivalmanager.service.*;

/**
 * Objedinjuje sve servise i ovisnosti potrebne izborniku aplikacije,
 * kako se ne bi prosljeđivali kao velik broj zasebnih parametara.
 */
public record ApplicationContext(
        MemberService memberService,
        WorkerService workerService,
        ArtistService artistService,
        StageService stageService,
        PerformanceService performanceService,
        BackupService backupService,
        XmlLogService xmlLogService,
        WorkerRepository workerRepository
) {
}