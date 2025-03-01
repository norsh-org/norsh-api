package org.norsh.api.v1.transactions;

import org.norsh.api.v1.ApiV1;
import org.norsh.model.dtos.transactions.PaymentCreateDto;
import org.norsh.rest.RestMethod;
import org.norsh.rest.RestRequest;
import org.norsh.rest.RestResponse;
import org.norsh.rest.annotations.Mapping;

@Mapping("/v1/payments")
public class PaymentV1 extends ApiV1 {
	@Mapping(value="/generate", method = RestMethod.POST)
	public void generate(RestRequest request, RestResponse response) throws Exception {
		PaymentCreateDto dto = request.getBody(PaymentCreateDto.class);
		dto.validate();
		
		processRequest(request, response, dto.getHash(), dto);
	}
}
