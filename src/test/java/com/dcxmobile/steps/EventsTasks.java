package com.dcxmobile.steps;

import com.dcxmobile.drivers.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assume;

public class EventsTasks {
    private AppiumDriver driver = DriverFactory.getDriver();

    @Given("I am logged in to the Go Mobile Practice App")
    public void i_am_logged_in_to_the_go_mobile_practice_app() {
        // If no driver is available, skip the step
        Assume.assumeFalse("Skipping because Appium/Kobiton driver isn't available", driver == null);
        // TODO: implement real login verification or reuse LoginSteps actions
    }

    @Given("I tap the plus FAB")
    public void i_tap_the_plus_fab() {
        Assume.assumeFalse("Skipping because Appium/Kobiton driver isn't available", driver == null);
        // TODO: implement locator tap for FAB
    }

    @Given("I select \"Event\" or \"Task\"")
    public void i_select_event_or_task() {
        Assume.assumeFalse("Skipping because Appium/Kobiton driver isn't available", driver == null);
        // TODO: implement selection logic
    }

    @When("I am on the creation form")
    public void i_am_on_the_creation_form() {
        Assume.assumeFalse("Skipping because Appium/Kobiton driver isn't available", driver == null);
        // TODO: validate creation form is displayed
    }

    @Then("the \"Subject\" field should be marked as mandatory")
    public void the_subject_field_should_be_marked_as_mandatory() {
        Assume.assumeFalse("Skipping because Appium/Kobiton driver isn't available", driver == null);
        // TODO: verify UI indicates subject field is required
    }

    @Then("I should not be able to save the Event or Task until all mandatory fields are completed")
    public void i_should_not_be_able_to_save_the_event_or_task_until_all_mandatory_fields_are_completed() {
        Assume.assumeFalse("Skipping because Appium/Kobiton driver isn't available", driver == null);
        // TODO: attempt to save and confirm validation prevents save
    }
}

