package org.project.social_account_business.service.report;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ApiMessageDto;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.report.ReportDto;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.report.CreateReportForm;
import org.project.social_account_business.form.report.UpdateReportForm;
import org.project.social_account_business.mapper.AccountMapper;
import org.project.social_account_business.model.Report;
import org.project.social_account_business.repository.ReportRepository;
import org.project.social_account_business.service.account.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service("reportService")
@Slf4j
public class ReportServiceImpl implements ReportService{
    private final AccountMapper accountMapper;
    private final ReportRepository reportRepository;
    private final AccountService accountService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ReportServiceImpl(ReportRepository reportRepository, AccountService accountService,
                             AccountMapper accountMapper, SimpMessagingTemplate simpMessagingTemplate) {
        this.reportRepository = reportRepository;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    @Transactional
    public void createReport(CreateReportForm form) {
        val account = accountService.findById(form.getAccountId());
        val report = new Report();
        report.setAccount(account);
        report.setReportContent(form.getContent());
        report.setStatus(BetaConstant.STATUS_PENDING);
        reportRepository.save(report);
        log.info("Sending payment completion message to user {}", account.getId());
    }

    @Override
    @Transactional
    public void updateReport(UpdateReportForm form) {
        val report = reportRepository.findFirstById(form.getReportId()).orElseThrow(() -> new NotFoundException("Report not found", ErrorCode.REPORT_NOT_FOUND));
        report.setReportContent(form.getContent());
        report.setStatus(BetaConstant.STATUS_PENDING);
        reportRepository.save(report);
    }

    @Override
    @Transactional
    public void deleteReport(long reportId) {
        val report = reportRepository.findFirstById(reportId).orElseThrow(() -> new NotFoundException("Report not found", ErrorCode.REPORT_NOT_FOUND));
        reportRepository.delete(report);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDto getReportById(long reportId) {
        return reportRepository.findFirstById(reportId)
                .map(report -> {
                    ReportDto reportDto = new ReportDto();
                    reportDto.setId(report.getId());
                    reportDto.setAccount(accountMapper.fromEntityToAutoCompleteAccountDto(report.getAccount()));
                    reportDto.setContent(report.getReportContent());
                    reportDto.setStatus(report.getStatus());
                    return reportDto;
                })
                .orElseThrow(() -> new NotFoundException("Report not found", ErrorCode.REPORT_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseListDto<List<ReportDto>> getReportsByAccountId(Long accountId, Pageable pageable) {
        Page<Report> reportPage = reportRepository.findAllByAccountId(accountId, pageable);
        List<ReportDto> reportDtos = getActiveReports(reportPage);
        return new ResponseListDto<>(reportDtos, reportPage.getTotalElements(), reportPage.getTotalPages());
    }

    @Override
    public ResponseListDto<List<ReportDto>> getAllPendingReports(Pageable pageable) {
        Page<Report> reportPage = reportRepository.findAllByStatus(BetaConstant.STATUS_PENDING, pageable);
        List<ReportDto> reportDtos = getActiveReports(reportPage);
        return new ResponseListDto<>(reportDtos, reportPage.getTotalElements(), reportPage.getTotalPages());
    }

    @Override
    public ResponseListDto<List<ReportDto>> getAllActiveReports(Pageable pageable) {
        Page<Report> reportPage = reportRepository.findAllByStatus(BetaConstant.STATUS_ACTIVE, pageable);
        List<ReportDto> reportDtos = getActiveReports(reportPage);
        return new ResponseListDto<>(reportDtos, reportPage.getTotalElements(), reportPage.getTotalPages());
    }

    @Override
    @Transactional
    public void deactivateReport(long reportId) {
        val report = reportRepository.findFirstById(reportId).orElseThrow(() -> new NotFoundException("[ReportService] Report not found", ErrorCode.REPORT_NOT_FOUND));
        report.setStatus(BetaConstant.STATUS_PENDING);
        reportRepository.save(report);
    }

    @Override
    @Transactional
    public void activateReport(long reportId) {
        val report = reportRepository.findFirstById(reportId).orElseThrow(() -> new NotFoundException("[ReportService] Report not found", ErrorCode.REPORT_NOT_FOUND));
        report.setStatus(BetaConstant.STATUS_ACTIVE);
        reportRepository.save(report);
    }

    private List<ReportDto> getActiveReports(Page<Report> page) {
        return page.getContent().stream()
                .map(report -> {
                    ReportDto reportDto = new ReportDto();
                    reportDto.setId(report.getId());
                    reportDto.setAccount(accountMapper.fromEntityToAutoCompleteAccountDto(report.getAccount()));
                    reportDto.setContent(report.getReportContent());
                    reportDto.setStatus(report.getStatus());
                    reportDto.setCreatedDate(report.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    return reportDto;
                })
                .toList();
    }
}
