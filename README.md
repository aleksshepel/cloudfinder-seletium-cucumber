*** Running tests with Maven ***
enter in commandline $ mvn clean test -DsuiteXmlFile=all-tests.xml

*** Running specific scenario ***
you need to add a tag in TestRunner class inside @CucumberOptions (e.g. tags = {"@SearchContact"})

*** Reporting ***
open \cloudfinder-seletium-cucumber\target\cucumber-reports\cucumber-pretty\index.html

