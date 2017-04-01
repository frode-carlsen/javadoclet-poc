
<a name="paths"></a>
## Paths

<a name="getuser"></a>
### Returns user details
```
GET /users/{userUUID}
```


#### Description
Returns user details.


#### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**userUUID**  <br>*required*|userUUID|string (uuid)|


#### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Successful retrieval of user entity|[User](#user)|
|**404**|User with given uuid does not exist|No Content|
|**500**|Internal server error|No Content|


#### Produces

* `application/xml`


#### Tags

* users



