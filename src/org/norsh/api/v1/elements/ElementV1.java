package org.norsh.api.v1.elements;

import org.norsh.api.v1.ApiV1;
import org.norsh.model.dtos.elements.ElementCreateDto;
import org.norsh.model.dtos.elements.ElementGetDto;
import org.norsh.model.dtos.elements.ElementMetadataDto;
import org.norsh.model.dtos.elements.ElementNetworkDto;
import org.norsh.model.dtos.elements.ElementPolicyDto;
import org.norsh.model.types.ElementType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API for managing Elements.
 * <p>
 * This API provides endpoints for registering and updating Elements, ensuring proper validation and cryptographic security.
 * </p>
 *
 * <h2>Features:</h2>
 * <ul>
 * <li>Supports registration of Element Proxies and Coins.</li>
 * <li>Ensures cryptographic integrity through digital signatures.</li>
 * <li>Processes metadata updates for existing Elements.</li>
 * </ul>
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Danthur Lice
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
@RestController
@RequestMapping("/v1/elements")
public class ElementV1 extends ApiV1 {
	/**
	 * Registers a new Element Proxy.
	 *
	 * @param dto the Element Proxy data.
	 * @return a response indicating the successful creation request.
	 */
	@PostMapping("/proxy")
	public ResponseEntity<Object> createProxy(@RequestBody ElementCreateDto dto) {
		dto.setType(ElementType.PROXY);
		dto.validate();
		
		return processRequest(dto.getHash(), dto);

	}

	/**
	 * Registers a new Element Token.
	 *
	 * @param dto the Element Token data.
	 * @return a response indicating the successful creation request.
	 */
	@PostMapping("/token")
	public ResponseEntity<Object> createCoin(@RequestBody ElementCreateDto dto) {
		dto.setType(ElementType.TOKEN);
		dto.validate();
		
		return processRequest(dto.getHash(), dto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> get(@PathVariable("id") String id) {
		ElementGetDto dto = new ElementGetDto(id);
		dto.validate();
		
		return processRequest(dto.getId(), dto);
	}
	
	/**
	 * Updates the metadata of an existing Element.
	 * <p>
	 * This method allows modifying metadata fields such as logo, description, and policy. The request must contain a valid
	 * cryptographic signature to ensure data integrity.
	 * </p>
	 *
	 * <h2>Validation Rules:</h2>
	 * <ul>
	 * <li>The Element must already exist.</li>
	 * <li>A valid cryptographic signature is required.</li>
	 * <li>At least one metadata field (name, logo, about, site, or policy) must be provided.</li>
	 * </ul>
	 *
	 * @param dto the metadata update request containing the changes.
	 * @return a {@link ResponseEntity} with the request ID, confirming that the request was accepted for processing.
	 */
	@PutMapping("/metadata")
	public ResponseEntity<Object> setMetadata(@RequestBody ElementMetadataDto dto) {
		dto.validate();
		
		return processRequest(dto.getHash(), dto);
	}
	
	@PutMapping("/network")
	public ResponseEntity<Object> setNetwork(@RequestBody ElementNetworkDto dto) {
		dto.validate();
		
		return processRequest(dto.getHash(), dto);
	}
	
	@PutMapping("/policy")
	public ResponseEntity<Object> setPolicy(@RequestBody ElementPolicyDto dto) {
		dto.validate();
		
		return processRequest(dto.getHash(), dto);
	}
	
	@DeleteMapping("/network")
	public ResponseEntity<Object> deleteNetwork(@RequestBody ElementNetworkDto dto) {
		dto.validate();
		
		return processRequest(dto.getHash(), dto);
	}
}
