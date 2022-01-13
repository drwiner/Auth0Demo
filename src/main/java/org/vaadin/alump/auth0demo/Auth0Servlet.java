package org.vaadin.alump.auth0demo;

import com.auth0.Tokens;
import com.auth0.json.auth.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.spring.SpringServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("vaadinServlet")
//@Component
public class Auth0Servlet extends SpringServlet {

    private final ApplicationContext context;


    public Auth0Servlet(ApplicationContext context) {
//        super();
        super(context, false);
        this.context = context;
    }

    @Override
    protected VaadinServletService createServletService(
            DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        // this is needed when using a custom service URL
        Auth0Service service = new Auth0Service(this, deploymentConfiguration, this.context);
        service.init();
        return service;
    }

    private static final String AUTH0_CALLBACK_HANDLE = "/callback";
    private static final String STATE = "state";
    private static final String CODE = "code";

//    @Override
//    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        /*
//         * If this is an Auth0 authorize request, then we should see a state and code
//         */
//        super.service(request, response);
//
//
//    }
}
