package org.project.social_account_business.service.ticket_product;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.project.social_account_business.constant.BetaConstant;
import org.project.social_account_business.constant.ErrorCode;
import org.project.social_account_business.dto.ResponseListDto;
import org.project.social_account_business.dto.ticket_product.TicketProductDto;
import org.project.social_account_business.exception.BadRequestException;
import org.project.social_account_business.exception.NotFoundException;
import org.project.social_account_business.form.UploadItemProductForm;
import org.project.social_account_business.form.ticket_product.CreateTicketProductForm;
import org.project.social_account_business.form.ticket_product.UpdateTicketProductForm;
import org.project.social_account_business.mapper.TicketProductMapper;
import org.project.social_account_business.model.Ticket;
import org.project.social_account_business.model.TicketProduct;
import org.project.social_account_business.model.criteria.TicketProductCriteria;
import org.project.social_account_business.repository.TicketProductRepository;
import org.project.social_account_business.service.ExcelService;
import org.project.social_account_business.service.ticket.TicketService;
import org.project.social_account_business.utils.DateUtils;
import org.project.social_account_business.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class TicketProductImpl implements TicketProductService {
    private static final String TICKET_PRODUCT_PREFIX = "TP";
    final TicketProductRepository ticketProductRepository;
    final TicketProductMapper ticketProductMapper;
    final TicketService ticketService;
    final ExcelService excelService;

    public TicketProductImpl(TicketProductRepository ticketProductRepository, TicketProductMapper ticketProductMapper, TicketService ticketService, ExcelService excelService) {
        this.ticketProductRepository = ticketProductRepository;
        this.ticketProductMapper = ticketProductMapper;
        this.ticketService = ticketService;
        this.excelService = excelService;
    }

    @Override
    @Transactional
    public void createTicketProduct(CreateTicketProductForm createTicketProductForm) {
        val ticket = ticketService.findTicketById(createTicketProductForm.getTicketId());
        TicketProduct ticketProduct = ticketProductMapper.fromCreateTicketProductFormToEntity(createTicketProductForm);
        ticketProduct.setTicket(ticket);
        ticketProduct.setItemCode(Utils.generateCode(TICKET_PRODUCT_PREFIX, new Date()));
        ticket.getTicketProducts().add(ticketProduct);
        ticketProductRepository.save(ticketProduct);
    }

    @Override
    @Transactional
    public void updateTicketProduct(UpdateTicketProductForm updateTicketProductForm) {
        TicketProduct ticketProduct = ticketProductRepository.findById(updateTicketProductForm.getId()).orElseThrow(
                () -> new NotFoundException("[TicketProductService] ❌TicketProduct not found.", ErrorCode.TICKET_PRODUCT_NOT_FOUND)
        );
        ticketProductMapper.updateTicketProductFromCreateTicketProductForm(ticketProduct, updateTicketProductForm);
        ticketProductRepository.save(ticketProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketProductDto getTicketProduct(Long ticketProductId) {
        return ticketProductRepository.findById(ticketProductId).map(ticketProductMapper::fromEntityToDto).orElseThrow(
                () -> new NotFoundException("[TicketProductService] ❌TicketProduct not found.", ErrorCode.TICKET_PRODUCT_NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public void deleteTicketProduct(Long ticketProductId) {
        val ticketProduct = ticketProductRepository.findById(ticketProductId).orElseThrow(
                () -> new NotFoundException("[TicketProductService] ❌TicketProduct not found.", ErrorCode.TICKET_PRODUCT_NOT_FOUND)
        );
        ticketProductRepository.delete(ticketProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseListDto<List<TicketProductDto>> getTicketProducts(TicketProductCriteria ticketProductCriteria, Pageable pageable) {
        Page<TicketProduct> ticketProducts = ticketProductRepository.findAll(ticketProductCriteria.getSpecification(), pageable);
        return new ResponseListDto<>(ticketProductMapper.fromEntitiesToDtos(ticketProducts.getContent()), ticketProducts.getTotalElements(), ticketProducts.getTotalPages());
    }

    @Override
    @Transactional
    public void uploadExcelFile(MultipartFile multipartFile, Long ticketId) {
        if (multipartFile.isEmpty()) {
            throw new NotFoundException("File is empty");
        }
        if (excelService.hasExcelFormat(multipartFile)) {
            try {
                val uploadItemProductForms = excelService.mapExcelToData(multipartFile.getInputStream());
                for (UploadItemProductForm uploadItemProductForm : uploadItemProductForms) {
                    if (ticketProductRepository.existsByName(uploadItemProductForm.getName())) {
                        throw new BadRequestException("Ticket product name is already exist");
                    }
                    TicketProduct ticketProduct = new TicketProduct();
                    ticketProduct.setName(uploadItemProductForm.getName());
//                    ticketProduct.setQuantity(uploadItemProductForm.getQuantity());
                    ticketProduct.setPrice(uploadItemProductForm.getPrice());
                    ticketProduct.setDescription(uploadItemProductForm.getDescription());
                    ticketProduct.setTicket(ticketService.findTicketById(ticketId));
                    ticketProduct.setItemCode(Utils.generateCode(TICKET_PRODUCT_PREFIX, new Date()));
                    ticketProduct.setMaxPurchasePerAccount(uploadItemProductForm.getMaxPurchasePerAccount());
                    ticketProductRepository.save(ticketProduct);
                }
                log.info("[TicketProductService] Uploaded the file successfully: " + multipartFile.getOriginalFilename());
            } catch (Exception e) {
                log.error(e.toString());
                log.info("[TicketProductService] Could not upload file " + multipartFile.getOriginalFilename() + "!");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TicketProduct findById(Long id) {
        return ticketProductRepository.findFirstById(id).orElseThrow(
                () -> new NotFoundException("[TicketProductService] ❌ TicketProduct not found.", ErrorCode.TICKET_PRODUCT_NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public void save(TicketProduct ticketProduct) {
        ticketProductRepository.save(ticketProduct);
    }

    @Override
    public void deactivateTicketProduct(long ticketProductId) {
        val ticketProduct = ticketProductRepository.findById(ticketProductId).orElseThrow(
                () -> new NotFoundException("[TicketProductService] ❌ TicketProduct not found.", ErrorCode.TICKET_PRODUCT_NOT_FOUND)
        );
        if(ticketProduct.getStatus() == BetaConstant.STATUS_ACTIVE)
        {
            ticketProduct.setStatus(BetaConstant.STATUS_DELETE);
            ticketProductRepository.save(ticketProduct);
        } else {
            throw new BadRequestException("[TicketProductService] ❌ TicketProduct is already inactive.", ErrorCode.TICKET_PRODUCT_NOT_FOUND);
        }
        ticketProductRepository.save(ticketProduct);
    }

    @Override
    public void activateTicketProduct(long ticketProductId) {
        val ticketProduct = ticketProductRepository.findById(ticketProductId).orElseThrow(
                () -> new NotFoundException("[TicketProductService] ❌ TicketProduct not found.", ErrorCode.TICKET_PRODUCT_NOT_FOUND)
        );
        if(ticketProduct.getStatus() == BetaConstant.STATUS_DELETE)
        {
            ticketProduct.setStatus(BetaConstant.STATUS_ACTIVE);
            ticketProductRepository.save(ticketProduct);
        } else {
            throw new BadRequestException("[TicketProductService] ❌ TicketProduct is already active.", ErrorCode.OBJECT_ALREADY_ACTIVATED);
        }
        ticketProductRepository.save(ticketProduct);
    }

    @Override
    public ResponseListDto<List<TicketProductDto>> getTicketProductsByTicketId(long ticketId, Pageable pageable) {
        Page<TicketProduct> ticketProducts = ticketProductRepository.findAllByTicketId(ticketId, pageable);
        return new ResponseListDto<>(ticketProductMapper.fromEntitiesToDtos(ticketProducts.getContent()), ticketProducts.getTotalElements(), ticketProducts.getTotalPages());
    }
}
