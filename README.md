# Treeservice
Solution for Coding Challenge by Alexander Keck built with Spring Boot. 
## Description
The tree is stored in the database as a adjacency list. Each row in the table contains a nodes id and the relation to its parent thus recursive common table expressions can be used to fetch the descendants of the node with high performance and changing the parent of a node can simply be done by updating the node's reference to the parent. Additionally to the node's id and the relation to its parent the database holds the root id and the height, otherwise this information would have to be queried separately. The downside of this is the fields of the descendants of a node have to be updated when the node is moved to another parent. But the impact on the performance of the change parent operation is less than the impact of the performance of the get descendants operation if the height and the root id would be determined during the transaction.
## Getting Started
Clone this repository via 
```
git clone git@github.com:lxndrdnxl/treeservice.git
```
or 
```
git clone https://github.com/lxndrdnxl/treeservice.git
``` 
in the case of using HTTPS.

Run  
```
docker-compose up
```
to create and start the containers running the treeservice and it's postgresql database as well as a container running tests.

When the containers are running the treeservice is accessible via localhost:8080/.
### Preequisites
A running docker instance on the server is everything needed to run this application.
## Required APIs
### Get all descendants of a given node
Will retrieve all descendants of the given node. The returned list does not include the given node. 
```http
GET /node/descendants/{nodeId}
``` 
#### Parameters
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `nodeId` | `Long` | **Required** |
#### Response
If the response was succcessful:
```javascript
[
  {
    "id":1062,
    "parentId":1183,
    "rootId":1183,
    "height":1},
  {
    "id":1184,
    "parentId":1183,
    "rootId":1183,
    "height":1},
  {
    "id":1185,
    "parentId":1183,
    "rootId":1183,
    "height":1}
]
``` 
The API will return HTTP code 404 if the given node does not exist.
### Change the parent of a given node
Will update the parentId of the given node. Will update it's descendants height accordingly. The new parent can be in another tree. The new parent may not be a descendant of the given node. Returns a object representation of the node with the new parent id and if changed root id and height. 
```http
PUT /node/moveNode
```
#### Request
```javascript
{
  nodeId: 1062
  newParentId: 1184
}
```
#### Parameters
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `nodeId` | `Long` | **Required** |
| `newParentId` | `Long` | **Required** |
#### Response
If the transaction was successfull:
```javascript
{
  "id": 1062,
  "parentId": 1184,
  "rootId": 1183,
  "height": 2
}
```
The Api will return HTTP code 404 if the given nodes do not exists and 400 if the new parent is a descendant of the node.
## Additional APIs
### Create a new tree
Creates and returns a new root node.
```http
GET /node/createTree
```
#### Response
```javascript
{
  "id": 1200,
  "rootId": 1200,
  "height": 0
}
```
### Add Child
Creates a new node with the given node as a parent. Returns a object representaion of the newly created node.
```http
POST /node/addChild
```
#### Request
```javascript
{
  nodeId: 1062
}
```
#### Parameters
| Parameter | Type | Description |
| :--- | :--- | :--- |
| `nodeId` | `Long` | **Required** |
#### Response
If the transaction was successfull:
```javascript
{
  "id": 1063,
  "parentId": 1062,
  "rootId": 1062,
  "height": 1
}
```
The Api will return HTTP code 404 if the given node does not exist.
## Tests

## Built With
* [Spring Boot] (https://spring.io/projects/spring-boot) - Web and DI framework used
* [Spring Data JPA] (https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#project) - To use their repositories to connect with the database
* [PostgreSQL] (https://www.postgresql.org/) - For persistence
* [JUnit 5] (https://junit.org/junit5/docs/current/user-guide/) - As a test platform
* [Mockito] (https://site.mockito.org/) - To ease stubbing inside unit tests
* [AssertJ] (https://joel-costigliola.github.io/assertj/) - For fluent and thus readable assertions in tests
* [testcontainers] (https://www.testcontainers.org/) - To run a PostgreSQL database for integration tests
* [Apache Maven] (https://www.testcontainers.org/) - For build and dependency management
