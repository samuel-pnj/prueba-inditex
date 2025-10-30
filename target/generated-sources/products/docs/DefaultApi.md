# DefaultApi

All URIs are relative to *http://localhost:5000*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getProductSimilar**](DefaultApi.md#getProductSimilar) | **GET** /product/{productId}/similar | Similar products |



## getProductSimilar

> Set&lt;ProductDetail&gt; getProductSimilar(productId)

Similar products

### Example

```java
// Import classes:
import org.example.products.ApiClient;
import org.example.products.ApiException;
import org.example.products.Configuration;
import org.example.products.models.*;
import org.example.products.api.DefaultApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:5000");

        DefaultApi apiInstance = new DefaultApi(defaultClient);
        String productId = "productId_example"; // String | 
        try {
            Set<ProductDetail> result = apiInstance.getProductSimilar(productId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#getProductSimilar");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **productId** | **String**|  | |

### Return type

[**Set&lt;ProductDetail&gt;**](ProductDetail.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **404** | Product Not found |  -  |

