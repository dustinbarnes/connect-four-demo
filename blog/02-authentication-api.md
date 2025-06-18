# 02: Authentication API

We're going to start with Authentication. To keep the scope minimal for now, we're going to allow these flows: 

1. Users can register with a username and password
    1. Validate that the username is available
2. The frontend will send a username/password object (LoginRequest) to the backend. 
    1. The backend will grant a JWT token if authentication is successful
    2. The frontend will present that JWT for subsequent requests to the backend

We're going to start API-first and model this as an OpenAPI spec. You'll find that work at [../openapi/auth.yaml](../openapi/auth.yaml). 

Straightforward username/password registration, and standard HTTP response codes for standard login scenarios.
