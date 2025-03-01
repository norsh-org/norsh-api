//package org.norsh.api.v1.crypto;
//
//import java.util.Base64;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import org.norsh.api.v1.ApiV1;
//import org.norsh.security.Cryptography;
//import org.norsh.util.Converter;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * API controller for managing cryptographic key generation.
// * <p>
// * This endpoint generates an ECDSA key pair and returns the keys in various formats (Base64, Hexadecimal, and PEM).
// * </p>
// *
// * <h2>Key Features:</h2>
// * <ul>
// * <li>Generates a new ECDSA key pair on each request.</li>
// * <li>Supports multiple formats: Base64, Hexadecimal, and PEM.</li>
// * <li>Returns structured responses for easy parsing.</li>
// * </ul>
// *
// * @since 1.0.0
// * @version 1.0.0
// * @author Danthur Lice
// * @see Cryptography
// * @see Converter
// * @see <a href="https://docs.norsh.org">Norsh Documentation</a>
// */
//@RestController
//@RequestMapping("/v1/crypto/keys")
//public class KeysApiV1 extends ApiV1 {
//
//    /**
//     * Generates an ECDSA key pair and returns the keys in multiple formats.
//     * <p>
//     * The response includes the keys in Base64, Hexadecimal, and PEM formats.
//     * </p>
//     *
//     * <h2>Example Response:</h2>
//     * <pre>
//     * {
//     *   "base64": {
//     *     "private": "MHcCAQEEICR2jL3Hh7qs...",
//     *     "public": "MFYwEAYHKoZIzj0CAQYF..."
//     *   },
//     *   "hex": {
//     *     "private": "302e0201...",
//     *     "public": "30563010..."
//     *   },
//     *   "pem": {
//     *     "private": "-----BEGIN PRIVATE KEY-----\nMHcCAQEEICR2jL3...",
//     *     "public": "-----BEGIN PUBLIC KEY-----\nMFYwEAYHKoZIz..."
//     *   }
//     * }
//     * </pre>
//     *
//     * @return a {@link ResponseEntity} containing the keys in multiple formats.
//     */
//    @RequestMapping(value = "/generate", method = RequestMethod.GET)
//    public ResponseEntity<Map<String, Map<String, String>>> generate() {
//        Cryptography cryptography = new Cryptography();
//        Map<String, Map<String, String>> result = new LinkedHashMap<>();
//
//        // Add Base64 representation of the keys
//        result.put("base64", Map.of(
//                "private", Base64.getEncoder().encodeToString(cryptography.getPrivateKeyBytes()),
//                "public", Base64.getEncoder().encodeToString(cryptography.getPublicKeyBytes())
//        ));
//
//        // Add Hexadecimal representation of the keys
//        result.put("hex", Map.of(
//                "private", Converter.bytesToHex(cryptography.getPrivateKeyBytes()),
//                "public", Converter.bytesToHex(cryptography.getPublicKeyBytes())
//        ));
//
//        // Add PEM format representation of the keys
//        result.put("pem", Map.of(
//                "private", cryptography.exportPrivateKeyToPEM(),
//                "public", cryptography.exportPublicKeyToPEM()
//        ));
//
//        return ResponseEntity.ok(result);
//    }
//}
