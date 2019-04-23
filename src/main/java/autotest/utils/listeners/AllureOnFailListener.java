package autotest.utils.listeners;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.StepResult;
import lombok.extern.log4j.Log4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

@Log4j

public final class AllureOnFailListener implements ITestListener, StepLifecycleListener, TestLifecycleListener {

    @Override
    public void beforeStepStop(final StepResult result) {
        log.info("Finishing step: " + result.getName());
    }

    @Override
    public void onTestStart(final ITestResult result) {
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
    }

    @Override
    public void onTestFailure(final ITestResult result) {
        try {
           screenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(final ITestResult result) {
        try {
            screenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(final ITestResult result) {
        try {
            screenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    @Override
    public void onStart(final ITestContext context) {
    }

    @Override
    public void onFinish(final ITestContext context) {
    }

    @Attachment(type = "image/png")
    public byte[] screenshot() throws IOException {
        File screenshot = Screenshots.takeScreenShotAsFile();

        if (screenshot == null) {
            screenshot = new File(new File("src/main/resources/supportFiles/screen.png").getAbsolutePath());

        }

        return Files.toByteArray(screenshot);
    }

}
