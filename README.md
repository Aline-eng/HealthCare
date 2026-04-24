# Healthcare Management System

## Project Overview
A comprehensive Java-based healthcare management system that enables clinics and medical facilities to manage patients, doctors, appointments, and medical records efficiently. The system provides a menu-driven interface for managing healthcare operations with real-time conflict detection and robust input validation.

## Features

### 1. **Patient Management**
- Register new patients with unique IDs and validation
- Store patient information securely
- Track medical records associated with each patient

### 2. **Doctor Management**
- Register doctors with specializations (General, Cardiology, etc.)
- Maintain doctor profiles and specialization information
- View all available doctors in the system

### 3. **Appointment Booking**
- Book appointments between patients and doctors
- Automatic conflict detection with 60-minute buffer between appointments
- Prevent past date appointments
- Real-time validation of appointment data

### 4. **Medical Records Management**
- Add medical records to patients using generic type handling
- Store records with timestamps for audit trails
- Retrieve all medical records for a specific patient

### 5. **Input Validation**
- Comprehensive input validation for all user inputs
- Regex-based validation for names, IDs, and specializations
- Clear, user-friendly error messages

## Technical Architecture

### Project Structure
```
HealthCare/
├── src/
│   ├── Main.java                          # Entry point with CLI interface
│   ├── model/
│   │   ├── Patient.java                   # Patient entity with generic medical records
│   │   ├── Doctor.java                    # Doctor entity with specialization
│   │   ├── Appointment.java               # Appointment entity with conflict validation
│   │   └── MedicalRecords.java            # Generic medical record class
│   ├── service/
│   │   └── ClinicService.java             # Business logic layer
│   ├── repository/
│   │   └── PatientRepository.java         # Data access layer for patients
│   ├── exception/
│   │   ├── PatientNotFoundException.java   # Custom exception for missing patients
│   │   └── AppointmentException.java      # Custom exception for appointment errors
│   └── util/
│       └── InputValidator.java            # Input validation utility
└── README.md                              # This file
```

### Design Patterns Used

1. **Repository Pattern** - PatientRepository manages data access
2. **Service Layer Pattern** - ClinicService handles business logic
3. **MVC Architecture** - Main.java as controller, model classes for data
4. **Exception Handling** - Custom exceptions for specific error scenarios
5. **Generics** - MedicalRecords<T> for type-safe medical record storage

## Core Classes

### Patient.java
- **Encapsulation**: Final immutable fields with private access
- **Collections**: ArrayList<MedicalRecords<?>> for storing medical records
- **Generics**: Uses wildcard generics for flexible record types

### Doctor.java
- **Immutability**: All fields are final
- **Encapsulation**: Private fields with public getters
- **Validation**: Input validation in constructor

### Appointment.java
- **Encapsulation**: Immutable appointment details
- **Validation**: Comprehensive null checks and validation
- **Relationships**: Maintains references to Patient and Doctor

### MedicalRecords.java
- **Generics**: Generic class MedicalRecords<T> for type-safe storage
- **Type Safety**: Ensures type consistency across operations
- **Timestamps**: Automatically records creation time

### ClinicService.java
- **Business Logic**: Centralized appointment and record management
- **Conflict Detection**: 60-minute buffer check for appointment scheduling
- **Exception Handling**: Throws custom exceptions for error scenarios
- **Collections**: Uses ArrayList and unmodifiable lists for data management
- **Generics**: Generic method `<T> void addMedicalRecord()` for flexible record types
- **Wildcards**: `List<? extends MedicalRecords<?>>` for wildcard usage

### PatientRepository.java
- **Data Access**: HashMap-based patient storage
- **Collections**: Uses Map<String, Patient> for efficient lookups
- **Validation**: Checks for null values and duplicate IDs

### InputValidator.java
- **Validation Logic**: Regex-based pattern matching
- **Reusability**: Static utility methods for validation
- **Error Messages**: Clear, descriptive error messages

## Usage Instructions

### Running the Application
1. Compile the project:
   ```bash
   javac -d bin src/**/*.java
   ```

2. Run the application:
   ```bash
   java -cp bin Main
   ```

