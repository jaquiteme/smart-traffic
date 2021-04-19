const MongoClient = require('mongodb').MongoClient;
const assert = require('assert');

// credentials
const user = encodeURIComponent('root');
const password = encodeURIComponent('root');
const authMechanism = 'SCRAM-SHA-1';
const databaseAddr = "10.5.0.20"

// Connection URL
const url = `mongodb://${user}:${password}@${databaseAddr}:27017/?authMechanism=${authMechanism}`;

// Database Name
const dbName = 'myproject';

// Create a new MongoClient
const client = new MongoClient(url, { useUnifiedTopology: true, useNewUrlParser: true });

// Use connect method to connect to the Server
client.connect(function (err) {
  assert.equal(null, err);
  console.log("Connected successfully to server");

  const db = client.db(dbName);

  // insertDocuments(db, function () {
  //   client.close();
  // })
  findDocuments(db, function () {
    client.close()
  })

});

const insertDocuments = function (db, callback) {
  // Get the documents collection
  const collection = db.collection('documents');
  // Insert some documents
  collection.insertMany([
    { a: 1 }, { a: 2 }, { a: 3 }
  ], function (err, result) {
    assert.equal(err, null);
    assert.equal(3, result.result.n);
    assert.equal(3, result.ops.length);
    console.log("Inserted 3 documents into the collection");
    callback(result);
  });
}

const findDocuments = function (db, callback) {
  const collection = db.collection('documents');
  collection.find({ a: 2 }).toArray(function (err, docs) {
    assert.equal(err, null);
    console.log("Found the folowing records");
    console.log(docs)
    callback(docs);
  });
}