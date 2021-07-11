package in.jiraautomation.test;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class JIRAAutomationTestListeners implements ITestListener {

	Logger logger = Logger.getLogger(JIRAAutomationTestListeners.class);

	@Override
	public void onTestStart(ITestResult result) {

		logger.info("Starting the test case " + result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		logger.info("The " + result.getName() + " passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {

		logger.info("The " + result.getName() + " failed");

		if (!result.getThrowable().getMessage().isEmpty())
			logger.error(result.getThrowable().getMessage());

	}

	@Override
	public void onTestSkipped(ITestResult result) {

		logger.info("The test case " + result.getName() + " skipped");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public void onStart(ITestContext context) {

		logger.info("Test cases execution started");

	}

	@Override
	public void onFinish(ITestContext context) {

		logger.info("Test cases execution finished");

	}

}
