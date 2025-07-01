package org.project.social_account_business.service.ticket;

import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.ticket.TicketDto;
import org.project.social_account_business.form.ticket.CreateTicketForm;
import org.project.social_account_business.form.ticket.UpdateTicketForm;
import org.project.social_account_business.model.Ticket;
import org.project.social_account_business.model.criteria.TicketCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {
    /**
     * Save a ticket entity to the database.
     *
     * @param ticket the ticket entity to save
     */
    void save(Ticket ticket);

    /**
     * Find a ticket by its ID.
     *
     * @param ticketId the ID of the ticket to find
     * @return the ticket entity with the specified ID
     */
    Ticket findTicketById(Long ticketId);

    /**
     * Create a new ticket using the provided form.
     *
     * @param createTicketForm the form containing details for the new ticket
     */
    void createTicket(CreateTicketForm createTicketForm);

    /**
     * Update an existing ticket using the provided form.
     *
     * @param updateTicketForm the form containing updated details for the ticket
     */
    void updateTicket(UpdateTicketForm updateTicketForm);

    /**
     * Delete a ticket by its ID.
     *
     * @param ticketId the ID of the ticket to delete
     */
    void deleteTicket(Long ticketId);

    /**
     * Retrieve a paginated list of tickets based on criteria.
     *
     * @param ticketCriteria the criteria for filtering tickets
     * @param pageable the pagination information
     * @return a response containing the list of ticket DTOs and pagination details
     */
    ResponseListDto<List<TicketDto>> getTickets(TicketCriteria ticketCriteria, Pageable pageable);

    /**
     * Deactivate a ticket by its ID.
     * @param ticketId
     */
    void deactivateTicket(long ticketId);

    /**
     * Activate a ticket by its ID.
     * @param ticketId
     */
    void activateTicket(long ticketId);
}
