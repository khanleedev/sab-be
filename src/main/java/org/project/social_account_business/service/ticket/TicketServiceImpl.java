package org.project.social_account_business.service.ticket;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.ticket.TicketDto;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.ticket.CreateTicketForm;
import org.project.social_account_business.form.ticket.UpdateTicketForm;
import org.project.social_account_business.model.Ticket;
import org.project.social_account_business.model.criteria.TicketCriteria;
import org.project.social_account_business.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public void save(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket findTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(
                () -> new NotFoundException("[TicketService] ❌Ticket not found.", ErrorCode.TICKET_NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public void createTicket(CreateTicketForm createTicketForm) {
        val ticket = new Ticket();
        ticket.setTitle(createTicketForm.getTitle());
        log.info("Ticket created: {}", ticket);
        ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public void updateTicket(UpdateTicketForm updateTicketForm) {
        val ticket = ticketRepository.findById(updateTicketForm.getId()).orElseThrow(
                () -> new NotFoundException("[TicketService] ❌Ticket not found.", ErrorCode.TICKET_NOT_FOUND)
        );
        ticket.setTitle(updateTicketForm.getTitle());
        log.info("Ticket updated: {}", ticket);
        ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new NotFoundException("[TicketService] ❌Ticket not found.", ErrorCode.TICKET_NOT_FOUND)
        );
        if(!ticket.getTicketProducts().isEmpty()){
            throw new BadRequestException("[TicketService] ❌Ticket has ticket products.", ErrorCode.TICKET_HAS_TICKET_PRODUCTS);
        }
        log.info("Ticket deleted: {}", ticket);
        ticketRepository.delete(ticket);
    }

    private TicketDto fromEntityToDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setTitle(ticket.getTitle());
        ticketDto.setStatus(ticket.getStatus());
        return ticketDto;
    }

    @Override
    public ResponseListDto<List<TicketDto>> getTickets(TicketCriteria ticketCriteria, Pageable pageable) {
        Page<Ticket> ticketList = ticketRepository.findAll(ticketCriteria.toSpecification(), pageable);
        return new ResponseListDto<>(ticketList.stream().map(this::fromEntityToDto).toList(), ticketList.getTotalElements(), ticketList.getTotalPages());
    }

    @Override
    public void deactivateTicket(long ticketId) {
        val ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new NotFoundException("[TicketService] ❌Ticket not found.", ErrorCode.TICKET_NOT_FOUND)
        );
        ticket.setStatus(0);
        ticketRepository.save(ticket);
    }

    @Override
    public void activateTicket(long ticketId) {
        val ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new NotFoundException("[TicketService] ❌Ticket not found.", ErrorCode.TICKET_NOT_FOUND)
        );
        ticket.setStatus(1);
        ticketRepository.save(ticket);
    }
}