### Menu Options
```
1. Register Patient       - Add a new patient to the system
2. Register Doctor        - Add a new doctor to the system
3. View Doctors          - Display all registered doctors
4. Book Appointment      - Schedule an appointment
5. View Appointments     - Display all scheduled appointments
6. Add Medical Record    - Add a medical record to a patient
7. View Medical Records  - Display a patient's medical records
8. Exit                  - Close the application
```

### Example Workflow
1. **Register a Doctor**: Select option 2, enter Doctor ID (e.g., "D3"), name (e.g., "Dr. John"), specialization (e.g., "Neurology")
2. **Register a Patient**: Select option 1, enter Patient ID (e.g., "P1"), name (e.g., "John Doe")
3. **Book Appointment**: Select option 4, enter patient and doctor IDs, and appointment date/time (format: yyyy-MM-dd HH)
4. **Add Medical Record**: Select option 6, enter patient ID and record description
5. **View Records**: Select option 7, enter patient ID to see all medical records

## Exception Handling

### Custom Exceptions
- **PatientNotFoundException**: Thrown when a patient is not found in the system
- **AppointmentException**: Thrown for appointment-related errors (conflict, invalid time, etc.)

### Error Scenarios Handled
- Invalid input format or empty fields
- Duplicate patient or doctor IDs
- Booking appointments for non-existent patients or doctors
- Booking appointments in the past
- Doctor availability conflicts (60-minute buffer)
- Null value validations

## Key Features Demonstrating OOP Concepts

### 1. Object-Oriented Design
- **Class Design**: Well-structured classes representing real-world entities
- **Encapsulation**: Private fields with controlled access through getters
- **Immutability**: Final fields for data integrity
- **Inheritance**: Custom exceptions extend RuntimeException

### 2. Relationship Modeling
- **Has-A Relationship**: Patient has medical records, Appointment has Patient and Doctor
- **Dependency**: ClinicService depends on PatientRepository
- **Association**: Clear relationships between entities

### 3. Separation of Concerns
- **Model Layer**: Business entities (Patient, Doctor, Appointment)
- **Service Layer**: Business logic (ClinicService)
- **Repository Layer**: Data access (PatientRepository)
- **Utility Layer**: Cross-cutting concerns (InputValidator)

### 4. Generics & Type Safety
- **Generic Class**: MedicalRecords<T> for storing any type of medical record
- **Generic Methods**: `<T> void addMedicalRecord()`
- **Wildcard Usage**: `List<? extends MedicalRecords<?>>` in getPatientRecords()
- **Type Safety**: Compile-time type checking with generics

### 5. Collections Usage
- **HashMap**: PatientRepository uses HashMap for O(1) lookup
- **ArrayList**: Dynamic lists for doctors and appointments
- **Unmodifiable Lists**: Collections.unmodifiableList() for data integrity
- **Iteration**: Enhanced for-loops for safe iteration

## Validation Patterns

All user inputs are validated using:
1. **Regex Patterns**:
   - Names: `^[a-zA-Z\s.'-]+$`
   - IDs: `^[a-zA-Z0-9_-]+$`
   - Specializations: `^[a-zA-Z\s&-]+$`

2. **Null Checks**: All critical fields validated for null values

3. **Business Logic Validation**: 
   - Duplicate ID prevention
   - Appointment conflict detection
   - Past date prevention

## System Constraints
- Appointment buffer: 60 minutes between appointments for the same doctor
- All IDs must be unique within their entity type
- Patient and doctor names must follow regex patterns
- Specializations support letters, spaces, ampersands, and hyphens

## Dependencies
- **Java**: Java 8 or higher
- **Standard Library**: java.time, java.util, java.util.regex

## Future Enhancements
- Database integration (SQL/NoSQL)
- REST API endpoints for remote access
- User authentication and authorization
- Appointment reminders and notifications
- Multi-clinic support
- Advanced reporting and analytics

## Notes
- The system uses in-memory storage (HashMap and ArrayList)
- Data is not persisted between application runs
- All times are in 24-hour format
- The system initializes with two sample doctors (D1: Dr. Alice, D2: Dr. Bob)

## License
This project is provided as-is for educational purposes.
