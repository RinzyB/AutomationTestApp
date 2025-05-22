package org.test.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src\\test\\java\\org\\test\\cucumber\\featurefiles", 
				glue = "org.test.cucumber.stepdefinitions", 
				plugin = {"pretty", "html:target\\cucumber-report.html", "json:target\\cucumber-report.json" })

public class CucumberTestRunner extends AbstractTestNGCucumberTests {

}