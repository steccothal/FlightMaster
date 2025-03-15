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

## First Test: Checking Database Connection

To verify that Hibernate is correctly set up, a test class was created.

**Location:** `src/main/java/com/flight/manager/main/TestConnection.java`

This test checks if the connection to H2 is successful. Running it should output:

```
Connection to H2 successful!
```
