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

package com.motorrad.entity;

import com.motorrad.persistence.Persistable;
import com.sun.istack.internal.NotNull;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "kickstart_snippits")
public class KickstartSnippit implements Persistable<KickstartSnippit>, Serializable {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String snippit;

    @NotNull
    private KickstartSnippitType type;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSnippit(String snippit) {
        this.snippit = snippit;
    }

    public String getSnippit() {
        return snippit;
    }

    public void setType(KickstartSnippitType type) {
        this.type = type;
    }

    @Enumerated
    public KickstartSnippitType getType() {
        return type;
    }

    @Override
    public void initialize(boolean descendHierarcy) {
        Hibernate.initialize(this);
    }

    @Override
    public int compareTo(KickstartSnippit snippit) {
        return this.snippit.toLowerCase().compareTo(snippit.name.toLowerCase());
    }
}
