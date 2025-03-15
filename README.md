# FlightMaster

FlightMaster is a Java project using Hibernate and H2 Database, developed with Maven.  
The goal is to manage a relational database for flight tracking using Object-Relational Mapping (ORM) techniques.  
This project is structured for educational purposes, demonstrating step-by-step how to configure Hibernate and interact with a relational database.

## Hibernate and Database Configuration

### 1. Persistence Configuration (`persistence.xml`)

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

### 2. Entity Manager Factory (`JPAUtil.java`)

**Location:** `src/main/java/com/flight/manager/config/JPAUtil.java`

The `JPAUtil` class provides a centralized way to create and manage `EntityManager` instances.

#### Why is `JPAUtil` needed?

- Hibernate requires an `EntityManagerFactory` to create connections to the database.
- Instead of initializing it manually in every class, `JPAUtil` provides a singleton instance.
- Ensures that all database operations use a properly managed entity manager.
- Includes a `close()` method to properly shut down Hibernate when the application stops.

## 3. Entity Definition and Relationships

**Location:** `src/main/java/com/flight/manager/model/entities/\*

### 3.1 Entity Overview

The main entities in the system are:

- **Person** (Base class, abstract)
- **Passenger** (Extends `Person`)
- **Pilot** (Extends `Person`)
- **CrewMember** (Extends `Person`)
- **Passport** (Represents the identity document of a `Person`)
- **Flight** (Represents an airline flight)
- **Airplane** (Represents an aircraft)

### 3.2 Relationships Between Entities

| Entity     | Relationship  | Entity     | Type                                                  |
| ---------- | ------------- | ---------- | ----------------------------------------------------- |
| `Person`   | `@OneToOne`   | `Passport` | One-to-One (each person has one passport)             |
| `Person`   | `@ManyToMany` | `Flight`   | Many-to-Many (each person has many flights)           |
| `Flight`   | `@ManyToMany` | `Person`   | Many-to-Many (each person has many flights)           |
| `Flight`   | `@ManyToOne`  | `Airplane` | Many-to-One (each flight is operated by one airplane) |
| `Airplane` | `@OneToMany`  | `Flight`   | One-to-Many (an airplane has multiple flights)        |

### 3.3 Inheritance in Hibernate

#### **Person as a Superclass**

All individuals in the system inherit from `Person`, which acts as a **superclass**. This entity is marked as:

```java
@Inheritance(strategy = InheritanceType.JOINED)
```

With this strategy:

- The `person` table stores general attributes (`id`, `firstName`, `lastName`, `email`).
- Each subclass (`Passenger`, `Pilot`, `CrewMember`) has its own table with a **foreign key linking to `person.id`**.

### 3.4 Test Suite for Entity Persistence and Validation

A JUnit 5 test suite has been added to verify data persistence and the correctness of entity modeling.

#### Purpose:

- Ensure that entities are correctly persisted in the database.
- Validate relationships between entities (`OneToOne`, `ManyToMany`, etc.).
- Confirm data integrity and proper cascading behavior.

The test suite initializes the database, performs validation checks, and ensures correct cleanup after execution, allowing multiple test runs without data duplication in case a file based strategy has been chosen for H2.
