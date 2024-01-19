package com.food.order.system.payment.application.service;

import org.springframework.stereotype.Service;
import com.food.order.system.payment.application.service.dto.PaymentRequest;
import com.food.order.system.payment.application.service.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistPayment(paymentRequest);
    }
    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
       paymentRequestHelper.persistCancelPayment(paymentRequest);
    }



}
