Project Overview

src/: Contains the test class files.
reports/: Holds the generated test result reports.
README.md: Documentation for the project.

Requirements

Java 22 or higher
ChromeDriver installed and configured in the system's PATH
Maven installed for dependency management

Process of executing tests

Clone repository:

 ```
    bash
    git clone https://github.com/MufradMahmud19/Demo_sauce_test.git
    cd assignment4
 ```


To Execute All Tests Using Maven:

Run the following command to execute all test cases one after another:

```
    bash
    mvn clean test
```

To Execute a Specific Test:

If you want to run a particular test class, specify it using:

 ```
    bash
    mvn -Dtest=SauceDemoTest#test1 clean test
 ```

Test reports will be saved in the reports/ folder.
