package com.motorrad.webapp.http;

import com.motorrad.webapp.http.actions.HomeAction;
import com.motorrad.webapp.http.actions.NotFoundAction;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ActionRegistry {
    private static final ActionID[] IDS = new ActionID[]{
            HomeAction.ID,
            NotFoundAction.ID
    };
    private final Map<String, ActionID> map;
    private final ActionID notFound;
    private final ActionID defaultAction;

    public static ActionRegistry defaultRegistry() {
        return new ActionRegistry(Arrays.asList(IDS), NotFoundAction.ID, HomeAction.ID);
    }

    public ActionRegistry(Collection<ActionID> actionIDs, ActionID notFound, ActionID defaultAction) {
        this.notFound = notFound;
        this.defaultAction = defaultAction;
        map = new HashMap<String, ActionID>();
        for (ActionID actionID : actionIDs) {
            map.put(actionID.getName(), actionID);
        }
    }

    public ActionID getDefaultAction() {
        return defaultAction;
    }

    public ActionID get(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        } else {
            return notFound;
        }
    }

}
