# FlightMaster

FlightMaster is a Java project using Hibernate and H2 Database, developed with Maven.  
The goal is to manage a relational database for flight tracking using Object-Relational Mapping (ORM) techniques.  
This project is structured for educational purposes, demonstrating step-by-step how to configure Hibernate and interact with a relational database.

## Versioning and Tags

To facilitate tracking of development progress and allow step-by-step analysis, each major section of the project corresponds to a **Git tag** in the repository. These tags allow easy access to different stages of development:

- **`01-Persistence-setup`** → Basic Hibernate and database configuration.
- **`02-Entities-creation`** → Definition of entities and relationships.
- **`03-Repositories`** → Implementation of repositories and database queries.

By checking out a specific tag, it is possible to obtain the exact version of the project at that stage, making it easier to understand the incremental development process.

## 1. Hibernate and Database Configuration

Git tag: **`01-Persistence-setup`**

### 1.1 Persistence Configuration (`persistence.xml`)

**Location:** `src/main/resources/META-INF/persistence.xml`

The `persistence.xml` file defines the database connection settings and the Hibernate configuration for the project.

#### Why is it inside `META-INF/`?

In a JPA-based application, `persistence.xml` must be placed inside the `META-INF/` directory because that is where the JPA provider (Hibernate) looks for it when scanning the classpath.

#### Database Connection Mode

The project is configured to use **H2 in file-based mode**, meaning the database persists between application restarts. This is set by the following property in the `persistence.xml` file:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:h2:file:./data/flightdb"/>
```

If instead, an **in-memory** database is required (which does not persist data after the application stops), the following configuration should be used:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:h2:mem:flightdb;DB_CLOSE_DELAY=-1"/>
```

#### Purpose:

- Declares a persistence unit (`flightPU`), which defines how Hibernate interacts with the database.
- Configures H2 as the database, using file-based storage to persist data across application restarts.
- Enables automatic schema updates (`hbm2ddl.auto = update`), allowing Hibernate to create and modify tables.
- Enables SQL logging (`show_sql = true`), displaying queries executed by Hibernate in the console.

### 1.2 Entity Manager Factory (`JPAUtil.java`)

**Location:** `src/main/java/com/flight/manager/config/JPAUtil.java`

The `JPAUtil` class provides a centralized way to create and manage `EntityManager` instances.

#### Why is `JPAUtil` needed?

- Hibernate requires an `EntityManagerFactory` to create connections to the database.
- Instead of initializing it manually in every class, `JPAUtil` provides a singleton instance.
- Ensures that all database operations use a properly managed entity manager.
- Includes a `close()` method to properly shut down Hibernate when the application stops.

## 2. Entity Definition and Relationships

Git tag: **`02-Entities-creation`**

### 2.1 Entity Overview

**Location:** `src/main/java/com/flight/manager/model/entities/\*`

The main entities in the system are:

- **Person** (Base class, abstract)
- **Passenger** (Extends `Person`)
- **Pilot** (Extends `Person`)
- **CrewMember** (Extends `Person`)
- **Passport** (Represents the identity document of a `Person`)
- **Flight** (Represents an airline flight)
- **Airplane** (Represents an aircraft)

### 2.2 Relationships Between Entities

| Entity     | Relationship  | Entity     | Type                                                  |
| ---------- | ------------- | ---------- | ----------------------------------------------------- |
| `Person`   | `@OneToOne`   | `Passport` | One-to-One (each person has one passport)             |
| `Person`   | `@ManyToMany` | `Flight`   | Many-to-Many (each person has many flights)           |
| `Flight`   | `@ManyToMany` | `Person`   | Many-to-Many (each person has many flights)           |
| `Flight`   | `@ManyToOne`  | `Airplane` | Many-to-One (each flight is operated by one airplane) |
| `Airplane` | `@OneToMany`  | `Flight`   | One-to-Many (an airplane has multiple flights)        |

### 2.3 Inheritance in Hibernate

#### **Person as a Superclass**

All individuals in the system inherit from `Person`, which acts as a **superclass**. This entity is marked as:

```java
@Inheritance(strategy = InheritanceType.JOINED)
```

With this strategy:

- The `person` table stores general attributes (`id`, `firstName`, `lastName`, `email`).
- Each subclass (`Passenger`, `Pilot`, `CrewMember`) has its own table with a **foreign key linking to `person.id`**.

### 2.4 Test Suite for Entity Persistence and Validation

A JUnit 5 test suite has been added to verify data persistence and the correctness of entity modeling.

#### Purpose:

- Ensure that entities are correctly persisted in the database.
- Validate relationships between entities (`OneToOne`, `ManyToMany`, etc.).
- Confirm data integrity and proper cascading behavior.

The test suite initializes the database, performs validation checks, and ensures correct cleanup after execution, allowing multiple test runs without data duplication in case a file based strategy has been chosen for H2.

## 3. Repositories

Git tag: **`03-Repositories`**

### 3.1 Implementation of Generic Repositories

**Location:** `src/main/java/com/flight/manager/model/repositories/\*`

A **generic repository class** has been created to **standardize CRUD operations** for all entities. This class provides basic methods for:

- Creating new entities
- Reading entities by ID
- Updating existing entities
- Deleting entities
- Listing all entities of a given type

This abstraction allows for **code reuse** and ensures **consistency** across all repositories.

The database initializer has been modified. Instead of directly interacting with the `EntityManager`, it now utilizes **generic repositories** to populate the database.

### 3.2 Specific Repository Example: `FlightRepository`

A concrete implementation, `FlightRepository`, has been added to demonstrate **custom query operations** beyond the basic CRUD functionality. This repository includes examples of:

- **JPQL queries**
- **Named queries**
- **Native SQL queries**
- **Criteria API queries**

### 3.3 Introduction of a Dedicated Persistence Unit for Tests

A separate persistence unit has been added in `persistence.xml` to ensure test isolation from the main database. This unit uses an **in-memory H2 database**, which is created and destroyed automatically during each test suite execution. The main persistence unit remains file-based for real-world application use.

### 3.4 Handling Multiple Persistence Units in `JPAUtil`

The `JPAUtil` class has been updated to **dynamically select** the appropriate persistence unit based on the execution context. It now provides the correct `EntityManager` instance depending on whether the system is running in **test mode** or normal mode.

### 3.5 Unit Testing for Repositories

Test suites have been implemented for both the **generic repository** and the **`FlightRepository`** to verify that the persistence layer operates correctly. These tests check the proper execution of CRUD operations and the different query types.

To ensure that the test persistence unit is correctly initialized and properly closed, a **`TestWatcher`** class has been implemented. This class is extended by all test classes and:

- **Initializes the test persistence unit before executing each test suite.**
- **Closes the `EntityManagerFactory` after each test suite has completed execution.**

This approach ensures that the **test database is always fresh** for each test suite and avoids potential issues with pre-existing data.

---

With these enhancements, the project now supports a **clean separation between test and production environments**, an **extensible repository system**, and **robust testing for database interactions**.
