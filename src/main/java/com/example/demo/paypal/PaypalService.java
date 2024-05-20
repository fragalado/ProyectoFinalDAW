package com.example.demo.paypal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Servicio para realizar compras con Paypal
 * 
 * Fecha: 19/05/2024
 * 
 * @author Francisco Jose Gallego Dorado
 */
@Service
public class PaypalService {

	@Autowired
	private APIContext apiContext;

	public Payment createPayment(Double total, String currency, String description, String cancelUrl, String successUrl)
			throws PayPalRESTException {
		Amount amount = new Amount();
		amount.setCurrency(currency);
		amount.setTotal(total.toString());

		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);
		
		// Generar un ID único para la solicitud
        String requestId = UUID.randomUUID().toString();
        Map<String, String> map = new HashMap<>();
        map.put("PayPal-Request-Id", requestId);
        apiContext.setHTTPHeaders(map);

		return payment.create(apiContext);
	}

	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}
}
