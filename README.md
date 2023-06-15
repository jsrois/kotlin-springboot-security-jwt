# JWT-based authentication with spring boot and kotlin (POC)

| Method | path                 | Description                      | Secured (role) | Implemented |
|--------|----------------------|----------------------------------|----------------|-------------|
| POST   | `/api/auth/login`    | attempts to login and gets token | No             | ❌           |
| POST   | `/api/auth/register` | creates a new user               | Yes (admin)    | ❌           |
| GET    | `/api/products`      | returns all products             | No             | ❌           |
| POST   | `/api/products`      | creates a new product            | Yes (manager)  | ✅           |


### Resources

- https://github.com/spring-projects/spring-security-samples/blob/main/servlet/spring-boot/java/jwt/login/src/main/java/example/RestConfig.java
- 