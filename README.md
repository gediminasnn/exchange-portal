# Exchange Portal
![main](https://github.com/gediminasnn/exchange-portal/assets/70708109/7c3756c5-a355-474a-8b87-2acb8a0796e2)

This exchange rate portal application is built with Java (backend) and Angular (frontend). It provides a user-friendly web interface to display exchange rates from the Bank of Lithuania, view historical exchange rate data for specific currencies, and use a currency calculator for converting amounts between different currencies. Exchange rates are automatically updated daily using the Quartz job scheduling library and stored in an H2 database. This document outlines the steps required to set up the application on your local environment.

## Prerequisites

Before proceeding with the setup, ensure you have the following installed on your machine:

- **Java 17 or later:** You can verify the version by running `java -version` in your terminal. If not installed, download and install it from https://www.oracle.com/java/technologies/downloads/.
- **Maven 3.6 or later:** You can verify the version by running `mvn -v` in your terminal. If not installed, download and install it from https://maven.apache.org/download.cgi.
-   **Node.js and npm:** Angular requires Node.js. You can verify the version by running `node -v` and `npm -v` in your terminal. If not installed, download and install them from https://nodejs.org/en.

0.  **Clone the Repository**
    
    First, clone the repository to your local machine. Open a terminal and run the following command:
    
    `git clone git@github.com:gediminasnn/springboot.angular.exchange-portal.git` 
    
    (Optional) Replace `git@github.com:gediminasnn/springboot.angular.exchange-portal.git` with the desired URL of the repository.

1.  **Navigate to the Application Directory**
    
    Change directory to the application root:
    
    `cd exchange-portal` 
    
    (Optional) Replace `exchange-portal` with the path where you cloned the repository.

2.  **Navigate to the Backend Directory**

    Open a new terminal and run the following command:

    `cd backend`

    This command changes your working directory to the backend folder where the backend code is located.

3.  **Install Backend Dependencies**

    Run the following command in your terminal:

    `mvn clean install -DskipTests`

    This command uses Maven, a build automation tool, to download all the libraries specified in the project's pom.xml file and install them in the local Maven repository.


4.  **(Optional) Run Application Tests**

    Run the following command in your terminal:

    `mvn test`

    This command executes the test classes defined in the project and provides feedback on whether the application functions as expected.

5.  **Run the Backend Application**

    Run the following command in your terminal:

    `mvn spring-boot:run`

    This command instructs Maven to use the Spring Boot plugin to execute the application's main class and run the program.


6.  **Navigate to the Frontend Directory**

    Open a new terminal from project root directory and run the following command:

    `cd frontend`

    This command changes your working directory to the backend folder where the backend code is located.

7.  **Install Node.js Dependencies**
    
    Run the following command in your terminal:
    
    `npm install` 
    
    This command will ensure that all Node.js dependencies are installed according to the package.json.

8.  **Compile Front-End Assets**
    
    In the same terminal window or tab, execute the following command:
    
    `npm start` 
    
    This command compiles and publishes the assets, such as CSS and JavaScript files, making them available for use by the application.

 By completing this step, you will have fully set up your Exchange Portal application on your local development environment, ensuring it is ready for further development, testing.

## API Documentation

You can send HTTP requests to the following RESTful endpoints:

1.  Get currency with exchange rate history:

    `GET /api/currencies/173/exchange-rates?fromDate=2024-03-01&toDate=2024-03-02`
    ```
    HTTP/1.1 200 OK
    Content-Type: application/json
    ```
    ```yaml
    {
    "id": 173,
    "code": "USD",
    "name": "US dollar",
    "minorUnits": 2,
    "exchangeRates": [
        {
            "id": 211,
            "currency": 173,
            "rate": 1.0813,
            "date": "2024-03-02"
        },
        {
            "id": 212,
            "currency": 173,
            "rate": 1.0826,
            "date": "2024-03-01"
        }
    ]
    }
    ```
    
1.  Get latest exchange rates:

    `GET /api/exchange-rates`
    ```
    HTTP/1.1 200 OK
    Content-Type: application/json
    ```
    ```yaml
    [
    {
        "id": 1,
        "currency": {
            "id": 2,
            "code": "AED",
            "name": "UAE dirham",
            "minorUnits": 2,
            "exchangeRates": null
        },
        "rate": 3.93743,
        "date": "2024-05-19"
    },
    {
        "id": 2,
        "currency": {
            "id": 3,
            "code": "AFN",
            "name": "Afghani",
            "minorUnits": 2,
            "exchangeRates": null
        },
        "rate": 77.69146,
        "date": "2024-05-19"
    },
    ...
    ]
    ```

## Pages Documentation

### Exchange Rates Page
The Exchange Rates page displays the current exchange rates for various currencies.
![rates](https://github.com/gediminasnn/gediminasnn/assets/70708109/3dd3a17f-0577-41e9-9dd7-b92fc35b5f51)

### Currency's Exchange Rate History Page
The Currency's Exchange Rate History page displays historical exchange rate data for a specific currency.
![main](https://github.com/gediminasnn/gediminasnn/assets/70708109/12443f5c-112c-4aa3-9d88-daabca5d141d)

### Currency Calculator Page
The Currency Calculator page allows users to convert amounts between different currencies.
![calculator](https://github.com/gediminasnn/gediminasnn/assets/70708109/ef609964-fc75-4e72-8efc-767cd052f1fe)

## License

This project is licensed under the MIT License
