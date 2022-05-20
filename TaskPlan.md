# Priority Features
## High Priority
- Adding, editing, and deleting a campaign
- Adding, editing, and deleting a session
- Joining and leaving a campaign
- User profiles with location and availability
- Showing campaigns that fit into the player's schedule

## Mid Priority
- Sorting and filtering by max players, session count, type of game, starting date.
- Filter players based on tags.

## Low Priority
- Profile pictures
- Users can put in a bio

# Task Plan (35.0 hours)
Time Budget (66 hours)

## Database
### SQL Planning (3.0 hours)
1. [ ] Determine separate tables
2. [ ] Determine proper relationships

### SQL Creation (2.0 hours)
1. [ ] Create tables for entities
2. [ ] Create foreign keys
3. [ ] Create bridge tables
4. [ ] Populate the tables (for testing)

## Server (14.5 hours)
1. [ ] Create the models using Lombok (2.0 hours)
2. [ ] Create respository (3.5 hours)
    1. [ ] Create a template repository for the tables
        1. [ ] Create the interfaces
            1. [ ] Create the add, edit, delete methods
        2. [ ] Include the interfaces
    2. [ ] Create the mappers for certain templates
    3. [ ] Create the test cases for the repository 
2. [ ] Create service (3.0 hours)
    1. [ ] Create the connections to the repo
    2. [ ] Create the validations
        1. [ ] Autmomate validations
        2. [ ] Other validations
            - Overlapping dates
            - End date must be after start date
            - Duplicates (Users, sessions, campaigns)
    3. [ ] Create test cases for the service
3. [ ] Create controllers (4.0 hours)
    1. [ ] Create the handlers
        1. [ ] Implement CORS
        2. [ ] Mapping it to urls
    2. [ ] Test for different error exceptions using breakpoints
    3. [ ] Add tested errors into the global exception handler
4. [ ] Create security (2.0 hours)
    1. [ ] Establish code with antmatchers.

## Interface (15.5 hours)
1. [ ] Create a homepage (0.5 hours)
    - Introduction
2. [ ] Create a login screen (0.5 hours)
3. [ ] Create a registration screen (0.5 hours)
    - Username
    - Password
    - Empty Calendar
4. [ ] Create a campaign list view (4.0 hours)
    1. [ ] Shows off location, name, and next session date
    2. [ ] One table to show off campaigns the user created
    3. [ ] One table to show off campaigns others created
    4. [ ] When picking campaign, show off a detailed view
        1. [ ] Information about the campaign
        2. [ ] Same selections as before
        3. [ ] Calendar showing off sessions
5. [ ] Create a user schedule screen (4.0 hours)
    1. [ ] Use a calendar to set what times they are not available
        1. [ ] This is only for one week which will be reused
6. [ ] Create a campaign creation/edit/delete menu (2.0 hours)
7. [ ] Create a session creation/edit/delete menu (2.0 hours)
8. [ ] Navigation bar showing off components and user information (2.0 hours)
    - Homepage
    - Login/Registration
    - Campaigns
    - Users -- LOW PRIOIRTY --
    - Schedule
        - Show User availability
        - Show Campaign sessions in schedule -- LOW PRIOIRTY --




