package autotest.utils.http;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class RestResponseErrorHandler implements ResponseErrorHandler {
    private final Logger logger = Logger.getLogger(RestResponseErrorHandler.class);
    private final ResponseErrorHandler defaultErrorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return defaultErrorHandler.hasError(clientHttpResponse);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        String body = IOUtils.toString(clientHttpResponse.getBody(), "UTF-8");
        throw new RuntimeException(body);
    }
}
