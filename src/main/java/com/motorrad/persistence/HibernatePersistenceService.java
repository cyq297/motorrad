/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorrad.persistence;

import com.motorrad.entity.KickstartSnippit;
import com.motorrad.entity.Role;
import com.motorrad.entity.Server;
import com.motorrad.util.Log;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.util.List;

public class HibernatePersistenceService implements PersistenceService {
    private SessionFactory sessionFactory;

    public HibernatePersistenceService(HibernateConfiguration hibernateConfiguration) throws IOException {
        System.setProperty("derby.stream.error.method", "com.motorrad.persistence.Derby.log");
        Configuration configuration = new Configuration();
        configuration.setProperties(hibernateConfiguration.asProperties());
        configuration.addAnnotatedClass(KickstartSnippit.class);
        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(Server.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public final void close() throws IOException {
        sessionFactory.close();
    }

    @Override
    public <T extends Persistable> T hibernate(T tee) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(tee);
            session.getTransaction().commit();
            tee.initialize(true);
            return tee;
        } finally {
            session.close();
        }
    }

    @Override
    public <T extends Persistable> T get(Class<T> theClass, long id) {
        Session session = sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(theClass).add(Restrictions.idEq(id));
            T tee = (T) criteria.uniqueResult();
            tee.initialize(true);
            return tee;
        } finally {
            session.close();
        }
    }

    @Override
    public <T extends Persistable> List<T> list(DbQuery<T> dbQuery) {
        Session session = sessionFactory.openSession();
        try {
            List<T> list = dbQuery.makeCriteria(new RealCriteriaFactory(session)).list();
            for (T t : list) {
                t.initialize(true);
            }
            return list;
        } finally {
            session.close();
        }
    }

    @Override
    public <T extends Persistable> T update(T tee) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.update(tee);
            session.getTransaction().commit();
            tee.initialize(true);
            return tee;
        } finally {
            session.close();
        }
    }

    @Override
    public <T extends Persistable> boolean delete(Class<T> theClass, long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Object delete = session.load(theClass, id);
            session.delete(delete);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            Log.error(this, e);
            session.close();
            return false;
        } finally {
            session.close();
        }
    }

    private class RealCriteriaFactory implements CriteriaFactory {
        private Session session;

        public RealCriteriaFactory(Session session) {
            this.session = session;
        }

        @Override
        public Criteria makeCriteria(Class<? extends Persistable> theClass) {
            return session.createCriteria(theClass);
        }
    }
}