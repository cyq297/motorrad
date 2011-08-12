package com.motorrad.webapp.http.actions;

import com.motorrad.webapp.ActionID;
import com.motorrad.webapp.http.Action;
import com.motorrad.webapp.http.HttpContext;
import com.motorrad.webapp.http.toolkit.Resource;
import com.motorrad.webapp.http.views.HomePage;
import com.motorrad.webapp.service.Services;

public class HomeAction implements Action{
    public static final ActionID ID = new ActionID() {
        @Override
        public String getName() {
            return "home";
        }

        @Override
        public Action makeAction(Services services) {
            return new HomeAction();
        }
    };

    public HomeAction(){

    }


    @Override
    public Resource execute(HttpContext httpContext) {
        return new HomePage();
    }
}
