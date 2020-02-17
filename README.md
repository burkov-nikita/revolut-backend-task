# Revolut Backend Test
This test is about implementing RESTful API for money transfers between accounts.

## Preface
The main idea of the application is to make it simple and extendable. UUID and locking on the database allow you to add more replicas and scale money transferring feature. Also, I'm really excited about the batching mode. It may decrease number of requests to datastore and make it more functional.

## Requirements
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You don't need to install any additional software to run the application. This application has gradle wrapper for building project purpose. In-memory datastore H2 is used.


### Installing

To build project you have to use gradlew command from root of project. This command uploads all dependencies and make project prepared for start.

```
gradlew clean build
```

## Running the tests

To run tests you have to use gradlew command. NOTE: this command runs all tests in the project: unit and functional.

```
gradlew clean test
```

### Test cases

Explain what these tests test and why

```
Give an example
```

### Start application

To start application you have to execute gradlew command from root of the project. 
Application will be started on port 8080. 
Container will be automatically placed with "/api". Example `http://localhost:8080/api`/

```
gradlew appStart
```

### API quick review

#### Account:Get
* **URL**

  /account/:id
  
* **Method:**

  `GET`
  
*  **Path Params** 

   **Required:**
 
   `id=[UUID or Account Number]`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{
                      "id": "0b6e1ecd-c998-3c74-aa3a-cf5ce9d6c1e3",
                      "currencyId": 643,
                      "num": "726020654000643",
                      "metadata": "Test",
                      "saldo": 0
                  }`                          
  * **Code:** 204 <br />
    **Content:** ``                              

* **Sample Call:**

    ```
    GET http://localhost:8080/api/account/207111811500643
    ```
    
* **Notes:**
You can use account number as UUID as well

###Account:Create
* **URL**

  /account/create
  
* **Method:**

  `POST`
  
*  **Path Params** 
  
    None
    
*  **Request body**
    
    JSON data with defined currencyId `{"currencyId": 643}`

* **Success Response:**

  * **Code:** 200 OK <br />
    **Content:** `{
                      "id": "0b6e1ecd-c998-3c74-aa3a-cf5ce9d6c1e3",
                      "currencyId": 643,
                      "num": "726020654000643",
                      "metadata": "Test",
                      "saldo": 0
                  }`                          
  * **Code:** 204 <br />
    **Content:** ``   
                               
* **Error Response:**

  * **Code:** 400 BAD REQUEST <br />
    **Content:** `Currency id is not specified.`
    
    OR
    
  * **Code:** 400 BAD REQUEST <br />
    **Content:** `You are not allowed to predefine account id before creation.`
    
* **Sample Call:**

    ```
    POST http://localhost:8080/api/account/create
    ```
    
* **Notes:**
If you don't define the currency id, you'll get an error message.

###Account:Delete
* **URL**

  /account/:id
  
* **Method:**

  `DELETE`
  
*  **Path Params** 
  
    `id=[UUID or Account Number]`

* **Success Response:**

  * **Code:** 204 NO CONTENT <br />
    **Content:** ``                                 
    
* **Sample Call:**

    ```
    DELETE http://localhost:8080/api/account/151959595500643
    ```
* **Notes:**
Delete operation is idempotent. The same result in any way.

###Account:Update
* **URL**

  /account/update
  
* **Method:**

  `POST`
  
*  **Path Params** 
  
    None

*  **Request body**
    
    JSON data with defined account UUID `{"id": "0b6e1ecd-c998-3c74-aa3a-cf5ce9d6c1e3", "metadata": "new"}`

* **Success Response:**

  * **Code:** 200 OK <br />
    **Content:** `{
                          "id": "0b6e1ecd-c998-3c74-aa3a-cf5ce9d6c1e3",
                          "currencyId": 643,
                          "num": "726020654000643",
                          "metadata": "new",
                          "saldo": 0
                      }`                                 
    
* **Sample Call:**

    ```
    POST http://localhost:8080/api/account/update
    ```
    
* **Notes:**
This operation can throw data storage exceptions. For example constraint exceptions.

###Account:Statement
* **URL**

  /account/:id/statement
  
* **Method:**

  `GET`
  
*  **Path Params** 
  
   `id=[UUID or Account Number]`

* **Success Response:**

  * **Code:** 200 OK <br />
    **Content:** `[{
                           "id": "5a1458b3-d344-440b-89bf-966fce31c99f",
                           "debitAccount": {
                               "id": "52f7ae88-6f7a-38b2-bbe7-0a6571f590cf",
                               "currencyId": 643,
                               "num": "9466670643",
                               "metadata": "Test"
                           },
                           "creditAccount": {
                               "id": "ea6e9879-4a78-356c-bb7f-5b555d34686b",
                               "currencyId": 643,
                               "num": "2391150643",
                               "metadata": "Test"
                           },
                           "amount": 100
                       }]`                                 
    
* **Sample Call:**

    ```
    GET http://localhost:8080/api/account/9466670643/statement
    ```
    
* **Notes:**
This operation can throw data storage exceptions. For example constraint exceptions.

###Money Transferring:To
* **URL**

  /transfer/to/:to?amount
  
* **Method:**

  `POST`
  
*  **Path Params** 
  
   `to=[UUID or Account Number]`
   
*  **Query Params**

    `amount=[Number]`

* **Success Response:**

  * **Code:** 204 NO CONTENT <br />
    **Content:** ``                                 
    
* **Sample Call:**

    ```
    POST http://localhost:8080/api/transfer/to/9466670643?amount=100
    ```
    
* **Notes:**
This operation does not return anything. This operation is just for the sake of account saldo changing. Account Entries are not created.

###Money Transferring:FromTo
* **URL**

  /transfer/from/:from/to/:to?amount
  
* **Method:**

  `POST`
  
*  **Path Params** 
  
   `from=[UUID or Account Number]; to=[UUID or Account Number]`

*  **Query Params**

    `amount=[Number]`

* **Success Response:**

  * **Code:** 200 OK <br />
    **Content:** `[{
                                             "id": "5a1458b3-d344-440b-89bf-966fce31c99f",
                                             "debitAccount": {
                                                 "id": "52f7ae88-6f7a-38b2-bbe7-0a6571f590cf",
                                                 "currencyId": 643,
                                                 "num": "9466670643",
                                                 "metadata": "Test"
                                             },
                                             "creditAccount": {
                                                 "id": "ea6e9879-4a78-356c-bb7f-5b555d34686b",
                                                 "currencyId": 643,
                                                 "num": "2391150643",
                                                 "metadata": "Test"
                                             },
                                             "amount": 100
                                         }]`                                             
    
* **Sample Call:**

    ```
    POST http://localhost:8080/api/transfer/from/9466670643to/9466670643?amount=100
    ```
    
* **Notes:**
The response may be like `[{"id": null, "debitAccount": null, "creditAccount": null, "amount": null}]`. It means that accounts have not existed. For more information you have to see application logs. 

###Money Transferring:In Batch mode
* **URL**

  /transfer/batch
  
* **Method:**

  `POST`
  
*  **Path Params** 
  
   None

*  **Query Params**

    None
    
*  **Request body**
    
    JSON data with defined account number `[{
                                             "creditAccountNumber": "9466670643",
                                             "debitAccountNumber": "2391150643",
                                             "amount": 100
                                           }]`
                                           
    OR `[{"creditAccountId": "52f7ae88-6f7a-38b2-bbe7-0a6571f590c",
          "debitAccountId": "ea6e9879-4a78-356c-bb7f-5b555d34686b",
          "amount": 100
           }]`
    
* **Success Response:**

  * **Code:** 204 NO CONTENT <br />
    **Content:** ``                                             
    
* **Sample Call:**

    ```
    POST http://localhost:8080/api/transfer/batch
    ```
    
* **Notes:**
No response because the operation may take plenty of time and generates a lot of entries. It depends on JSON size in the request. You can control operation with application logs.

## Built With

* Java & Lombok
* Gradle - Dependency Management
* Hibernate - ORM
* H2 - in-memory datastore
* EhCache - caching
* Guice - DI
* Jersey - RESTful Web Service
* JUnit & Mockito - Testing

## Authors

* **Burkov Nikita** - *Software Developer* - [GitHub](https://github.com/burkov-nikita/)