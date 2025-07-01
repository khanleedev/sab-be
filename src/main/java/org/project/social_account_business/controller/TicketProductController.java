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
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.ticket_product.CreateTicketProductForm;
import org.project.social_account_business.form.ticket_product.UpdateTicketProductForm;
import org.project.social_account_business.model.criteria.TicketProductCriteria;
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
    final EmailService emailService;

    public TicketProductController(TicketProductService ticketProductService, EmailService emailService) {
        this.ticketProductService = ticketProductService;
        this.emailService = emailService;
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
}
