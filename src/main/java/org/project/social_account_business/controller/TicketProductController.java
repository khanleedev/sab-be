package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.ss.formula.functions.T;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.ticket_product.TicketProductDto;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.ticket_product.CreateTicketProductForm;
import org.project.social_account_business.form.ticket_product.UpdateTicketProductForm;
import org.project.social_account_business.form.ticket_product_info.CreateTicketProductInfoForm;
import org.project.social_account_business.form.ticket_product_info.UpdateTicketProductInfoForm;
import org.project.social_account_business.form.ticket_product_info.UploadTicketProductInfoForm;
import org.project.social_account_business.model.TicketProductInfo;
import org.project.social_account_business.model.criteria.TicketProductCriteria;
import org.project.social_account_business.repository.TicketProductInfoRepository;
import org.project.social_account_business.service.ExcelService;
import org.project.social_account_business.service.email.EmailService;
import org.project.social_account_business.service.ticket_product.TicketProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/ticket-products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class TicketProductController extends ABasicController {
    final TicketProductService ticketProductService;
    final TicketProductInfoRepository ticketProductInfoRepository;
    final EmailService emailService;
    final ExcelService excelService;

    public TicketProductController(TicketProductService ticketProductService, TicketProductInfoRepository ticketProductInfoRepository, EmailService emailService, ExcelService excelService) {
        this.ticketProductService = ticketProductService;
        this.ticketProductInfoRepository = ticketProductInfoRepository;
        this.emailService = emailService;
        this.excelService = excelService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> createTicketProduct(@Valid @RequestBody CreateTicketProductForm createTicketProductForm, BindingResult bindingResult) {
        log.info("Creating ticket product");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }
        ticketProductService.createTicketProduct(createTicketProductForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Ticket product created successfully"));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> updateTicketProduct(@Valid @RequestBody UpdateTicketProductForm updateTicketProductForm, BindingResult bindingResult) {
        log.info("Updating ticket product");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }
        ticketProductService.updateTicketProduct(updateTicketProductForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket product updated successfully"));
    }

    @PostMapping(value = "/upload/{tId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> uploadTicketProduct(@RequestParam("file") MultipartFile file, @PathVariable("tId") @Min(10) Long ticketId) {
        log.info("Uploading ticket product");
        ticketProductService.uploadExcelFile(file, ticketId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket product uploaded successfully"));
    }

    @DeleteMapping(value = "/{tpId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> deleteTicketProduct(@PathVariable Long tpId) {
        log.info("Deleting ticket product");
        ticketProductService.deleteTicketProduct(tpId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket product deleted successfully"));
    }

    @GetMapping(value = "/{tpId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<EntityModel<TicketProductDto>>> getTicketProduct(@PathVariable("tpId") Long tpId) {
        log.info("Getting ticket product");
        val ticketProductDto = ticketProductService.getTicketProduct(tpId);
        EntityModel<TicketProductDto> entityModel = EntityModel.of(ticketProductDto,
                linkTo(methodOn(TicketProductController.class).getTicketProduct(tpId)).withSelfRel(),
                linkTo(methodOn(TicketProductController.class).getTicketProducts(new TicketProductCriteria(), Pageable.unpaged())).withRel("ticket-products"),
                linkTo(methodOn(TicketProductController.class).createTicketProduct(new CreateTicketProductForm(), null)).withRel("create-ticket-product"),
                linkTo(methodOn(TicketProductController.class).updateTicketProduct(new UpdateTicketProductForm(), null)).withRel("update-ticket-product"),
                linkTo(methodOn(TicketProductController.class).deleteTicketProduct(tpId)).withRel("delete-ticket-product"),
                linkTo(methodOn(TicketProductController.class).uploadTicketProduct(null, tpId)).withRel("upload-ticket-product"));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket product retrieved successfully", entityModel));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<TicketProductDto>>>> getTicketProducts(TicketProductCriteria ticketProductCriteria, Pageable pageable) {
        log.info("Getting ticket products");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket products retrieved successfully", ticketProductService.getTicketProducts(ticketProductCriteria, pageable)));
    }

    @GetMapping(value = "/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ResponseListDto<List<TicketProductDto>>>> getTicketProductsByTicketId(@RequestParam("ticketId") Long ticketId, Pageable pageable) {
        log.info("Getting ticket products by ticket id");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket products retrieved successfully", ticketProductService.getTicketProductsByTicketId(ticketId, pageable)));
    }

    @PatchMapping(value = "/soft-delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> softDeleteTicketProduct(@PathVariable("id") Long tpId) {
        log.info("Soft deleting ticket product");
        ticketProductService.deactivateTicketProduct(tpId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket product soft deleted successfully"));
    }

    @PatchMapping(value = "/activate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> activateTicketProduct(@PathVariable("id") Long tpId) {
        log.info("Activating ticket product");
        ticketProductService.activateTicketProduct(tpId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket product activated successfully"));
    }

    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<TicketProductDto>>>> getActiveTicketProducts(TicketProductCriteria ticketProductCriteria, Pageable pageable) {
        log.info("Getting active ticket products");
        ticketProductCriteria.setStatus(BetaConstant.STATUS_ACTIVE);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Active ticket products retrieved successfully", ticketProductService.getTicketProducts(ticketProductCriteria, pageable)));
    }

    @PostMapping(value = "/infos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<String>> createTicketProductInfos(@Valid @RequestBody CreateTicketProductInfoForm createTicketProductInfoForm, BindingResult bindingResult){
        log.info("Creating ticket product infos");
        if (bindingResult.hasErrors()) {
            log.error("Error in creating ticket product infos: {}", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            throw new MyBindingException(bindingResult.getFieldError().getDefaultMessage());
        }
        val ticketProduct = ticketProductService.findById(createTicketProductInfoForm.getTicketProductId());
        TicketProductInfo ticketProductInfo = TicketProductInfo.builder()
                .ticketProduct(ticketProduct)
                .uid(createTicketProductInfoForm.getUid())
                .pass(createTicketProductInfoForm.getPass())
                .twoFA(createTicketProductInfoForm.getTwoFA())
                .mail(createTicketProductInfoForm.getMail())
                .passMail(createTicketProductInfoForm.getPassMail())
                .mailVerify(createTicketProductInfoForm.getMailVerify())
                .isSold(false)
                .build();

        // 3. Lưu vào DB
        ticketProductInfoRepository.save(ticketProductInfo);
        ticketProduct.setQuantity(ticketProduct.getQuantity() + 1);
        ticketProductService.save(ticketProduct);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Ticket product infos created successfully"));
    }

    @GetMapping(value = "/infos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<TicketProductInfo>>> getTicketProductInfos(@RequestParam("ticketProductId") Long ticketProductId) {
        log.info("Getting ticket product infos by ticket product ID");
        List<TicketProductInfo> infos = ticketProductInfoRepository.findAllByTicketProductId(ticketProductId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Infos retrieved successfully", infos));
    }

    @GetMapping(value = "/infos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<TicketProductInfo>> getTicketProductInfo(@PathVariable("id") Long id) {
        log.info("Getting ticket product info by ID: {}", id);
        TicketProductInfo info = ticketProductInfoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("TicketProductInfo not found"));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Info retrieved", info));
    }

    @PutMapping(value = "/infos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateTicketProductInfo(@Valid @RequestBody UpdateTicketProductInfoForm form, BindingResult bindingResult) {
        log.info("Updating ticket product info");
        if (bindingResult.hasErrors()) {
            throw new MyBindingException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        TicketProductInfo info = ticketProductInfoRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("TicketProductInfo not found"));

        info.setUid(form.getUid());
        info.setPass(form.getPass());
        info.setTwoFA(form.getTwoFA());
        info.setMail(form.getMail());
        info.setPassMail(form.getPassMail());
        info.setMailVerify(form.getMailVerify());
        ticketProductInfoRepository.save(info);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Info updated successfully"));
    }

    @PatchMapping(value = "/infos/soft-delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<String>> softDeleteTicketProductInfo(@PathVariable("id") Long id) {
        log.info("Soft deleting ticket product info with id: {}", id);
        TicketProductInfo info = ticketProductInfoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("TicketProductInfo not found"));
        info.setStatus(BetaConstant.STATUS_DELETE);
        ticketProductInfoRepository.save(info);
        val ticketProduct = info.getTicketProduct();
        ticketProduct.setQuantity(ticketProduct.getQuantity() - 1);
        ticketProductService.save(ticketProduct);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Soft deleted successfully"));
    }

    @DeleteMapping(value = "/infos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteTicketProductInfo(@PathVariable("id") Long id) {
        log.info("Deleting ticket product info with id: {}", id);
        ticketProductInfoRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Deleted successfully"));
    }

    @PostMapping(value = "/infos/upload/{tpId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<String>> uploadTicketProductInfos(
            @RequestParam("file") MultipartFile file,
            @PathVariable("tpId") Long ticketProductId) throws Exception {
        log.info("Uploading TicketProductInfos to TicketProduct id = {}", ticketProductId);

        if (!excelService.hasExcelFormat(file)) {
            throw new BadRequestException("File format must be .xlsx");
        }

        val ticketProduct = ticketProductService.findById(ticketProductId);
        List<UploadTicketProductInfoForm> infos = excelService.mapExcelToTicketProductInfos(file.getInputStream());

        List<TicketProductInfo> entities = infos.stream()
                .map(form -> TicketProductInfo.builder()
                        .ticketProduct(ticketProduct)
                        .uid(form.getUid())
                        .pass(form.getPass())
                        .twoFA(form.getTwoFA())
                        .mail(form.getMail())
                        .passMail(form.getPassMail())
                        .mailVerify(form.getMailVerify())
                        .isSold(false)
                        .build())
                .toList();

        ticketProductInfoRepository.saveAll(entities);
        ticketProduct.setQuantity(ticketProduct.getQuantity() + infos.size());

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, infos.size() + " TicketProductInfos uploaded successfully"));
    }
}
