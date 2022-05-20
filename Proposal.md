# Problem Statement
Most tabletop games require a gamemaster and several other players that will be victim to his schemes. Games can last for a long time across several days or even months. As a result it is important to show up and play consistently.

For tabletop game enthusiasts, it is difficult to find the time in their schedule to attend a session. It is important to have a group of people that can meet consistently to start and finish a campaign.

# Proposal
Create an application where users can list out what times they are available. Based on their availability, users can see what sessions they can go to. Those that are hosting the games are able to plan out their own sessions based on the schedules of other players.
## Scenarios
### Scenario 1
Roger has always been interested in playing a game of DnD but he has never found a group that meets at times that work for him. Using the app we had created, Roger is now able to see all the available events that work for him.
### Scenario 2
Sarah is a gamemaster managing a party. She already has a set of people in her campaign but unfortunately, the game is lasting a lot longer than planned. As a result she must add several new sessions into the game in order to finish the campaign. Using the application, she can see the schedules of other people and from there add new sessions without impeding on their schedule.
### Scenario 3
David enjoys playing Warhammer 40k tabletop. He has already joined on campaign but he wants to join a few more. In order to prevent conflicts between these campaigns, David uses the application in order to pick the campaigns that don't have sessions that overlap with ones that he is already a part of.
### Scenario 4
Bob is a member of several tabletop campaigns but he recently found out that his friends are making a new campaign that he wants to be a part of. Unfortunately it clashes with several games he has already joined. Using the application, he can pinpoint which campaigns have sessions that overlap with the ones that his friends are creating. He can leave these campaigns in particular in order to make room to join his friend's campaign.
### Scenario 5
Jill wants to play Pathfinder, a tabletop RPG. Unfortunately for her, she has no way to communicate with all of the tabletop gamers around her as she doesn't really know what games are available nearby. Using the application, Jill is able to see what kinds of games, nearby campaigns are hosting. From there she no longer has to ask around and instead can use the app we made to join the campaign that is playing Pathfinder.

# Vocabulary
## Campaign
An object that contains a list of sessions (dates and times where games will be played). Users can sign up for these campaigns and the dungeon master can schedule it accordingly.
## Player
Individuals that will be joining the campaigns to play in these sessions. Their information will include dates and times they are available.
## GameMaster
Individuals that create campaigns for people to join. They will be able to see the availability of players and schedule events accordingly.
## Users
Individuals that have created an account on the website. They can be treated as either a player or a gamemaster depending on whether or not they created the campaign they are a part of.
## Session
### Personal/User Sessions
Periods of time in the user's schedule that is occupied by activities relating to personal matters and not tabletop gaming. These can include work, school, or other unrelated topics.
### Campaign Sessions
Periods of time created by the gamemaster for a specific campaign. These will be used to show the players what times they will meet up.

# Security Requirements
## Non-registered Users
They will be allowed to view all campaigns. Certain buttons and navigations features will not be available. These are listed below:
- Sehedule
- Join, edit, delete, and adding
## Registered Users
They will be allowed to view all campaigns and have full access to all of our features.
### Players
They will be allowed to join and leave a campaign.
### Gamemasters
They will be allowed to edit and delete the campaigns they have created.

# For All Users
## Listed View for Campaign
### Explanation
Users will see two separate tables showing off campaigns. One table will show off campaigns that they are hosting while another table will show off campaigns that are available. To make things more user friendly, users will be allowed to sort and filter out campaigns based on their preferences.

For the table where the user created the campaigns, buttons on side will show off if they wanted to delete or edit the campaign. for the table where the user did not create the campaigns, buttons on the side will show off if they want to join or leave the campaign.

Each list element will show a preview of the campaign data. The name, the location, and the start date for the next session. All people will be allowed to view it even if they don't have an account.
### Process
1. Pick on the campaign page on the navbar
2. Filter out based off of keywords if the user wants
3. Pick on the campaign on the list for a detailed view
## Creating an Account
### Data
- Username
- Password
- Description
- Location

All users will be allowed to create an account to use the features available on the website. If no account is created and user tries to use some of the features, they will be redirected to the login screen.

# For Gamemasters
## Adding a Campaign
### Data
- session count: How many sessions are in the campaign
- type of game: What kind of game the campaign is hosting
- description: A brief description about the campaign
- location: Where it is being hosted(Location filtering will be based off of states.)
- max participants: How many people can play the game
### Explanation
Users that add a campaign will be treated as a gamemaster and the campaign will have an indicator showing off who the gamemaster is.
Gamemasters after creation will be shown the detailed view of their newly created campaign. From here they may edit the campaign's data including session count/information.
### Process
1. User must create an account and log in.
2. Enter the campaign list
3. Pick the add campaign button
4. User is sent to the adding campaign view
    - Here they can set the data for the campaign
5. After submitting, the user is sent the published detailed page
    - Page shows off the data shown above
    - Shows off a list of sessions in a calendar format
    - Buttons on the page allow the user to edit and delete the campaign.
## Adding a Session
### Data
- Beginning Date
- End Date
### Explanation
Users that are gamemasters of the campaign will be allowed to change and edit these sessions. If the user is not a gamemaster, they will not be able to add a session for a campaign.

All users will be able to create sessions in their own personal schedule to show off when times they are avilable and what times they are not.
### Process
1. User must create an account and log in.
2. Enter the edit page for the campaign via list view or detailed view
3. Pick on add a session
4. Enter the session adding page
    - Page shows a calendar showing off all sessions including player schedules.
5. Enter the dates of the new session
6. Submit session
7. Redirected to the detailed campaign view.

# For Players
## Joining a Campaign
### Explanation
Users will be allowed to join a campaign as long as they are not the ones that created it. After the user joins a campaign they will be redirected to the detailed view of the campaign. If the user's sessions overlap with the campaigns sessions, the user will not be allowed to join the campaign.
### Process
1. User must create an account and log in.
2. Navigate to the campaign list view through the navbar.
3. Browse through the campaigns available.
    - Users will be allowed to filter based off of tags and keywords attached to a campaign or its description.
4. Pick on a campaign they want to play redirecting them to a more detailed page.
5. Join the campaign by picking on the button on the page.
    - If sessions within the player's schedule clash with the sessions on the campaign, the user will not be able to join and will receive an error message.
6. After joining, redirect them to the detailed campaign view.
## Setting Availability
### Explanation
Users will be allowed to set what times in the week they are not available. The week will be used to check availability with other sessions in the campaign. The user's schedule will be occupied by sessions that are not linked to campaigns. Users will be allowed to edit their schedule anytime by navigating to it through the navbar. Availability schedule will be represented by a calendar that uses the fullcalendar.io library.

If the user tries to join a campaign where the campaign sessions overlap with the user's personal sessions, they will not be allowed to. An error message will be shown as a result.

The user's schedule will show off times they are not available and campaign sessions that they have already joined.
### Process
1. User must create an account and log in.
2. Navigate to their personal schedule through the navbar.
3. Users will see a calendar that shows off all of the sessions for that week.
4. Users can pick add a session which allows them to block out available times and days within the week.
    - They will not be able to affect the sessions from campaign, only personal sessions that are unique to them.

# New Tech
- Full Calendar IO (https://fullcalendar.io)
    - The calendar library will be used to show off occupied times in a user friendly UI. They will be viewable from the schedule and the detailed view of the campaign.
- Lombok
    - Used to help quicken the development process by offering the coders tools to create getters, setters, and constructors without much hassle.