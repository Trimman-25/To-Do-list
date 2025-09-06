package com.cityguide.frontend;

import com.cityguide.backend.GeminiService;
import com.cityguide.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final GeminiService geminiService;
    private final SecurityService securityService;

    private final TextField cityField;
    private final Button searchButton;
    private final VerticalLayout resultsLayout;

    @Autowired
    public MainView(GeminiService geminiService, SecurityService securityService) {
        this.geminiService = geminiService;
        this.securityService = securityService;

        H1 header = new H1("City Guide");

        var loggedInUser = securityService.getAuthenticatedUser();
        HorizontalLayout authLayout = new HorizontalLayout();
        if (loggedInUser != null) {
            Span welcomeMessage = new Span("Welcome, " + loggedInUser.getUsername());
            Button logoutButton = new Button("Logout", e -> securityService.logout());
            authLayout.add(welcomeMessage, logoutButton);
        } else {
            authLayout.add(new RouterLink("Login", LoginView.class));
        }

        cityField = new TextField("Enter a city name");
        searchButton = new Button("Search");
        resultsLayout = new VerticalLayout();
        resultsLayout.setWidth("80%");

        searchButton.addClickListener(e -> searchCity());

        add(authLayout, header, cityField, searchButton, resultsLayout);
        setAlignItems(Alignment.CENTER);
    }

    private void searchCity() {
        String city = cityField.getValue();
        if (city == null || city.trim().isEmpty()) {
            return;
        }

        resultsLayout.removeAll();

        List<String> categories = new ArrayList<>(List.of(
                "Famous Food",
                "Famous Tourist Spots",
                "Famous Restaurants",
                "Nightlife and Famous Pubs",
                "Shopping Mall Insights"
        ));

        if (securityService.getAuthenticatedUser() != null) {
            categories.add("Culture of the region");
        }

        for (String category : categories) {
            String info = geminiService.getCityInfo(city, category);
            resultsLayout.add(new Details(category, new Span(info)));
        }
    }
}
