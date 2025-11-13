# Java_JPA_Hibernate-lab-06_Solved

<img width="495" height="480" alt="image" src="https://github.com/user-attachments/assets/a040df7c-47f6-490a-b58f-84f0b02daa7f" />

## 1. Exercise

Working with JPQL and Criteria API

**Goal**

Learn the facilities JPA provides for searching entities: JPQL and Criteria API.

**Subject**

You have domain objects represented by classes: Company, Department and Employee. Also you have service layer objects represented by classes:

o	EntityService – base class for all service classes. Contains logic for entities initialization (see the class constructor) and defines the methods:

o	getEntityManagerFactory() returns EntityManagerFactory instance that is created and initialized.

o	getEmployeesByDepartmentName(String)::List<Employee> - method for searching employees by name of department they belong to. This method is abstract and should be implemented in EntityService1 and EntityService2.

o	getDepartmentsInfo()::List<DepartmentInfo> - method for gathering information about all existing departments in form of DepartmentInfo (auxiliary JavaBean class). This method is abstract and should be implemented in EntityService1 and EntityService2.

o	EntityService1 – implementation of search logic for EntityService that should use JPQL for searching.

o	EntityService2 – implementation of search logic for EntityService that should use JPA Criteria API for searching.

You need to implement EntityService1 and EntityService2 classes.

**Description**

22.	Open module jpa-lab-06.

23.	Look on the classes in “edu.jpa.entity” package to be aware about domain objects and its structure.

24.	Open class EntityService and analyze its structure:

o	Class constructor with logic for creating EntityManagerFactory instance and initializing the entities

o	getEntityManagerFactory()::EntityManagerFactory method that should be used in child classes for getting EntityManagerFactory instance

o	abstract methods getEmployeesByDepartmentName(String) and getDepartmentsInfo() that you need to implement in child classes EntityService1 and EntityService2

**Using JPQL**

25.	Open class EntityService1

26.	Implement method getEmployeesByDepartmentName(String):

27.	Create the EntityManager: 

EntityManager em = getEntityManagerFactory().createEntityManager()

28.	Start the transaction: 

em.getTransaction().begin()

29.	Define the JPQL query: 

String queryText = "select e from Employee e where e.department.name = :name"

30.	Create TypedQuery object: 

TypedQuery<Employee> query = em.createQuery(queryText, Employee.class)

31.	Specify the value for parameter “name”: 

query.setParameter("name", name)

32.	Execute the query: 

List<Employee> result = query.getResultList()

33.	Finalize the transaction: 

em.getTransaction().rollback()

34.	Implement method getDepartmentsInfo():

35.	Create the EntityManager: 

EntityManager em = getEntityManagerFactory().createEntityManager()

36.	Start the transaction: 

em.getTransaction().begin()

37.	Define the JPQL query: 

String queryText = "select new edu.jpa.service.DepartmentInfo(e.department.name,count(e.department)) from Employee e group by e.department.name"

38.	Create TypedQuery object: 

TypedQuery<DepartmentInfo> query = em.createQuery(queryText, DepartmentInfo.class)

39.	Execute the query: 

List<DepartmentInfo> result = query.getResultList()

40.	Finalize the transaction: 

em.getTransaction().rollback()

**Using Criteria API**

41.	Open class EntityService2

42.	Implement method getEmployeesByDepartmentName(String):

Create the EntityManager: EntityManager em = getEntityManagerFactory().createEntityManager()

43.	Create CriteriaBuilder object: 

CriteriaBuilder cb = em.getCriteriaBuilder()

44.	Create CriteriaQuery object: 

CriteriaQuery<Employee> cq = cb.createQuery(Employee.class)

45.	Specify the FROM statement (entity you are looking for): 

Root e = cq.from(Employee.class)

46.	Specify WHERE condition for the query: 

cq.where(cb.equal(e.get("department").get("name"), cb.parameter(String.class, "name")))

47.	Start the transaction: 

em.getTransaction().begin()

48.	Create TypedQuery object: 

TypedQuery<Employee> query = em.createQuery(cq);

49.	Execute the query: 

List<Employee> result = query.getResultList()

50.	Finalize the transaction: 

em.getTransaction().rollback()

51.	Implement method getDepartmentsInfo():

52.	Create the EntityManager: 

EntityManager em = getEntityManagerFactory().createEntityManager()

53.	Create CriteriaBuilder object: 

CriteriaBuilder cb = em.getCriteriaBuilder()

54.	Build the query:

Create CriteriaQuery object: 

CriteriaQuery<DepartmentInfo> cq = cb.createQuery(DepartmentInfo.class)

Specify the FROM statement: 

Root e = cq.from(Employee.class)

Since the result we need differs from the entity we use in FROM statement the SELECT statement should be defined (with creation of DepartmentInfo basing on data selected from target entity): 

cq.select(cb.construct(DepartmentInfo.class,e.get("department").get("name"), cb.count(e.get("department"))))

Specify the GROUP BY statement: 

cq.groupBy(e.get("department"))

55.	Create TypedQuery object: 

TypedQuery<DepartmentInfo> query = em.createQuery(cq)

56.	Execute the query: 

List<DepartmentInfo> result = query.getResultList()

57.	Finalize the transaction: 

em.getTransaction().rollback()

**Running**

58.	Open class Launcher and analyze it.

59.	Run the class Launcher.

60.	Analyze the log written to STDOUT

## 2. Solution

Here are SQL checks you can run in MySQL after launching the app (it creates and populates the schema).

**Schema sanity**

```
SHOW TABLES;
DESCRIBE Company;
DESCRIBE Department;
DESCRIBE Employee;
```

**Row counts**

```
SELECT COUNT(*) AS companies FROM Company;
SELECT COUNT(*) AS departments FROM Department; 
SELECT COUNT(*) AS employees FROM Employee; 
```

→ expect 1
→ expect 2
→ expect 5

**All employees with department and company**

```
SELECT e.name AS employee, d.name AS department, c.name AS company FROM Employee e JOIN Department d ON e.department_id = d.id JOIN Company c ON d.company_id = c.id ORDER BY department, employee;
```
Expect Training Center: Ivan, Viktor; IT: Andrey, Maxim, Sergey; company: Luxoft

**Employees in IT (matches getEmployeesByDepartmentName)**

```
SELECT e.name FROM Employee e JOIN Department d ON e.department_id = d.id WHERE d.name = 'IT' ORDER BY e.name;
```

Expect: Andrey, Maxim, Sergey

**DepartmentInfo equivalent (name + employee count)88

```
SELECT d.name AS departmentName, COUNT(e.id) AS employeesCount FROM Department d LEFT JOIN Employee e ON e.department_id = d.id GROUP BY d.id, d.name ORDER BY d.name;
```

Expect: IT → 3, Training Center → 2

**Company check**

```
SELECT name FROM Company WHERE name = 'Luxoft';
```

→ expect Luxoft

## 3. How to Run the Application in VSCode

Open the Terminal Window in VSCode and execute this command:

```
mvn exec:java
```

<img width="1919" height="771" alt="image" src="https://github.com/user-attachments/assets/7581a172-9c2a-49d4-a90f-413a50e3e3d0" />

## 4. Application Output (MySQL database)

<img width="1919" height="1017" alt="image" src="https://github.com/user-attachments/assets/6d3e87c7-b7b0-454c-8617-a79e25a4bc5d" />




