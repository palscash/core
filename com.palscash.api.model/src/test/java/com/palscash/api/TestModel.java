package com.palscash.api;

import com.palscash.api.model.ErrorResponse;
import com.palscash.api.model.GetAccountResponse;
import com.palscash.api.model.GetBalanceResponse;
import com.palscash.api.model.GetFeeResponse;
import com.palscash.api.model.GetNewAccountRequest;
import com.palscash.api.model.GetNewAccountResponse;
import com.palscash.api.model.MetaData;
import com.palscash.api.model.PingResponse;
import com.palscash.api.model.TransactionCountResponse;
import com.palscash.api.model.TransactionInfo;
import com.palscash.api.model.TransactionListResponse;
import com.palscash.api.model.TransferRequest;
import com.palscash.api.model.TransferResponse;

import junit.framework.TestCase;

public class TestModel extends TestCase {

	public void test() {
		
		new Urls();
		
		{
			new ErrorResponse();
			ErrorResponse o = new ErrorResponse("Error Test");
			assertEquals("Error Test", o.getError());
			assertEquals("error-response", o.getType());
			o.setError("Error Test");
			assertEquals("Error Test", o.getError());
			
			assertNotNull(o.getResponseMetadata());
			assertNotNull(o.toString());
			System.out.println(o.hashCode());
			assertTrue(o.hashCode() != 0);
			assertTrue(o.equals(o));
		}
		
		new GetAccountResponse();
		new GetBalanceResponse();
		new GetFeeResponse();
		new GetNewAccountRequest();
		new GetNewAccountResponse();
		new MetaData();
		new PingResponse();
		new TransactionCountResponse();
		new TransactionInfo();
		new TransactionListResponse();
		new TransferRequest();
		new TransferResponse();
		
		
		
	}
	
}
