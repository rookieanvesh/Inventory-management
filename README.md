# Inventory Management System 📦

A robust and scalable Spring Boot application for managing product inventory and orders with a focus on simplicity and reliability.

## Overview 🎯
- RESTful APIs for product and order management
- Built with Spring Boot and PostgreSQL
- Concurrent order handling
- Comprehensive error handling
- Modern architectural patterns

## Architecture 🏗️
![mermaid-flow-1x (1)](https://github.com/user-attachments/assets/7e3f736e-82e5-4b3b-9e5f-e4ae2e090e0c)

## Tech Stack 🛠️
- Java 21
- Spring Boot 3.4.1
- PostgreSQL (pgADMIN 8.13)
- Maven 3.9.9

## Quick Start 🚀


### Database Setup
```sql
CREATE DATABASE inventory_db;
```

## Installation Steps 🛠️
1. Clone the repository
```bash
git clone https://github.com/rookieanvesh/Inventory-management.git
```
2. Configure database in application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_management_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```
3. Run the application
```bash
mvn spring-boot:run
```

## API Documentation 📚

Explore the comprehensive API documentation here: [View Full Documentation](https://web.postman.co/workspace/61457640-0895-4dc7-85e2-6364deecb2ac/collection/35179385-756280f1-a253-47b9-a8cf-42cc17779687?origin=tab-menu)

The documentation includes:
- Detailed endpoint descriptions
- Request/Response examples
- Sample payloads
- Error scenarios
- Required headers and parameters

### Product APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | /api/products | Create product |
| PUT    | /api/products/{id} | Update product |
| DELETE | /api/products/{id} | Delete product |
| GET    | /api/products | List all products |
| GET    | /api/products?search=keyword | Search products |

### Order APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | /api/orders | Create order |
| GET    | /api/orders/{id} | Get order details |
| GET    | /api/orders | List all orders |


## Testing ✅
```bash
mvn test
```
