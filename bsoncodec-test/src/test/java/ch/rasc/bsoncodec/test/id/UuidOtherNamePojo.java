/**
 * Copyright 2015-2015 Ralph Schaer <ralphschaer@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.rasc.bsoncodec.test.id;

import java.util.UUID;

import ch.rasc.bsoncodec.annotation.BsonDocument;
import ch.rasc.bsoncodec.annotation.Id;

@BsonDocument
public class UuidOtherNamePojo {

	@Id
	private UUID primaryKey;

	private int data;

	public UUID getPrimaryKey() {
		return this.primaryKey;
	}

	public void setPrimaryKey(UUID primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getData() {
		return this.data;
	}

	public void setData(int data) {
		this.data = data;
	}

}