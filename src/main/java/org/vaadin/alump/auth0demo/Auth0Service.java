package org.vaadin.alump.auth0demo;


import com.auth0.Tokens;
import com.auth0.json.auth.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.*;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.SpringVaadinServletService;
import org.apache.http.HttpResponse;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by alump on 05/07/2017.
 */
public class Auth0Service extends SpringVaadinServletService {

    public Auth0Service(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration, ApplicationContext context)
            throws ServiceException {
        super(servlet, deploymentConfiguration, context);
    }

    private static void sessionInit(SessionInitEvent event) {
//            VaadinRequest request = event.getRequest();
        event.getSession().addRequestHandler(Auth0Service::handleRequest);

    }

    private static boolean handleRequest(VaadinSession vaadinSession, VaadinRequest request, VaadinResponse response) throws IOException {
        if (request.getPathInfo().equals(AUTH0_CALLBACK_HANDLE)) {
            if (request.getParameterMap().containsKey(STATE) && request.getParameterMap().containsKey(CODE)) {
                try {
                    Tokens tokens = AuthenticationControllerProvider.getInstance().handle((HttpServletRequest) request);
                    UserInfo userInfo = Auth0Util.resolveUser(tokens.getAccessToken());
                    Auth0Session session = (Auth0Session) vaadinSession;
                    if (session == null) {
                        System.err.println("session error");
                    } else {
                        session.setAuth0Info(tokens, userInfo);
                    }
                    ((HttpServletResponse) response).sendRedirect("main");
                    return true;
                } catch (Exception eprime) {
                    ((HttpServletResponse) response).sendRedirect("error");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected List<RequestHandler> createRequestHandlers()
            throws ServiceException {
        List<RequestHandler> list = super.createRequestHandlers();
        addSessionInitListener(Auth0Service::sessionInit);
        return list;
    }

    private static final String AUTH0_CALLBACK_HANDLE = "/callback";
    private static final String STATE = "state";
    private static final String CODE = "code";


    private void test(){

//        if (request.getPathInfo().equals(AUTH0_CALLBACK_HANDLE)){
//            if (request.getParameterMap().containsKey(STATE) && request.getParameterMap().containsKey(CODE)){
//                try {
//                    Tokens tokens = AuthenticationControllerProvider.getInstance().handle(request);
//                    UserInfo userInfo = Auth0Util.resolveUser(tokens.getAccessToken());
//                    Auth0Session session = Auth0Session.getSessionFromRequest(request);
//                    if (session == null){
//                        System.err.println("session error");
//                    }
////                    Auth0Session session = (Auth0Session) this.createVaadinRequest(request).getSession();
//                    session.setAuth0Info(tokens, userInfo);
//                    UI.getCurrent().navigate("main");
//                    return;
//                } catch (Exception eprime) {
//                    System.err.println("IDk what to do here");
//                }
//            }
//        }
    }


    @Override
    protected VaadinSession createVaadinSession(VaadinRequest request) {
        return new Auth0Session(this);
    }
}
