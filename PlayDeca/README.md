# Jakarta 10 - Faces 4.0

## Description

A simple Jakarta 10 MVC project using Jakarta Faces 4.0, Primefaces 12 and Jakarta Persistence 3.1.

---

## Prerequisites

Before getting started with the project, make sure you have the following prerequisites installed on your system:

### 1. JDK 17

- Choose one of the following options to download and install JDK 17:

  - [Microsoft OpenJDK](https://learn.microsoft.com/en-us/java/openjdk/download)
  - [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
  - [Azul Zulu](https://www.azul.com/downloads/#zulu)

### 2. NetBeans 18

- Download NetBeans 18 from [here](https://netbeans.apache.org/).

### 3. Payara 6.2023.6

- Download Payara 6.2023.6 from [here](https://www.payara.fish/downloads/payara-platform-community-edition/).

### 4. MySQL 8 (Including JConnector)

- Download and install MySQL 8 from [here](https://dev.mysql.com/downloads/installer/).

---

## Installation

Follow the steps below to install and set up the required components:

### 2.1 JDK

1. Unzip the downloaded JDK to a desired location on your system.

### 2.2 MySQL 8

1. Install MySQL using the downloaded installer.

2. Download Connector-J. It should download by default to 'Program Files (x86)\MySQL\Connector J 8.0'.

3. Create the database and user to manage it. Execute the following SQL statements in your MySQL client:

   ```sql
   CREATE DATABASE jakarta;
   CREATE USER 'jakarta'@'localhost' IDENTIFIED BY 'Jakarta@1!';
   GRANT ALL PRIVILEGES ON jakarta.* TO 'jakarta'@'localhost';
   FLUSH PRIVILEGES;
   ```

### 2.3 Payara 6

1. Unzip the downloaded Payara package.

2. Navigate to `payara/glassfish/domains/domain1` folder and paste the MySQL Connector there. Also, paste it inside the "lib" folder. So, there should be one in `payara6/glassfish/domains/domain1/lib` and another in `payara6/glassfish/domains/domain1` directory.

3. Open a command prompt or terminal and navigate to `payara6/bin` directory.

4. Run the `asadmin.bat` (or `asadmin.sh` for Linux/Mac) file.

5. Once in the `asadmin` console, type the following command and press Enter to start the domain:

   ```shell
   start-domain
   ```

6. Open your web browser and go to `http://localhost:4848`. It will open the Payara admin console.

7. In the admin console, navigate to **Resources** -> **JDBC** -> **JDBC Connection Pools**.

8. Click on **New...** to create a new JDBC connection pool.

9. Fill in the following details:

   - **Pool Name**: MySQLPool
   - **Resource Type**: javax.sql.DataSource
   - **Database Driver Vendor**: MySQL

10. Click **Next** and scroll down to **Additional Properties**.

11. Add the following properties (or modify as needed):

   - **password**: Jakarta@1!
   - **databaseName**: jakarta
   - **serverName**: localhost
   - **user**: jakarta
   - **portNumber**: 3306
   - **UseSSL**: false
   - **allowPublicKeyRetrieval**: true

12. Click **Finish** to create the JDBC connection pool.

13. Ping the resource pool to ensure it's working properly.

14. Next, add a JDBC resource using the pool you just created:

   - **JDNI Name**: jdbc/MySQL
   - **Pool Name**: MySQLPool

15. Click **OK** to save the JDBC resource.

16. Finally, go back to the `asadmin` console and type the following command to stop the domain:

    ```shell
    stop-domain
    ```

### 2.4 NetBeans 18

1. Follow the installation instructions for NetBeans 18, using the downloaded JDK as its default.

2. Once NetBeans is installed, open it and go to the **Tools** menu.

3. Select **Servers** from the menu.

4. In the **Servers** window, click on **Add Server**.

5. Choose **Payara Server**, browse to the location where you unzipped the Payara server, and click **Next**.

6. Leave the domain location unchanged and click **Finish** to add the Payara server.

---

Once all the above steps are completed, you can open your project in NetBeans. Make sure there are no issues related to the Java Platform. You should now be able to run your project, and NetBeans will handle deploying the application to the Payara server.

The web application should be available at [http://localhost:8080/Jakarta10/index.xhtml](http://localhost:8080/Jakarta10/index.xhtml).
