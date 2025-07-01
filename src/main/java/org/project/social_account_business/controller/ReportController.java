package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.ss.formula.functions.T;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.report.ReportDto;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.report.CreateReportForm;
import org.project.social_account_business.form.report.UpdateReportForm;
import org.project.social_account_business.service.report.ReportService;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/reports")
@Slf4j
public class ReportController extends ABasicController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<T>> createReport(@Valid @RequestBody CreateReportForm createReportForm, BindingResult bindingResult) {
        log.info("Creating report");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[ReportController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }

        reportService.createReport(createReportForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Report created successfully"));
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> updateReport(@Valid @RequestBody UpdateReportForm updateReportForm, BindingResult bindingResult) {
        log.info("Updating report");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[ReportController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }

        reportService.updateReport(updateReportForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Report updated successfully"));
    }

    @DeleteMapping(value = "/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> deleteReport(@PathVariable Long reportId) {
        log.info("Deleting report");
        reportService.deleteReport(reportId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Report deleted successfully"));
    }

    @GetMapping(value = "/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ApiResponse<ReportDto>> getReport(@PathVariable Long reportId) {
        log.info("Getting report");
        val report = reportService.getReportById(reportId);
        EntityModel<ReportDto> model = EntityModel.of(report, linkTo(ReportController.class).slash(reportId).withSelfRel(),
                linkTo(ReportController.class).slash("pending").withRel("pending"),
                linkTo(methodOn(ReportController.class).createReport(new CreateReportForm(), null)).withRel("create"),
                linkTo(methodOn(ReportController.class).updateReport(new UpdateReportForm(), null)).withRel("update"),
                linkTo(methodOn(ReportController.class).getPendingReports(null)).withRel("pending-reports"),
                linkTo(methodOn(ReportController.class).getResolvedReports(null)).withRel("resolved-reports"));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Get report successfully", report));
    }

    @GetMapping(value = "/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ResponseListDto<List<ReportDto>>>> getPendingReports(Pageable pageable) {
        log.info("Getting pending reports");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Get pending reports successfully", reportService.getAllPendingReports(pageable)));
    }

    @GetMapping(value = "/resolved", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<ReportDto>>>> getResolvedReports(Pageable pageable) {
        log.info("Getting resolved reports");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Get resolved reports successfully", reportService.getAllActiveReports(pageable)));
    }
}
