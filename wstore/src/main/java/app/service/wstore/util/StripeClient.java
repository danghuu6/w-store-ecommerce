package app.service.wstore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;

@Component
public class StripeClient {

    @Autowired
    StripeClient() {
        Stripe.apiKey = "sk_test_51LXILOJvtSsB9DmY807yqCiAQ3EzpKLd62eSU2G9AmbYiSTnGv8MN7Eb368nRPmD3sw2SEkJdYZZZ7mImkgn3I5T00Br7ykapJ";
    }

    // Create Customer in Stripe
    public Customer createCustomer(String email) throws StripeException {
        Map<String, Object> customer = new HashMap<>();

        customer.put("email", email);

        return Customer.create(customer);
    }

    // Get Customer take Id
    private Customer getCustomer(String id) throws StripeException {
        return Customer.retrieve(id);
    }

    public void checkOutOrder(String customerId, String paymentMethod, float amount, int orderId)
            throws StripeException {

        Customer customer = getCustomer(customerId);

        Map<String, Object> metaData = new HashMap<>();
        metaData.put("order_id", orderId);
        metaData.put("customer", customer.getEmail());

        List<Object> paymentMethodTypes = new ArrayList<>();

        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) amount * 100);
        params.put("currency", "usd");
        params.put("payment_method_types", paymentMethodTypes);
        params.put("customer", customerId);
        params.put("metadata", metaData);
        params.put("payment_method", paymentMethod);
        params.put("confirm", true);

        PaymentIntent.create(params);

    }
}
