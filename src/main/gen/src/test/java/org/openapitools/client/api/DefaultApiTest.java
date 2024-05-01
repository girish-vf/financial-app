/*
 * FinGenius
 * Financial Application (Backend)
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.Invoice;
import org.openapitools.client.model.Partner;
import org.openapitools.client.model.Product;
import org.openapitools.client.model.Transaction;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DefaultApi
 */
@Disabled
public class DefaultApiTest {

    private final DefaultApi api = new DefaultApi();

    /**
     * Route to get invoice(s)
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void invoicesGetTest() throws ApiException {
        String id = null;
        String type = null;
        String response = api.invoicesGet(id, type);
        // TODO: test validations
    }

    /**
     * Route to add an invoice
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void invoicesPostTest() throws ApiException {
        Invoice invoice = null;
        String response = api.invoicesPost(invoice);
        // TODO: test validations
    }

    /**
     * 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void jsonKotlinxSerializationGetTest() throws ApiException {
        String response = api.jsonKotlinxSerializationGet();
        // TODO: test validations
    }

    /**
     * Route to get ledger items
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void ledgerGetTest() throws ApiException {
        String partnerId = null;
        String startDate = null;
        String endDate = null;
        String response = api.ledgerGet(partnerId, startDate, endDate);
        // TODO: test validations
    }

    /**
     * Route to get partner(s)
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void partnersGetTest() throws ApiException {
        String type = null;
        String id = null;
        String status = null;
        Partner response = api.partnersGet(type, id, status);
        // TODO: test validations
    }

    /**
     * Route to edit a partner
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void partnersIdPutTest() throws ApiException {
        String id = null;
        Partner partner = null;
        String response = api.partnersIdPut(id, partner);
        // TODO: test validations
    }

    /**
     * Route to add a partner
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void partnersPostTest() throws ApiException {
        Partner partner = null;
        String response = api.partnersPost(partner);
        // TODO: test validations
    }

    /**
     * Route to get product(s)
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void productsGetTest() throws ApiException {
        String id = null;
        String status = null;
        Product response = api.productsGet(id, status);
        // TODO: test validations
    }

    /**
     * Route to edit a product
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void productsIdPutTest() throws ApiException {
        String id = null;
        Product product = null;
        String response = api.productsIdPut(id, product);
        // TODO: test validations
    }

    /**
     * Route to add a product
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void productsPostTest() throws ApiException {
        Product product = null;
        String response = api.productsPost(product);
        // TODO: test validations
    }

    /**
     * Route to get all transactions
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void transactionsGetTest() throws ApiException {
        String type = null;
        String response = api.transactionsGet(type);
        // TODO: test validations
    }

    /**
     * Route to add a transaction
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void transactionsPostTest() throws ApiException {
        Transaction transaction = null;
        String response = api.transactionsPost(transaction);
        // TODO: test validations
    }

}
