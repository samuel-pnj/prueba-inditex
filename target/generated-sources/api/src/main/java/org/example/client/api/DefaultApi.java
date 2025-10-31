package org.example.client.api;

import org.example.client.ApiClient;

import org.example.client.model.ProductDetail;
import java.util.Set;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-10-30T19:25:42.041856900+01:00[Europe/Madrid]")
public class DefaultApi {
    private ApiClient apiClient;

    public DefaultApi() {
        this(new ApiClient());
    }

    @Autowired
    public DefaultApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Gets a product detail
     * Returns the product detail for a given productId
     * <p><b>200</b> - OK
     * <p><b>404</b> - Product Not found
     * @param productId The productId parameter
     * @return ProductDetail
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getProductProductIdRequestCreation(String productId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling getProductProductId", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("productId", productId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<ProductDetail> localVarReturnType = new ParameterizedTypeReference<ProductDetail>() {};
        return apiClient.invokeAPI("/product/{productId}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Gets a product detail
     * Returns the product detail for a given productId
     * <p><b>200</b> - OK
     * <p><b>404</b> - Product Not found
     * @param productId The productId parameter
     * @return ProductDetail
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ProductDetail> getProductProductId(String productId) throws WebClientResponseException {
        ParameterizedTypeReference<ProductDetail> localVarReturnType = new ParameterizedTypeReference<ProductDetail>() {};
        return getProductProductIdRequestCreation(productId).bodyToMono(localVarReturnType);
    }

    /**
     * Gets a product detail
     * Returns the product detail for a given productId
     * <p><b>200</b> - OK
     * <p><b>404</b> - Product Not found
     * @param productId The productId parameter
     * @return ResponseEntity&lt;ProductDetail&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<ProductDetail>> getProductProductIdWithHttpInfo(String productId) throws WebClientResponseException {
        ParameterizedTypeReference<ProductDetail> localVarReturnType = new ParameterizedTypeReference<ProductDetail>() {};
        return getProductProductIdRequestCreation(productId).toEntity(localVarReturnType);
    }

    /**
     * Gets a product detail
     * Returns the product detail for a given productId
     * <p><b>200</b> - OK
     * <p><b>404</b> - Product Not found
     * @param productId The productId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getProductProductIdWithResponseSpec(String productId) throws WebClientResponseException {
        return getProductProductIdRequestCreation(productId);
    }
    /**
     * Gets the ids of the similar products
     * Returns the similar products to a given one ordered by similarity
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @return Set&lt;String&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getProductSimilaridsRequestCreation(String productId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'productId' is set
        if (productId == null) {
            throw new WebClientResponseException("Missing the required parameter 'productId' when calling getProductSimilarids", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("productId", productId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Set<String>> localVarReturnType = new ParameterizedTypeReference<Set<String>>() {};
        return apiClient.invokeAPI("/product/{productId}/similarids", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Gets the ids of the similar products
     * Returns the similar products to a given one ordered by similarity
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @return Set&lt;String&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Set<String>> getProductSimilarids(String productId) throws WebClientResponseException {
        ParameterizedTypeReference<Set<String>> localVarReturnType = new ParameterizedTypeReference<Set<String>>() {};
        return getProductSimilaridsRequestCreation(productId).bodyToMono(localVarReturnType);
    }

    /**
     * Gets the ids of the similar products
     * Returns the similar products to a given one ordered by similarity
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @return ResponseEntity&lt;Set&lt;String&gt;&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Set<String>>> getProductSimilaridsWithHttpInfo(String productId) throws WebClientResponseException {
        ParameterizedTypeReference<Set<String>> localVarReturnType = new ParameterizedTypeReference<Set<String>>() {};
        return getProductSimilaridsRequestCreation(productId).toEntity(localVarReturnType);
    }

    /**
     * Gets the ids of the similar products
     * Returns the similar products to a given one ordered by similarity
     * <p><b>200</b> - OK
     * @param productId The productId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getProductSimilaridsWithResponseSpec(String productId) throws WebClientResponseException {
        return getProductSimilaridsRequestCreation(productId);
    }
}
