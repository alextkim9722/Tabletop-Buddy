# React ToDos

* [x] Create a new React project with CRA

* [x] Create a component to display the list of ToDos
  * [x] Create component (ToDos)
  * [x] Display all ToDos on the screen

* [x] Create a form to add a ToDo
    * [x] create a input text
    * [x] label
    * [x] button to submit
    * [x] function to handle submit
    * [x] funciton to handle input change

* [x] Support deleting ToDos
  * [x] button to delete
  * [x] new function to delete
    * [x] update the todo list as part of the function

* [x] Support editing Todos
  * [x] create a form
  * [x] label
  * [x] button to submit
  * [x] function to handle submit
  * [x] funciton to handle input change

* [x] Conditional rendering of list, add, edit, and delete components
  * [x] display initially the list of todos, not the two forms
  * [x] when edit is clicked - display the edit form, no longer display the list of todos
  * [x] some kind of event to trigger add - display the add form
  * [x] when submit successfully - go back to the list
  * [x] maybe if time - display "are you sure" message when delete is pressed

* [x] Import and use Bootstrap for styling your components

* [x] Look for opportunities to create additional components
  * [x] Refactor AddTodo into its own component
  * [x] Reafactor EditTodo into its own component
  * [x] Refactor Delete into its own component

* [x] If time Object Tracking

## Routing
1. [x] install router v5
2. [x] create home and about pages
  * [x] setup the routing for home and about pages
  * [x] setup the links in the navbar
3. [x] setup routing for Todos
  * [x] Question: "What does the history object look like"
  * [x] setup the ToDos
    * [x] setup routing
    * [x] refactor out old code
  * [x] setup Add
    * [x] setup routing
    * [x] refactor out old code
  * [x] setup Update
    * [x] setup routing
    * [x] use params to get the id of the todo
    * [x] refactor out old code
  * [x] setup Delete
    * [x] setup routing
    * [x] refactor out old code

4. [x] handle 404
  * [x] create 404 page ( copy and paste the 404 from the LMS )
  * [x] create routing
  * [x] update fetch requests in order to use the 404 in the edit and delete pages

## Context
1. [x] Create temporary login page
2. [x] Create the context
  * [x] Initialize empty context
  * [x] Place the context in the app.js (context provider)
  * [x] Give the context a value that can be used in other places
  * [x] Set the login information on the login page
3. [x] Refactor to use the context in the appropriate places

## React Security
1. [x] add jwt_decode to the project
2. [x] update login page to get a token
3. [x] update the login to use the token
  * [x] use local storage to store the token
4. [x] add the token to the HTTP requests
  * [x] /add
  * [x] /edit
  * [x] /delete
  * [x] add new response (what happens if the user's token has gone stale?)
5. [x] add some additional conditionals for the user role
  * [x] some kind of additional funciton to check the role
  * [x] avoid displaying delete when the person logged in is not an ADMIN
6. [ ] create a registration page