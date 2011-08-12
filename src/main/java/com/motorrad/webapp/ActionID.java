package com.motorrad.webapp;

import com.motorrad.webapp.http.Action;
import com.motorrad.webapp.service.Services;

public interface ActionID {
    public String getName();

    public Action makeAction(Services services);
}
