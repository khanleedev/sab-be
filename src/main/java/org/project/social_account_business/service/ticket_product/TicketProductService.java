package org.project.social_account_business.service.ticket_product;

import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.ticket_product.TicketProductDto;
import org.project.social_account_business.form.ticket_product.CreateTicketProductForm;
import org.project.social_account_business.form.ticket_product.UpdateTicketProductForm;
import org.project.social_account_business.model.TicketProduct;
import org.project.social_account_business.model.criteria.TicketProductCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketProductService {
    /**
     * Create a new ticket product using the provided form.
     *
     * @param createTicketProductForm the form containing details for the new ticket product
     */
    public void createTicketProduct(CreateTicketProductForm createTicketProductForm);

    /**
     * Update an existing ticket product using the provided form.
     *
     * @param updateTicketProductForm the form containing updated details for the ticket product
     */
    public void updateTicketProduct(UpdateTicketProductForm updateTicketProductForm);

    /**
     * Retrieve the details of a ticket product by its ID.
     *
     * @param ticketProductId the ID of the ticket product to retrieve
     * @return the DTO containing the ticket product details
     */
    TicketProductDto getTicketProduct(Long ticketProductId);

    /**
     * Delete a ticket product by its ID.
     *
     * @param ticketProductId the ID of the ticket product to delete
     */
    public void deleteTicketProduct(Long ticketProductId);

    /**
     * Retrieve a paginated list of ticket products based on criteria.
     *
     * @param ticketProductCriteria the criteria for filtering ticket products
     * @param pageable              the pagination information
     * @return a response containing the list of ticket product DTOs and pagination details
     */
    public ResponseListDto<List<TicketProductDto>> getTicketProducts(TicketProductCriteria ticketProductCriteria, Pageable pageable);

    /**
     * Upload an Excel file and associate its data with a specific ticket.
     *
     * @param multipartFile the Excel file to upload
     * @param ticketId      the ID of the ticket to associate with the uploaded data
     */
    public void uploadExcelFile(MultipartFile multipartFile, Long ticketId);

    /**
     * Find a ticket product by its ID.
     *
     * @param id the ID of the ticket product to find
     * @return the ticket product entity
     */
    TicketProduct findById(Long id);

    /**
     * Save a ticket product entity to the database.
     *
     * @param ticketProduct the ticket product entity to save
     */
    void save(TicketProduct ticketProduct);

    /**
     * Deactivate a ticket product by its ID.
     * @param ticketProductId
     */
    void deactivateTicketProduct(long ticketProductId);

    /**
     * Activate a ticket product by its ID.
     * @param ticketProductId
     */
    void activateTicketProduct(long ticketProductId);

    /**
     * Retrieve a paginated list of ticket products associated with a specific ticket ID.
     *
     * @param ticketId the ID of the ticket to filter ticket products
     * @param pageable the pagination information
     * @return a response containing the list of ticket product DTOs and pagination details
     */
    ResponseListDto<List<TicketProductDto>> getTicketProductsByTicketId(long ticketId, Pageable pageable);
}
