package org.vaadin.alump.auth0demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.*;

import java.io.IOException;

@Route(MainView.VIEW_NAME)
@RouteAlias("")
public class MainView extends VerticalLayout implements AfterNavigationObserver {

    public final static String VIEW_NAME = "main";

    public MainView() {
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        init();
    }


    private void init() {
        setMargin(true);
        setSpacing(true);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setWidth("100%");

        Button login = new Button("Login");
        login.setVisible(!Auth0Session.getCurrent().isLoggedIn());
        login.addClickListener(e->login());
        Button logout = new Button("Logout");
        logout.addClickListener(e->logout());
        logout.setVisible(Auth0Session.getCurrent().isLoggedIn());
        Button tryAccess = new Button("Try access view",
                e -> UI.getCurrent().navigate(LimitedView.VIEW_NAME));
        buttons.add(login, logout, tryAccess);

        H1 hello = new H1();
        if(!Auth0Session.getCurrent().isLoggedIn()) {
            hello.getElement().setText("Please login");
        }

        add(buttons, hello);

        Auth0Session.getCurrent().getUser().ifPresent(u -> {
            hello.getElement().setText("Hey " + u.getGivenName().orElse(u.getSubject()) + "!");

            HorizontalLayout info = new HorizontalLayout();
            info.setSpacing(true);
            info.setWidth("100%");
            add(info);

            Component userinfo = createUserInfoGrid(u);
            info.add(userinfo);
            // TODO
            //userinfo.setWidth("100%");
            //info.setExpandRatio(userinfo, 1f);

//            u.getPicture().ifPresent(url -> {
////                Image image = new Image(null, new ExternalResource(url));
////                image.setWidth("200px");
////                image.setHeight("200px");
////                info.add(image);
////            });
        });

    }

    private Component createUserInfoGrid(Auth0User user) {
        FormLayout layout = new FormLayout();

        user.getKeys().stream().filter(key -> !key.equals("clientID")).forEach(key -> {
            try {
                String value = user.getValue(key);

                H3 row = new H3(value);
                //row.setCaption(key);
                layout.add(row);
            } catch(Exception e) {
                System.err.println("Failed to read property " + key);
                e.printStackTrace();
            }
        });

        return layout;
    }

    private void login() {
        Auth0Session.getCurrent().login();
    }

    private void logout() {
        Auth0Session.getCurrent().logout();
    }
}