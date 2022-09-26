package com.ebay.signaturevalidation;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {Application.class, RestTemplate.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApplicationTestsIT {

	@Autowired
	private RestTemplate restTemplateWithSignature;

	@LocalServerPort
	private String port;

	private final Logger logger = LoggerFactory.getLogger(ApplicationTestsIT.class.getName());


	private String getLocalhostUrl() {
		return "https://209.140.140.112/sell/fulfillment/v1/order/14-00032-43825/issue_refund";
	}


	@Test
	void testSigning() throws Exception {
		String token = "<token comes here>";
		String body = "{\n" +
				"    \"orderLevelRefundAmount\": {\n" +
				"        \"currency\": \"USD\",\n" +
				"        \"value\": 10.39\n" +
				"    },\n" +
				"    \"reasonForRefund\": \"ITEM_NOT_AS_DESCRIBED\",\n" +
				"    \"comment\": \"public API test_order_partial_refund\"\n" +
				"}";

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Host", "api.sandbox.ebay.com");
		headers.add("Authorization", "Bearer " + token);


		HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplateWithSignature.exchange(getLocalhostUrl(), HttpMethod.POST, requestEntity, String.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		logger.info("Status: {}, Body: {}", response.getStatusCode(), response.getBody());
//		assertEquals(response.getBody(), "OK");
	}
}
