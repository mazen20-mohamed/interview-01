(1)
I see you are reviewing the error handling requirements for missing parameters and empty results. How do you plan to implement this in Spring Boot? Will you handle it locally within the Controller using try-catch / ResponseEntity, or do you prefer using a centralized @ControllerAdvice? 

What are the pros and cons of your choice?



Answer:
I prefer using a centralized @ControllerAdvice for exception handling rather than handling exceptions inside every controller.

pros:
- Controller only handles HTTP requests.
- Service only handles business logic.
- Each class has single responsiblity (SRP)
- No duplicate

cons:
- it is hard when debugging

---
(2)
In your code, you throw a NoProductsFoundException when the product list is empty. In a production REST API, how would you globally handle this exception so that the frontend receives a clean, user-friendly JSON error response instead of a raw 500 stack trace?


Answer:

For a NoProductsFoundException, I would map it to a 404 Not Found response with a standardized error object containing fields like timestamp, status, message, and request path. I would reserve local try-catch blocks only for endpoints that require custom exception handling different from the global behavior. I would use @ControllerAdvice for exception handling.