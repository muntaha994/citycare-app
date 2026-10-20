# CityCare – Smart City Management

A desktop application for managing city services, built with **Java Swing** using object-oriented programming principles.

## Features
- 🔐 Login system with role-based access (User and Admin)
- 📢 **Complaints:** submit and track complaints
- 🚨 **Emergency:** emergency service requests
- 🏥 **Healthcare:** healthcare services and blood bank entries
- 💳 **Bills:** view and manage bills
- 🚌 **Transport:** transport services
- ♻️ **Waste:** waste management
- 🛡️ **Security:** security services
- 🛠️ **Admin panels:** a separate admin tab for each service, visible only to admins
- 👤 **Profile:** user profile with logout

## Tech Stack
- Java
- Java Swing (GUI)

## OOP Design
- **Model classes:** `User`, `Bill`, `Complaint`, `BloodBankEntry`
- **Service classes:** one per module (e.g. `ComplaintService`, `BillingService`) handling the logic
- **UI panels:** one panel per module, plus admin panels
- **Entry point:** `CityCareApp.java`

## How to Run
1. Clone the repo:
```bash
   git clone https://github.com/muntaha994/citycare-app.git
   cd citycare-app
```
2. Compile (requires JDK):
```bash
   javac -d out *.java
```
3. Run:
```bash
   java -cp out com.mycompany.citycare.CityCareApp
```
4. Log in on the login screen.

## Author
Muntaha ([@muntaha994](https://github.com/muntaha994))
