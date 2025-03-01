package org.norsh.api.v1.crypto;

import java.util.Map;

import org.norsh.exceptions.InternalException;
import org.norsh.exceptions.NorshException;
import org.norsh.model.dtos.crypto.AddressApiV1GenerateDto;
import org.norsh.rest.RestMethod;
import org.norsh.rest.RestRequest;
import org.norsh.rest.RestResponse;
import org.norsh.rest.annotations.Mapping;
import org.norsh.security.Cryptography;
import org.norsh.security.Hasher;
import org.norsh.util.Converter;

/**
 * API controller for handling cryptographic address generation.
 * <p>
 * This endpoint generates a unique cryptographic address based on the provided public key.
 * </p>
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Danthur Lice
 * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
 */
@Mapping("/v1/crypto/address")
public class AddressApiV1 {

    /**
     * Generates a cryptographic address from a provided public key.
     * <p>
     * The address is derived using a SHA3-256 hash of the public key in either Base64 or Hexadecimal format.
     * </p>
     *
     * @param body a {@link AddressApiV1GenerateDto} containing the public key.
     * @return a {@link ResponseEntity} containing the generated address or an error response.
     * @throws NorshException if the public key is missing or invalid.
     * @throws InternalException if an unexpected error occurs during processing.
     * @see <a href="https://docs.norsh.org/v1/crypto/address/generate">Generates an address from a public key.</a>
     */
	@Mapping(value="/generate", method = RestMethod.POST)
	public void generate(RestRequest request, RestResponse response) throws Exception {
		AddressApiV1GenerateDto body = request.getBody(AddressApiV1GenerateDto.class);
        try {
        	byte[] publicKeyBytes = Converter.base64OrHexToBytes(body == null ? null : body.getPublicKey());
        	
        	//try reconstructing publicKey
        	Cryptography.valueOf(null, publicKeyBytes);
        	
            String addressHex = Hasher.sha3Hex(publicKeyBytes);
            response.setBody(Map.of("address", addressHex));
        } catch (NorshException e) {
            throw e; 
        } catch (Throwable t) {
            throw t;
        }
    }
}
