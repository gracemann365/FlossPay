package com.openpay.api.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openpay.shared.dto.StatusResponse;
import com.openpay.shared.model.TransactionEntity;
import com.openpay.shared.repository.TransactionRepository;

/**
 * <h2>StatusController</h2>
 * <p>
 * REST controller for retrieving the current status of a UPI transaction.
 * Exposes the <code>/transaction/{id}/status</code> endpoint for client status
 * polling.
 * </p>
    *
    * <h3>Endpoint</h3>
 * <ul>
 * <li><b>GET /transaction/{id}/status</b> — Fetch the current status for a
 * given transaction ID</li>
 * </ul>
 *
 * <h3>Example Request</h3>
 * 
 * <pre>
 * GET / transaction / 42 / status
 * </pre>
 *
 * <h3>Example Response</h3>
 * 
 * <pre>
 * {
 *   "id": 42,
 *   "status": "SUCCESS"
 * }
 * </pre>
 *
 * <ul>
 * <li>Returns <code>404 Not Found</code> if the transaction does not
 * exist.</li>
 * <li>On success, returns status in a {@link StatusResponse} DTO.</li>
 * </ul>
 *
 * @author David Grace
 * @since 1.0
 * @see com.openpay.shared.model.TransactionEntity
 * @see com.openpay.shared.dto.StatusResponse
 */
@RestController
@RequestMapping("/transaction")
public class StatusController {

    private final TransactionRepository transactionRepository;

    /**
     * Constructs the controller with its required dependencies.
     * 
     * @param transactionRepository DAO for accessing transaction data
     */
    public StatusController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves the current status for the given transaction ID.
     *
     * @param id the transaction ID to look up
     * @return ResponseEntity with a StatusResponse DTO or 404 if not found
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<?> getStatus(@PathVariable("id") Long id) {
        Optional<TransactionEntity> transaction = transactionRepository.findById(id);

        if (transaction.isEmpty()) {
            // Return 404 if transaction is not present
            return ResponseEntity.notFound().build();
        }

        // Return the transaction's status in a standard response DTO
        return ResponseEntity.ok(
                new StatusResponse(id, transaction.get().getStatus()));
    }
}
