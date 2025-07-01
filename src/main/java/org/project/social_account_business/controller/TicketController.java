package org.project.social_account_business.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.model.Ticket;
import org.project.social_account_business.model.criteria.TicketCriteria;
import org.project.social_account_business.service.ticket.TicketService;
import org.project.social_account_business.dto.ApiResponse;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.ticket.TicketDto;
import org.project.social_account_business.exception.MyBindingException;
import org.project.social_account_business.form.ticket.CreateTicketForm;
import org.project.social_account_business.form.ticket.UpdateTicketForm;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/tickets")
@Slf4j
public class TicketController extends ABasicController {
    final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> createTicket(@Valid @RequestBody CreateTicketForm createTicketForm, BindingResult bindingResult) {
        log.info("Creating ticket");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }
        ticketService.createTicket(createTicketForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED, "Ticket created successfully"));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> updateTicket(@Valid @RequestBody UpdateTicketForm updateTicketForm, BindingResult bindingResult) {
        log.info("Updating ticket");
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasErrors()) {
                throw new MyBindingException("[AccountController] " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            }
        }
        ticketService.updateTicket(updateTicketForm);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket updated successfully"));
    }

    @DeleteMapping(value = "/{tId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<ApiResponse<T>> deleteTicket(@PathVariable("tId") @Min(10) Long ticketId) {
        log.info("Deleting ticket");
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket deleted successfully"));
    }

    @GetMapping(value = "/{tId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Ticket>> getTicket(@PathVariable("tId") @Min(10) Long ticketId) {
        log.info("Getting ticket");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Ticket retrieved successfully", ticketService.findTicketById(ticketId)));
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<TicketDto>>>> getTickets(TicketCriteria ticketCriteria, Pageable pageable) {
        log.info("Getting tickets");
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Tickets retrieved successfully", ticketService.getTickets(ticketCriteria, pageable)));
    }

    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<ResponseListDto<List<TicketDto>>>> getActiveTickets(TicketCriteria ticketCriteria, Pageable pageable) {
        log.info("Getting active tickets");
        ticketCriteria.setStatus(BetaConstant.STATUS_ACTIVE);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, "Active tickets retrieved successfully", ticketService.getTickets(ticketCriteria, pageable)));
    }
}
