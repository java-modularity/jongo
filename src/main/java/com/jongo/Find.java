/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jongo;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import java.util.Iterator;

public class Find {

    private final DBCollection collection;
    private final Unmarshaller unmarshaller;
    private Query query;

    Find(Unmarshaller unmarshaller, DBCollection collection, String query) {
        this.unmarshaller = unmarshaller;
        this.collection = collection;
        this.query = Query.query(query);
    }

    Find(Unmarshaller unmarshaller, DBCollection collection, String query, Object... parameters) {
        this.unmarshaller = unmarshaller;
        this.collection = collection;
        this.query = Query.query(query, parameters);
    }

    public Find on(String fields) {
        this.query = new Query.Builder(query.getQuery()).fields(fields).build();
        return this;
    }

    public <T> Iterator<T> as(final Class<T> clazz) {
        ResultMapper<T> resultMapper = new ResultMapper<T>() {
            @Override
            public T map(String json) {
                return unmarshaller.unmarshall(json, clazz);
            }
        };
        return map(resultMapper);
    }

    public <T> Iterator<T> map(ResultMapper<T> resultMapper) {
        DBCursor cursor = collection.find(query.toDBObject());
        return new MongoIterator<T>(cursor, resultMapper);
    }

}
