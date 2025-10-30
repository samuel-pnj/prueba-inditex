# DefaultApi

All URIs are relative to *http://localhost:3001*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getProductProductId**](DefaultApi.md#getProductProductId) | **GET** /product/{productId} | Gets a product detail |
| [**getProductSimilarids**](DefaultApi.md#getProductSimilarids) | **GET** /product/{productId}/similarids | Gets the ids of the similar products |



## getProductProductId

> ProductDetail getProductProductId(productId)

Gets a product detail

Returns the product detail for a given productId

### Example

```java
// Import classes:
import org.example.client.ApiClient;
import org.example.client.ApiException;
import org.example.client.Configuration;
import org.example.client.models.*;
import org.example.client.api.DefaultApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:3001");

        DefaultApi apiInstance = new DefaultApi(defaultClient);
        String productId = "productId_example"; // String | 
        try {
            ProductDetail result = apiInstance.getProductProductId(productId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#getProductProductId");
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

[**ProductDetail**](ProductDetail.md)

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


## getProductSimilarids

> Set&lt;String&gt; getProductSimilarids(productId)

Gets the ids of the similar products

Returns the similar products to a given one ordered by similarity

### Example

```java
// Import classes:
import org.example.client.ApiClient;
import org.example.client.ApiException;
import org.example.client.Configuration;
import org.example.client.models.*;
import org.example.client.api.DefaultApi;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("http://localhost:3001");

        DefaultApi apiInstance = new DefaultApi(defaultClient);
        String productId = "productId_example"; // String | 
        try {
            Set<String> result = apiInstance.getProductSimilarids(productId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#getProductSimilarids");
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

**Set&lt;String&gt;**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

