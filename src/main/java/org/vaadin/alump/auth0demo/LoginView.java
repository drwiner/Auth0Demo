package org.vaadin.alump.auth0demo;

import com.auth0.AuthenticationController;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by alump on 05/07/2017.
 */
@Route("login")
//@RouteAlias("callback")
public class LoginView extends VerticalLayout implements AfterNavigationObserver {

    private ProgressBar spinner;
    private H1 errorLabel;
    private H2 errorDescLabel;

    public LoginView() {
        VerticalLayout layout = new VerticalLayout();
        //layout.addClassName("wait-for-login");
        layout.setMargin(true);
        layout.setSpacing(true);

        spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        spinner.setWidth("200px");
        spinner.setHeight("200px");
        layout.add(spinner);
        //layout.setComponentAlignment(spinner, Alignment.MIDDLE_CENTER);

        errorLabel = new H1("Something went wrong :(");
        errorLabel.setWidth("100%");
        errorLabel.setVisible(false);

        errorDescLabel = new H2("n/a");
        errorDescLabel.setWidth("100%");
        errorDescLabel.setVisible(false);

        layout.add(errorLabel, errorDescLabel);
    }

    private void checkAuthentication(AfterNavigationEvent afterNavigationEvent, VaadinRequest request) throws IOException {
        VaadinServletRequest servletRequest = (VaadinServletRequest) request;

//        List<String> state = queryParameters.getParameters().get("state");
//        List<String> code = queryParameters.getParameters().get("code");

//        VaadinSavedRequestAwareAuthenticationSuccessHandler respHandler = new VaadinSavedRequestAwareAuthenticationSuccessHandler();
//        respHandler.onAuthenticationSuccess(

//        Tokens tokens = instance.handle(request, res);
//        servletRequest.setAttribute("state", VaadinSession.getCurrent().getState());
//        servletRequest.getParameterMap().put("state", (String) VaadinSession.getCurrent().getState());
//        Auth0Session current = Auth0Session.getCurrent();
        try {
            String url = AuthenticationControllerProvider.getInstance().buildAuthorizeUrl(servletRequest, Auth0Util.getCallback()).withScope("openid email profile").build();//  buildAuthorizeUrl(servletRequest, Auth0Util.getLoginURL()).build();
            System.out.println(url);
//                VaadinServletResponse.getCurrent().sendRedirect(url);
            UI.getCurrent().getPage().setLocation(url);
//            QueryParameters queryParameters = event.getLocation().getQueryParameters();
//
//            authenticationController.
//
//            servletRequest.getHttpServletRequest().setAttribute("state", queryParameters.getParameters().get("state").get(0));
//            servletRequest.getHttpServletRequest().setAttribute("code", queryParameters.getParameters().get("code"));
//            Tokens tokens = getAuthenticationController().handle(servletRequest);
////            Tokens tokens = current.getAuth0Tokens().get();
//            UserInfo userInfo = Auth0Util.resolveUser(tokens.getAccessToken());
//            Auth0Session.getCurrent().setAuth0Info(tokens, userInfo);
//            UI.getCurrent().navigate(MainView.VIEW_NAME);

        }catch (Exception e) {

            try {
                String url = AuthenticationControllerProvider.getInstance().buildAuthorizeUrl(servletRequest, Auth0Util.getLoginURL()).build();//  buildAuthorizeUrl(servletRequest, Auth0Util.getLoginURL()).build();
                System.out.println("CATCH");
//                VaadinServletResponse.getCurrent().sendRedirect(url);
                UI.getCurrent().getPage().setLocation(url);
            } catch (Exception eprime) {
                showError(eprime);
            }
        }
    }

    private void showError(Throwable t) {
        spinner.setVisible(false);
        errorLabel.setVisible(true);
        errorDescLabel.setVisible(true);
        errorDescLabel.getElement().setText(t.getMessage());
        t.printStackTrace();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        VaadinResponse currentResponse = VaadinService.getCurrentResponse();
        try {
            checkAuthentication(afterNavigationEvent, VaadinService.getCurrentRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
