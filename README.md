# RentRoom - REST API

## Overview
RentRoom is a feature-rich **rental room management** REST API designed with **role-based access control (RBAC)**, **secure authentication**, and **advanced moderation tools**. It enables users to **list, search, and rent rooms**, while moderators and admins oversee platform operations.

## Tech Stack
- **Backend**: Spring Boot 3.4.2, Spring Security (JWT, Refresh Token), Spring Data JPA  
- **Database**: MySQL  
- **Search Engine**: Elasticsearch  
- **API Documentation**: Swagger, OpenAPI  
- **Security**: JWT Authentication, Role-based Access Control (RBAC)  
- **Other**: Email Notifications, Multi-language support, Pagination & Filtering  

## Build Tool
- **Java Version**: 17  
- **Build Tool**: Maven  
- **Spring Boot Version**: 3.4.2  

## Features
### **1. User Features**
- **Register & Login** (JWT-based authentication with Refresh Token)  
- **Manage Listings** (Create, Update, Delete rooms for rent)  
- **Rent Rooms** (View available rooms and make rental requests)  
- **Report Violations** (Flag inappropriate listings or users)  
(In Development)
### **2. Moderator Features**
- **Approve/Reject Listings** (Ensure only verified listings are live)  
- **Handle Reports** (Review user-reported violations)  
- **Issue Temporary Bans** (Suspend problematic users, except Admins)  

### **3. Admin Features**
- **Full Platform Control** (Manage users, listings, reports, and system settings)  
- **Payment System (In Development)** (Secure online payments for rentals)  

### **4. Additional Features**
- **RBAC with Fine-Grained Permissions** (Different access levels for Users, Moderators, and Admins)  
- **Advanced Search with Elasticsearch** (Fast and efficient room search with filters)  
- **Multi-language Support** (API responses and UI labels adapt to user preferences)  
- **Email Notifications** (Alerts for approvals, bans, and account updates)  
- **Pagination & Filtering** (Optimized data retrieval for large datasets)  

## API Documentation
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  

## Installation
### **1. Clone the Repository**
```bash
git clone https://github.com/your-username/RentRoom.git
cd RentRoom
