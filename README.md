🧪 Automation Framework
<div align="center"> <h3>🚀 Scalable UI & API Test Automation Framework</h3> <p> Built with focus on <b>performance</b>, <b>maintainability</b> and <b>CI/CD</b> </p> <br/> <img src="https://img.shields.io/badge/Java-11+-orange?style=flat-square&logo=java"/> <img src="https://img.shields.io/badge/Maven-Build-red?style=flat-square&logo=apachemaven"/> <img src="https://img.shields.io/badge/Selenium-UI-green?style=flat-square&logo=selenium"/> <img src="https://img.shields.io/badge/RestAssured-API-blue?style=flat-square"/> <img src="https://img.shields.io/badge/Allure-Reporting-purple?style=flat-square"/> <img src="https://img.shields.io/badge/CI/CD-Ready-black?style=flat-square&logo=githubactions"/>

<br/><br/>

<a href="#">View Demo</a>
·
<a href="#">Report Bug</a>
·
<a href="#">Request Feature</a>

</div>

📌 About The Project

A modern, extensible automation framework designed to support both UI and API testing in a clean and scalable way.

It allows teams to:

Run UI and API tests independently or together
Generate rich reports with Allure
Integrate easily into CI/CD pipelines
Maintain clean and reusable test architecture
✨ Key Features
🔹 UI & API testing in one framework
🔹 Maven profile-based execution
🔹 Allure reporting integration
🔹 Clean layered architecture
🔹 Easy scalability
🔹 CI/CD ready
⚙️ Tech Stack
Component	Technology
Language	Java
Build	Maven
UI	Selenium / Playwright
API	RestAssured
Reporting	Allure
Runner	TestNG / JUnit

🚀 Getting Started
Prerequisites
Java 11+
Maven
Allure CLI
java -version
mvn -version
allure --version
▶️ Running Tests
# UI tests
mvn test -Pui-tests
# API tests
mvn test -Papi-tests
# All tests
mvn test -Pall-tests

📊 Reporting
# Generate Allure report
allure generate target/allure-results -o target/allure-report --clean
# Open report
allure open target/allure-report

/docs/images/allure-report.png
🔄 CI/CD
mvn clean test -Pall-tests
<details> <summary>GitHub Actions example</summary>
name: Run Tests

on: [push]

jobs:
test:
runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3

      - uses: actions/setup-java@v3
        with:
          java-version: '11'

      - run: mvn clean test -Pall-tests
</details>
🧪 Best Practices
Page Object Model
Separation of concerns
Reusable utilities
Config-driven execution
Clean architecture
🔮 Future Improvements
Docker support
Parallel execution
Advanced dashboards
AI insights
👨‍💻 Author

Vadim Pasat
QA Engineer