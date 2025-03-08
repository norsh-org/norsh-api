package org.norsh.api.v1.elements;

import org.norsh.api.v1.ApiV1;
import org.norsh.model.dtos.elements.ElementCreateDto;
import org.norsh.model.dtos.elements.ElementGetDto;
import org.norsh.model.dtos.elements.ElementMetadataDto;
import org.norsh.model.dtos.elements.ElementNetworkDto;
import org.norsh.model.dtos.elements.ElementPolicyDto;
import org.norsh.model.types.ElementType;
import org.norsh.rest.RestMethod;
import org.norsh.rest.RestRequest;
import org.norsh.rest.RestResponse;
import org.norsh.rest.annotations.Mapping;

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
@Mapping("/v1/elements")
public class ElementV1 extends ApiV1 {
	@Mapping(value="/{id}", method = RestMethod.GET)
	public void get(RestRequest restRequest, RestResponse restResponse) throws Exception {
		String id = restRequest.getParameters().get("id");
		
		ElementGetDto dto = new ElementGetDto(id);
		dto.validate();
		
		processRequest(restRequest, restResponse, dto.getId(), dto);
	}
	
	/**
	 * Registers a new Element Proxy.
	 *
	 * @param dto the Element Proxy data.
	 * @return a response indicating the successful creation request.
	 */
	
	@Mapping(value="/proxy", method = RestMethod.POST)
	public void createProxy(RestRequest restRequest, RestResponse restResponse) throws Exception {
		ElementCreateDto dto = restRequest.getBody(ElementCreateDto.class);
	
		dto.setType(ElementType.PROXY);
		dto.validate();
		
		processRequest(restRequest, restResponse, dto.getHash(), dto);
	}
	
	/**
	 * Registers a new Element Token.
	 *
	 * @param dto the Element Token data.
	 * @return a response indicating the successful creation request.
	 */
	@Mapping(value="/token", method = RestMethod.POST)
	public void createToken(RestRequest restRequest, RestResponse restResponse) throws Exception {
		ElementCreateDto dto = restRequest.getBody(ElementCreateDto.class);
	
		dto.setType(ElementType.TOKEN);
		dto.validate();
		
		processRequest(restRequest, restResponse, dto.getHash(), dto);
	}

	@Mapping(value="/metadata", method = RestMethod.PUT)
	public void setMetadata(RestRequest restRequest, RestResponse restResponse) throws Exception {
		ElementMetadataDto dto = restRequest.getBody(ElementMetadataDto.class);
		dto.validate();
		
		processRequest(restRequest, restResponse, dto.getHash(), dto);
	}
	
	@Mapping(value="/network", method = RestMethod.PUT)
	public void setNetwork(RestRequest restRequest, RestResponse restResponse) throws Exception {
		ElementNetworkDto dto = restRequest.getBody(ElementNetworkDto.class);
		dto.validate();
		
		processRequest(restRequest, restResponse, dto.getHash(), dto);
	}
	
	@Mapping(value="/policy", method = RestMethod.PUT)
	public void setPolicy(RestRequest restRequest, RestResponse restResponse) throws Exception {
		ElementPolicyDto dto = restRequest.getBody(ElementPolicyDto.class);
		dto.validate();
		
		processRequest(restRequest, restResponse, dto.getHash(), dto);
	}
	
	@Mapping(value="/network", method = RestMethod.DELETE)
	public void deleteNetwork(RestRequest restRequest, RestResponse restResponse) throws Exception {
		ElementNetworkDto dto = restRequest.getBody(ElementNetworkDto.class);
		dto.validate();
		
		processRequest(restRequest, restResponse, dto.getHash(), dto);
	}
}
