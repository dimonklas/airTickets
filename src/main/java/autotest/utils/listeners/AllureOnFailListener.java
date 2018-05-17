package autotest.utils.listeners;

import autotest.utils.DriverUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public final class AllureOnFailListener implements ITestListener {
    @Override
    public void onTestStart(final ITestResult result) {
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
    }

    @Override
    public void onTestFailure(final ITestResult result) {
        try {
           DriverUtils.screenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(final ITestResult result) {
        try {
            DriverUtils.screenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(final ITestResult result) {
        try {
            DriverUtils.screenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    @Override
    public void onStart(final ITestContext context) {
    }

    @Override
    public void onFinish(final ITestContext context) {
    }
}
