package org.norsh.api.v1.crypto;

import java.util.Map;

import org.norsh.api.v1.ApiV1;
import org.norsh.exceptions.InternalException;
import org.norsh.exceptions.NorshException;
import org.norsh.model.dtos.crypto.AddressApiV1GenerateDto;
import org.norsh.security.Cryptography;
import org.norsh.security.Hasher;
import org.norsh.util.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/v1/crypto/address")
public class AddressApiV1 extends ApiV1 {

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
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generate(@RequestBody AddressApiV1GenerateDto body) {
        try {
        	byte[] publicKeyBytes = Converter.base64OrHexToBytes(body == null ? null : body.getPublicKey());
        	
        	//try reconstructing publicKey
        	Cryptography.valueOf(null, publicKeyBytes);
        	
            String addressHex = Hasher.sha3Hex(publicKeyBytes);
            return ResponseEntity.ok(Map.of("address", addressHex));
        } catch (NorshException e) {
            throw e; 
        } catch (Throwable t) {
            throw t;
        }
    }
}
