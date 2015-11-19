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
package ch.rasc.bsoncodec.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.ObjectIdGenerator;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import ch.rasc.bsoncodec.test.pojo.MonthPojo;
import ch.rasc.bsoncodec.test.pojo.MonthPojoCodec;
import ch.rasc.bsoncodec.time.MonthInt32Codec;

public class MonthPojoTest extends AbstractMongoDBTest {

	private final static String COLL_NAME = "months";

	static class MonthCodecProvider implements CodecProvider {
		@Override
		@SuppressWarnings("unchecked")
		public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
			if (clazz.equals(MonthPojo.class)) {
				return (Codec<T>) new MonthPojoCodec(registry, new ObjectIdGenerator());
			}
			return null;
		}
	}

	private MongoDatabase connect() {
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
				MongoClient.getDefaultCodecRegistry(),
				CodecRegistries.fromCodecs(new MonthInt32Codec()),
				CodecRegistries.fromProviders(new MonthCodecProvider()));

		MongoDatabase db = getMongoClient().getDatabase("pojo")
				.withCodecRegistry(codecRegistry);
		return db;
	}

	private static MonthPojo insert(MongoDatabase db) {
		MongoCollection<MonthPojo> coll = db.getCollection(COLL_NAME, MonthPojo.class);
		MonthPojo pojo = new MonthPojo();
		pojo.setScalar(Month.of(1));
		pojo.setArray(new Month[] { Month.of(2), Month.of(3), Month.of(4) });
		pojo.setArray2(new Month[][] { { Month.of(5) }, { Month.of(6), Month.of(7) } });
		pojo.setList(Arrays.asList(Month.of(8), Month.of(9)));

		Set<Month> set = new HashSet<>();
		set.add(Month.of(12));
		pojo.setSet(set);
		coll.insertOne(pojo);
		return pojo;
	}

	private static MonthPojo insertEmpty(MongoDatabase db) {
		MongoCollection<MonthPojo> coll = db.getCollection(COLL_NAME, MonthPojo.class);
		MonthPojo pojo = new MonthPojo();
		coll.insertOne(pojo);
		return pojo;
	}

	@Test
	public void testInsertAndFind() {
		MongoDatabase db = connect();
		MonthPojo pojo = insert(db);

		MongoCollection<MonthPojo> coll = db.getCollection(COLL_NAME, MonthPojo.class);
		MonthPojo read = coll.find().first();
		assertThat(read).isEqualToComparingFieldByField(pojo);

		MonthPojo empty = coll.find().projection(Projections.include("id")).first();
		assertThat(empty.getScalar()).isNull();
		assertThat(empty.getArray()).isNull();
		assertThat(empty.getArray2()).isNull();
		assertThat(empty.getList()).isNull();
		assertThat(empty.getSet()).isNull();
	}

	@Test
	public void testInsertAndFindEmpty() {
		MongoDatabase db = connect();
		MonthPojo pojo = insertEmpty(db);

		MongoCollection<MonthPojo> coll = db.getCollection(COLL_NAME, MonthPojo.class);
		MonthPojo read = coll.find().first();
		assertThat(read).isEqualToComparingFieldByField(pojo);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWithDocument() {
		MongoDatabase db = connect();
		MonthPojo pojo = insert(db);

		MongoCollection<Document> coll = db.getCollection(COLL_NAME);
		Document doc = coll.find().first();
		assertThat(doc).hasSize(6);
		assertThat(doc.get("_id")).isEqualTo(pojo.getId());
		assertThat(doc.get("scalar")).isEqualTo(1);
		assertThat((List<Integer>) doc.get("array")).containsExactly(2, 3, 4);
		assertThat((List<List<Integer>>) doc.get("array2"))
				.containsExactly(Arrays.asList(5), Arrays.asList(6, 7));
		assertThat((List<Integer>) doc.get("list")).containsExactly(8, 9);
		assertThat((List<Integer>) doc.get("set")).containsExactly(12);
	}

	@Test
	public void testEmptyWithDocument() {
		MongoDatabase db = connect();
		MonthPojo pojo = insertEmpty(db);

		MongoCollection<Document> coll = db.getCollection(COLL_NAME);
		Document doc = coll.find().first();
		assertThat(doc).hasSize(1);
		assertThat(doc.get("_id")).isEqualTo(pojo.getId());
	}

}
