package org.project.social_account_business.service.report;

import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.report.ReportDto;
import org.project.social_account_business.form.report.CreateReportForm;
import org.project.social_account_business.form.report.UpdateReportForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    void createReport(CreateReportForm form);
    void updateReport(UpdateReportForm form);
    void deleteReport(long reportId);
    ReportDto getReportById(long reportId);
    ResponseListDto<List<ReportDto>> getReportsByAccountId(Long accountId, Pageable pageable);
    ResponseListDto<List<ReportDto>> getAllPendingReports(Pageable pageable);
    ResponseListDto<List<ReportDto>> getAllActiveReports(Pageable pageable);

    void deactivateReport(long reportId);
    void activateReport(long reportId);
}
