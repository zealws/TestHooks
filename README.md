# TestHooks

Testing framework using hooks and HTTP to notify tests of changes in remote application.

## Using TestHooks

### Setup

TestHooks uses a database to pass configuration information between the test suite and the production application, so you will need to have one available to both the production application and the test suite.

The schema of this database can be maintained using [DbMigrations](http://github.com/zfjagann/DbMigrations/):

`python -m dbmigrations -b sql`

Be aware that this will attempt to connect to the database 'testhooks' with user and password 'testhooks'. If you would like to change this, edit `sql/dbmigrations.conf` and rename `sql/testhooks` to match the name of your database.

### Production Application

To configure your production application to use your database, you must specify in your production application how to connect to the database. For example:

    static {
        Hook.initializeDb("jdbc:postgresql://hostname:port/database?user=dbuser&password=dbpass");
    }

Once this confugration has been done, you can create a new [Hook](http://github.com/zfjagann/TestHooks/blob/master/src/testhooks/source/Hook.java) in a production application, simply embed a new Hook in your application:

`private Hook hook = new Hook("application-name");`

After that point, you can add new data points to this hook:

`hook.add("key", value);`

Once you have added all the data points, you can send the data to the test suite by using the `send` method:

`hook.send();`

For an exmaple application, see [CountingApplication](http://github.com/zfjagann/TestHooks/blob/master/example/testhooks/example/app/CountingApplication.java).

### Test Suite

First, your test class that needs access to hook information needs to extend `HookTestlet`. This will automatically startup and shutdown the server between test cases to ensure that hook information from one test does not cause issues with other tests.

The test suite needs to be configured to use the same database as the production application:

    static {
        HookManager.initializeDb("jdbc:postgresql://hostname:port/database?user=dbuser&password=dbpass");
    }

Extending `HookTestlet` also gives access to a [HookManager](https://github.com/zfjagann/TestHooks/blob/master/src/testhooks/test/HookManager.java), which is how tests access information about hooks. To use it, simply call it's `get` method:

`hookManager.get("application-name", "key")`

The first parameter will be the application name, and the second the key of the data point you with to fetch.

*Note*: Keep in mind that when the test starts executing, no hook calls may have been made. As a result, tests might need to sleep for some amount of time after starting to ensure that hook data has been delivered to the test suite.

For an example of a working test suite, see [CountingApplicationRemoteTest](https://github.com/zfjagann/TestHooks/blob/master/example/testhooks/example/test/CountingApplicationRemoteTest.java).

*Note*: if more than one application is required, the `HookManager.initializeDb` function may be applied to more than 1 JDBC connection, in which case all the connections are setup and cleaned up by the tests.

## Details

Gorey details, in no particular order:

- A Restlet HTTP server is brought up/down while running the Test Suite. This is how information is passed from the production application to the test suite.

- The `Hook.send` method only results in an HTTP request if a configured test suite is found. This way, no request is made unless a test suite is running.

- The request from production application to test suite uses java serialization to format the data into plain text. In the future, this will likely be changed to JSON to support more types of test suites. For example, python test suites could then be build without requiring java serialization.