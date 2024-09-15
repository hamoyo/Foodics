# Foodics
Foodics Test Assignment - Automation

Hello Foodics Team, I used IntelliJ in this project with JDK 19 and test-results are captured in screenshots beside this Readme file.

In this demo I’ll be using Java, Maven, Selenium WebDriver, TestNG and OkHttp as RestAssured throwing Unsupported-Media-Type error while Post &Put.

You will find the running properties variables in the resources folder - project.properties file. 

I’ve added all dependencies in pom.xml which will help you run the tests on your machine.

I’ve also added the core implementation for Users API in the main path.

You will find the test cases in test path in the project - under package SmokeTests. I have added assertions to verify that the expected outcomes match the actual results for each test case.

For running the tests I’m using IntelliJ as my IDE which helps me running and debugging easily from a Play-button.

For APIs I used dependencies like testng and okhttp3 - as mentioned in pom.xml
For Web App I innstalled chromedriver using homebrew cask in addition to selenium and testng. You can change the path for chromedriver from project.properties file in the resources folder.

I have more ideas which I didn't get the opportunity to implement, I'll keep them for business. For example, moving the test results in TFS/Jira immediately while runnning each test case automatically. Meaning You will have one place to see all manual/automated testcases with the corresponding execution results.

Thanks.
