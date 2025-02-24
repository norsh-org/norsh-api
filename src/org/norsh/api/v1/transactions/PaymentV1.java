package org.norsh.api.v1.transactions;

import org.norsh.api.v1.ApiV1;
import org.norsh.model.dtos.transactions.PaymentCreateDto;
import org.norsh.util.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
public class PaymentV1 extends ApiV1 {
	@PostMapping("/generate")
	public ResponseEntity<Object> generate(@RequestBody PaymentCreateDto dto) {
		dto.validate();
		
		System.out.println(Converter.toJsonPretty(dto));
		
		return processRequest(dto.getHash(), dto);

	}

}
