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
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "server")
public class Server implements Persistable<Server>, Serializable {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String macAddress;

    @NotNull
    private String ipAddress;

    @NotNull
    @OneToMany
    @JoinTable(name = "servers_to_snippits",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "snippit_id"))
    private Set<KickstartSnippit> snippits = new TreeSet<KickstartSnippit>();

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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Set<KickstartSnippit> getSnippits() {
        return snippits;
    }

    public void setSnippits(Set<KickstartSnippit> snippits) {
        this.snippits = snippits;
    }

    @Override
    public void initialize(boolean descendHierarcy) {
        Hibernate.initialize(this);
        if (descendHierarcy) {
            for (KickstartSnippit snippit : snippits) {
                if (snippit != null) {
                    snippit.initialize(true);
                }
            }
        }
    }

    @Override
    public int compareTo(Server server) {
        return this.name.toLowerCase().compareTo(server.name.toLowerCase());
    }
}
