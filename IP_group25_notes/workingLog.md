[202311201338] Produced initial class diagram

Marcus, Andreas & Sean discussed and drew an initial class diagram for task 1. 
The main topics we discussed were how to separate the act of searching from keeping the database itself.
We agreed to have one Main class, three minor classes for passing data and three major classes to drive the program. We discussed having a PageRanker class in the future, but did not include it at present. These classes are intended to become more advanced later.

The minor classes:

- **Page**:
    - Has a URL (String)
    - Has a Title (String)
    - Has content (List<String>)
    - Can display itself
- **PageList**:
    - Keeps a List<Page>
- **Query**:
    - Keeps a String of the search term

- **WebServer**: Displays results and receives input.
    - Defines interaction with client through HttpContexts.
    - Keeps a reference to the QueryHandler.
    - Passes search input as a String to QueryHandler, receives a response in the form of a PageList.
    - Displays results.
    - Handles SearchTermError.
- **QueryHandler**: Handles Query objects.
    - Transforms input (String) into a Query.
    - Keeps a reference to the Database.
    - Throws a SearchTermError if input strings is nonsense.
    - Sends Query to Database. Receives a PageList as a response.
    - Returns the PageList.
    - If we delete pagelist we need to change the wording in the methods. 
    - build inverted index in queryhandler either by manipulating the constructor in the database, build a method or create a new class. 
- **Database**: Keeps all data, and responds to Queries.
    - Constructs a PageList from the data file with all pages.
    - Receives a Query and returns a new PageList with the results.

- **Main**: Sets up the environment.
    - Creates the Database, the QueryHandler and the WebServer.
    - Informs the QueryHandler of the Database and the WebServer of the QueryHandler.