package be.witspirit.menuapigwlambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MenuApiBootstrap implements RequestStreamHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MenuApiBootstrap.class);

    private final RequestStreamHandler handler;

    public MenuApiBootstrap() {
        LOG.info("Initializing MenuApiHandler...");
        AnnotationConfigApplicationContext appCtx = new AnnotationConfigApplicationContext(MenuApiConfig.class);
        handler = appCtx.getBean("menuApiHandler", RequestStreamHandler.class);
        LOG.info("MenuApiHandler initialized.");
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        LOG.debug("Handle Request - BEGIN");

        handler.handleRequest(input, output, context);

        LOG.debug("Handle Request - END");
    }
}
