package com.motorrad.webapp.http.actions;

import com.motorrad.webapp.ActionID;
import com.motorrad.webapp.http.Action;
import com.motorrad.webapp.http.HttpContext;
import com.motorrad.webapp.http.toolkit.Resource;
import com.motorrad.webapp.http.views.FourOFourPage;
import com.motorrad.webapp.service.Services;

public class NotFoundAction implements Action{
    public static final ActionID ID = new ActionID() {
        @Override
        public String getName() {
            return "404";
        }

        @Override
        public Action makeAction(Services services) {
            return new NotFoundAction();
        }
    };

    public NotFoundAction(){

    }
    @Override
    public Resource execute(HttpContext httpContext) {
        return new FourOFourPage();
    }

}
