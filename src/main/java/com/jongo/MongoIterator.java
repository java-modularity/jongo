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

import com.mongodb.DBObject;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MongoIterator<E> implements Iterator<E> {

    private final Iterator<DBObject> cursor;
    private final ResultMapper<E> resultMapper;

    public MongoIterator(Iterator<DBObject> cursor, ResultMapper<E> resultMapper) {
        this.cursor = cursor;
        this.resultMapper = resultMapper;
    }

    public boolean hasNext() {
        return cursor.hasNext();
    }

    public E next() {
        if (!hasNext())
            throw new NoSuchElementException();

        DBObject dbObject = cursor.next();
        String json = Jongo.toJson(dbObject);
        return resultMapper.map(json);
    }

    public void remove() {
        throw new UnsupportedOperationException("remove() method is not supported");
    }
}
