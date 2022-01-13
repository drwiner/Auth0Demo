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

    private void checkAuthentication(VaadinRequest request) {
        VaadinServletRequest servletRequest = (VaadinServletRequest) request;
        try {
            String url = AuthenticationControllerProvider.getInstance().buildAuthorizeUrl(servletRequest, Auth0Util.getCallback()).withScope("openid email profile").build();//  buildAuthorizeUrl(servletRequest, Auth0Util.getLoginURL()).build();
            System.out.println(url);
            UI.getCurrent().getPage().setLocation(url);

        }catch (Exception e) {

            try {
                String url = AuthenticationControllerProvider.getInstance().buildAuthorizeUrl(servletRequest, Auth0Util.getLoginURL()).build();//  buildAuthorizeUrl(servletRequest, Auth0Util.getLoginURL()).build();
                System.out.println("CATCH");
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
        checkAuthentication(VaadinService.getCurrentRequest());
    }

}
