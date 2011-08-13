package com.motorrad.webapp.http;

import com.motorrad.util.Bag;
import com.motorrad.util.Log;
import com.motorrad.webapp.http.toolkit.Resource;
import com.motorrad.webapp.service.Services;

import org.apache.commons.lang.StringUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


public class Dispatcher extends HttpServlet{
    private ActionRegistry actionRegistry;
    private Services services;

    public Dispatcher(ActionRegistry actionRegistry, Services services) {
        this.actionRegistry = actionRegistry;
        this.services = services;
    }

    @Override
    protected void service(HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI().replaceAll("/", "");
        Bag<String, String> parameters = new RequestBagger().bagParameters(request);
        HttpSession session = request.getSession(true);
        try {
            dispatch(requestURI, new HttpContext(parameters, session.getId(), request), new FlyweightHttpResponse(response));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void dispatch(String actionName, HttpContext httpContext, FlyweightHttpResponse httpResponse) throws Exception {

        ActionID id;

        if (StringUtils.isEmpty(actionName)) {
            id = actionRegistry.getDefaultAction();
        } else {
            id = actionRegistry.get(actionName);
        }

        Action action = id.makeAction(services);
        Log.info(this, "Dispatching to " + action.getClass().getSimpleName());

        render(httpResponse, action.execute(httpContext));
    }

    private void render(FlyweightHttpResponse httpResponse, Resource resource) throws IOException {
        httpResponse.setContentType(resource.getContentType());
        httpResponse.setStatusCode(resource.getHttpStatus());

        Map<String, String> headers = resource.extraHeaders();

        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpResponse.setHeader(header.getKey(), header.getValue());
        }

        OutputStream out = httpResponse.getOutputStream();
        resource.renderWithoutClosing(out);

        out.flush();
        out.close();
    }


}
